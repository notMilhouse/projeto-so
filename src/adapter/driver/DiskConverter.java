package src.adapter.driver;

import src.application.management.exceptions.InvalidEntryException;
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

public class DiskConverter
{
    File disk;
    RandomAccessFile diskAccess;
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

    public void Read()
    {
        try
        {
            diskAccess = new RandomAccessFile(disk, "r");

            SNodeBitmapRef = 28*NumberOfSnodes;                                                 //Snode [28]bytes
            DatablockBitmapRef = SNodeBitmapRef + NumberOfSnodes/8 + NumberOfDatablocks*128;    //Datablock 128 bytes

            //Criar Bitmaps
            LoadBitmap();

            root = ParseSNode(0);
            diskAccess.close();
        }
        catch(Exception err)
        {
            System.err.println(err);
        }
        
    }

    public void Write()
    {
        try
        {
            diskAccess = new RandomAccessFile(disk, "w");
            //Persiste no arquivo
            diskAccess.close();
        }
        catch(Exception err)
        {
            System.err.println(err);
        }
    }

    public SNodeDir GetRoot()
    {
        return (SNodeDir)root;
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
            Type:           1 byte
            generation:     1 byte
            creationDate    8 bytes
            creationDate    8 bytes
            length          2 bytes
            DataBlockRef1   2 bytes (unsigned)
            DataBlockRef2   2 bytes (unsigned)
            DataBlockRef3   2 bytes (unsigned)
            DataBlockRef4   2 bytes (unsigned)
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
                dataBlocksInBitmap[0] = (dataBlockRef - (DatablockBitmapRef + NumberOfDatablocks))/128; //TODO isso n faz sentido favor arrumar

                while(diskAccess.getFilePointer() < position + length) //TODO Ver se esse while faz sentido
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

                    dataBlocksInBitmap[i] = (dataBlockRef - (DatablockBitmapRef + NumberOfDatablocks))/128; //TODO isso n faz sentido favor arrumar


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

        DEntry dEntry = new DEntry(snode, EntryLength, type, filename);

        diskAccess.seek(atRef - 2 + EntryLength);   //Vai para o final do DEntry (should)
        return dEntry;
    }
    /*
    public byte[] ReadBytes(int numberOfBytes)
    throws IOException
    {
        byte[] byteArray = new byte[numberOfBytes];
        diskAccess.read(byteArray, 0, numberOfBytes);
        return byteArray;
    }

    public long ByteArrayToLong(byte[] byteArray)
    {
        ByteBuffer wrapper = ByteBuffer.wrap(byteArray);
        return wrapper.getLong();
    }
    public int ByteArrayToInt(byte[] byteArray)
    {
        ByteBuffer wrapper = ByteBuffer.wrap(byteArray);
        return wrapper.getInt();
    }
    public int ByteArrayToUnsignedShort(byte[] byteArray)
    {
        ByteBuffer wrapper = ByteBuffer.wrap(byteArray);
        short ShortValue = wrapper.getShort();
        return ShortValue >= 0? ShortValue : 0x10000 + ShortValue;
    }
    public short ByteArrayToShort(byte[] byteArray)
    {
        ByteBuffer wrapper = ByteBuffer.wrap(byteArray);
        return wrapper.getShort();
    }
    */
}