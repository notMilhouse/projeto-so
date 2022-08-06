package src.adapter.driver;

import src.application.management.exceptions.InvalidEntryException;
import src.application.management.exceptions.VirtualFileNotFoundException;

import src.domain.snode.dentry.DEntry;
import src.domain.snode.FileType;
import src.domain.snode.SNode;
import src.domain.snode.SNodeDir;
import src.domain.snode.SNodeFile;
import src.domain.bitmap.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DiskConverter {
    File disk;
    RandomAccessByteArray diskAccess;
    RandomAccessFile _diskAccess;
    //FileInputStream diskIn;
    //FileOutputStream diskOut;
    SNode root;

    int NumberOfSnodes;
    int NumberOfDatablocks;

    int SNodeBitmapRef;
    int DatablockBitmapRef;

    BitMap SNodeBitmap;
    BitMap DatablockBitmap;

    public DiskConverter(File file, int numberOfSnodes, int numberOfDatablocks)
    {
        disk = file;
        NumberOfSnodes = numberOfSnodes;
        NumberOfDatablocks = numberOfDatablocks;
    }


    private void newDisk()
    {
        SNodeBitmapRef = 28*NumberOfSnodes;                                                 //Snode [28]bytes
        DatablockBitmapRef = SNodeBitmapRef + NumberOfSnodes/8 + NumberOfDatablocks*128;    //Datablock 128 bytes

        byte[] data = new byte[DatablockBitmapRef + NumberOfDatablocks/8];
        diskAccess = new RandomAccessByteArray(data);
        SNodeDir _temp = new SNodeDir();
        _temp.ChangeGeneration((byte)1);

        _temp.SetBitmap(0, new int[1]);
        try
        {
            SNodeBitmap = new BitMap(NumberOfSnodes);
            DatablockBitmap = new BitMap(NumberOfDatablocks);
            SNodeBitmap.allocateSlot();
            DatablockBitmap.allocateSlot();
        }
        catch(Exception err){}

        //Updating bitmaps
        diskAccess.seek(SNodeBitmapRef);
        diskAccess.write(SNodeBitmap.toBits());
        diskAccess.seek(DatablockBitmapRef);
        diskAccess.write(DatablockBitmap.toBits());

        //Writing Root
        diskAccess.seek(0);
        diskAccess.write(_temp.toBits());

        //Loading disk
        root = ParseSNode(0);

    }

    /**
     * Mounts virtual disk
     */
    public void Read()
    {
        try
        {
            SNodeBitmapRef = 28*NumberOfSnodes;                                                 //Snode [28]bytes
            DatablockBitmapRef = SNodeBitmapRef + NumberOfSnodes/8 + NumberOfDatablocks*128;    //Datablock 128 bytes

            _diskAccess = new RandomAccessFile(disk, "r");
            byte[] data = new byte[DatablockBitmapRef + NumberOfDatablocks/8];

            _diskAccess.readFully(data);

            diskAccess = new RandomAccessByteArray(data);
            _diskAccess.close();

            //Criar Bitmaps
            LoadBitmap();

            root = ParseSNode(0);
        }
        catch(IOException err)
        {
            if( err instanceof java.io.FileNotFoundException)
            {
                newDisk();
                return;
            }

            System.out.println(err);
        }
        catch(Exception err)
        {
            System.err.println("Disk Read:" + err);
        }
    }

    /**
     * Persists virtual disk modification
     * 
     * @throws IOException
     */
    public void SaveDisk()
    throws IOException
    {
        _diskAccess = new RandomAccessFile(disk, "rw");
        _diskAccess.seek(0);
        _diskAccess.write(diskAccess.GetByteArray());
        _diskAccess.close();
    }


    /**
     * In instance:
     * Builds and adds DEntry reference to specified directory.
     * Updates snode reference in bitmap
     * Updates snode datablock(s) reference in bitmap
     * Update size of directory
     * 
     * In disk:
     * Sets snode reference in bitmap.
     * Sets all snode datablocks references in bitmap.
     * Adds dentry to directory datablock.
     * Update size of directory
     * 
     * @param dir Directory to insert snode
     * @param snode Snode to be inserted in specified Directory
     * @param name Snode entry name
     * @return Success of operation
     * @throws InvalidEntryException
     * @throws VirtualFileNotFoundException
    */
    public boolean WriteSNode(SNodeDir dir, SNode snode, String name)
    throws InvalidEntryException, VirtualFileNotFoundException
    {

        DEntry dentry = new DEntry(snode, snode.GetFileType(), name);
        int[] datablockSlots = new int[snode.GetNumberOfDatablocks()];

        int offset = 0;
        for(int i = 0; i < dir.numberOfFilesInDir(); i++)
        {
            offset += dir.getDEntryAtIndex(i).getLength();
        }
        if(offset + dentry.getLength() > 128)
        {
            return false;
        }

        //Setting up bitmaps
        for(int i = 0; i < snode.GetNumberOfDatablocks(); i++)
        {
            try
            {
                datablockSlots[i] = DatablockBitmap.allocateSlot();
            }
            catch(Exception err)
            {
                return false; //TODO DEALLOCATE
            }
        }

        int snoderef = 0;
        try
        {
            snoderef = SNodeBitmap.allocateSlot();
        }
        catch(Exception err)
        {
            return false; //TODO DEALLOCATE
        }
        snode.SetBitmap(snoderef, datablockSlots);

        //Updating bitmaps
        diskAccess.seek(SNodeBitmapRef);
        diskAccess.write(SNodeBitmap.toBits());
        diskAccess.seek(DatablockBitmapRef);
        diskAccess.write(DatablockBitmap.toBits());

        //Adding DEntry
        dir.InsertDEntry(dentry);
        diskAccess.seek(SNodeBitmapRef + NumberOfSnodes/8 + dir.getDatablocksReferences()[0]*128 + offset);
        diskAccess.write(dentry.toBits());

        //Increment Generation
        diskAccess.seek(snoderef*28 + 1);
        byte generation = diskAccess.readByte();
        snode.ChangeGeneration(++generation);

        //Write Snode
        diskAccess.seek(snoderef*28);
        diskAccess.write(snode.toBits());

        //Update size of directory
        diskAccess.seek(dir.getIndexInBitmap()*28);
        diskAccess.write(dir.toBits());

        return true;
    }

    /**
     * In instance:
     * Removes target snode from Directory DEntry list.
     * 
     * In disk:
     * Unsets snode reference in bitmap.
     * Unsets all snode datablocks references in bitmap.
     * DEfrags specified Directory DEntry datablock.
     * 
     * Fails if target snode is a non-empty directory.
     * @param dir Directory from which target snode will be deleted
     * @param snode Snode to be deleted from specified Directory
     * @return Success of operation
     * @throws VirtualFileNotFoundException
    */
    public boolean DeleteSNode(SNodeDir dir, SNode snode)
    throws VirtualFileNotFoundException
    {
        
        if(snode.GetFileType() == FileType.Directory)
        {
            if(((SNodeDir)snode).numberOfFilesInDir() != 0)
            {
                return false;
            }
        }
        //Att o bitmap
        //int snodeRef = snode.getIndexInBitmap() * 28;
        SNodeBitmap.freeSlot(snode.getIndexInBitmap());


        int[] dataBlocksRef = new int[snode.GetNumberOfDatablocks()];
        int index = 0;
        for(int ref : snode.getDatablocksReferences())
        {
            dataBlocksRef[index] = SNodeBitmapRef + NumberOfSnodes/8 + ref*128;
            DatablockBitmap.freeSlot(ref);
            index++;
        }

        diskAccess.seek(SNodeBitmapRef);
        diskAccess.write(SNodeBitmap.toBits());
        diskAccess.seek(DatablockBitmapRef);
        diskAccess.write(DatablockBitmap.toBits());

        

        //Remover DEntry
        int offset = 0;
        int size = 0;
        DEntry dentry;
        for(int i = 0; i < dir.numberOfFilesInDir(); i++)
        {
            dentry = dir.getDEntryAtIndex(i);
            
            if(dentry.getSnode() == snode)
            {
                size = dentry.getLength();
                dir.removeDEntry(i);
                break;
            }
            offset += dentry.getLength();
        }

        //Defragmentar o DEntry
        diskAccess.seek(SNodeBitmapRef + NumberOfSnodes/8 + dir.getDatablocksReferences()[0]*128 + offset + size);
        byte[] datablockRemainder = new byte[128 - offset + size];
        diskAccess.readFully(datablockRemainder);
        diskAccess.seek(SNodeBitmapRef + NumberOfSnodes/8 + dir.getDatablocksReferences()[0]*128 + offset);
        diskAccess.write(datablockRemainder);

        //Update directory
        diskAccess.seek(dir.getIndexInBitmap()*28);
        diskAccess.write(dir.toBits());

        return true;
    }

    /**
     * Returns virtual disk root directory.
     * Returns {@literal NULL} if virtual disk hasn't been mounted
     * 
     * @return Filesystem root directory
     */
    public SNode GetRoot()
    {
        return root;
    }

    private void LoadBitmap()
    throws IOException
    {
        byte[] ReadBytes = new byte[NumberOfSnodes/8];
        String byteString = "";
            
        diskAccess.seek(SNodeBitmapRef);
        diskAccess.readFully(ReadBytes);

        for(byte b : ReadBytes)
        {
            byteString += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0");
        }
        SNodeBitmap = new BitMap(byteString);
    
        ReadBytes = new byte[NumberOfDatablocks/8];
        diskAccess.seek(DatablockBitmapRef);
        diskAccess.readFully(ReadBytes);

        byteString = "";
        for(byte b : ReadBytes)
        {
            byteString += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0");
        }
        DatablockBitmap = new BitMap(byteString);

    }

    /**
    *
    */
    public SNode ParseSNode(int atRef)
    {
        /*
            Snode builder
            Type:               1 byte
            generation:         1 byte
            creationDate        8 bytes
            modificationDate    8 bytes
            length              2 bytes
            DataBlockRef1       2 bytes (unsigned)
            DataBlockRef2       2 bytes (unsigned)
            DataBlockRef3       2 bytes (unsigned)
            DataBlockRef4       2 bytes (unsigned)
        */
        SNode snode;
        try
        {
            diskAccess.seek(atRef);
            FileType type = FileType.parseFileType(diskAccess.readByte());  //Reads [1]
            byte generation = diskAccess.readByte();                        //Reads [1]
            long creationDate = diskAccess.readLong();                      //Reads [8]
            long modificationDate = diskAccess.readLong();                  //Reads [8]
            short length = diskAccess.readShort();                          //Reads [2]
            int dataBlockRef = diskAccess.readUnsignedShort();              //Reads [2]
            
            int[] dataBlocksInBitmap;

            if(type == FileType.Directory)
            {
                snode = new SNodeDir();
                diskAccess.seek(SNodeBitmapRef + NumberOfSnodes/8 + dataBlockRef*128);   
                long position = diskAccess.getFilePointer();
                
                dataBlocksInBitmap = new int[1];       
                dataBlocksInBitmap[0] = dataBlockRef;

                while(diskAccess.getFilePointer() < position + length) 
                {
                    DEntry dEntry = ParseDir((int)diskAccess.getFilePointer());
                    snode.InsertDEntry(dEntry); //Erro de intellisense
                }
            }
            else
            {
                snode = new SNodeFile(type, length);
                int nDataBlocks = snode.GetNumberOfDatablocks(); //Erro de intellisense
                long position = diskAccess.getFilePointer();

                dataBlocksInBitmap = new int[nDataBlocks];


                for(int i = 0; i < nDataBlocks; i++)
                {

                    dataBlocksInBitmap[i] = dataBlockRef;

                    diskAccess.seek(SNodeBitmapRef + NumberOfSnodes/8 + dataBlockRef*128);
                    diskAccess.readFully(snode.DataBlockByIndex(i)); //Erro de intellisense
                    diskAccess.seek(position);
                    dataBlockRef = diskAccess.readUnsignedShort();
                    position = diskAccess.getFilePointer();
                }
            }
            snode.ChangeCreationDate(creationDate);
            snode.ChangeModificationDate(modificationDate);
            snode.ChangeGeneration(generation);

            int snodeIndexInBitmap = atRef/28;

            snode.SetBitmap(snodeIndexInBitmap, dataBlocksInBitmap);
        }
        catch(Exception err)
        {
            System.err.println(err);
            return null;
        }
        return snode;    
    }

    public DEntry ParseDir(int atRef) throws IOException, InvalidEntryException
    {
        /*
            DEntry Builder
            SNode Identifier:   2 bytes (unsigned)
            EntryLength:        2 bytes (unsigned)
            Filetype:           1 bytes
            FileNameLength      1 bytes (min 1 max 122): n
            FileName            n bytes
        */

        //Da pra simplificar isso daqui..
        diskAccess.seek(atRef);
        int snodeRef = diskAccess.readUnsignedShort();
        atRef = (int) diskAccess.getFilePointer();
        SNode snode = ParseSNode(snodeRef*28);
        diskAccess.seek(atRef);

        //Montar DEntry
        int EntryLength = diskAccess.readUnsignedShort();
        FileType type = FileType.parseFileType(diskAccess.readByte());
        int filenameLength = diskAccess.readUnsignedByte();
        byte[] tempBuffer = new byte[filenameLength];
        diskAccess.readFully(tempBuffer);
        String filename = new String(tempBuffer, StandardCharsets.ISO_8859_1); //Latin 8 bit charset

        DEntry dEntry = new DEntry(snode, type, filename);

        diskAccess.seek(atRef - 2 + EntryLength);   //Vai para o final do DEntry (should)
        return dEntry;
    }
}