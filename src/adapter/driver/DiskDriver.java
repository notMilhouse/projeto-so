package src.adapter.driver;

import src.adapter.driver.exceptions.VirtualFileNotFoundException;
import src.domain.bitmap.BitMap;
import src.domain.bitmap.exceptions.BitMapNextFitNotFoundException;
import src.domain.bitmap.exceptions.BitMapPositionAlreadySetException;
import src.domain.disk.VirtualDisk;
import src.domain.snode.FileType;
import src.domain.snode.SNode;
import src.domain.snode.SNodeDir;
import src.domain.snode.SNodeFile;
import src.domain.snode.dentry.DEntry;
import src.domain.snode.dentry.exceptions.InvalidEntryException;
import src.domain.snode.exceptions.InvalidLengthForSnodeException;
import src.domain.snode.exceptions.InvalidSNodeException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * DiskDriver is the disk driver responsible for manipulating the virtual disk
 *
 * @see VirtualDisk
 * @see RandomAccessFile
 * @see SNode
 */

public class DiskDriver {
    File diskImage;
    VirtualDisk virtualDisk;
    RandomAccessFile hardDisk;
    SNode root;

    int NumberOfSnodes;
    int NumberOfDatablocks;

    int SNodeBitmapPositionInDisk;
    int DatablockBitmapPositionInDisk;

    BitMap SNodeBitmap;
    BitMap DatablockBitmap;

    /**
     * Constructor of the driver
     *
     * @param file               the file containing a source disk image
     * @param numberOfSnodes     the number of {@link SNode} for the disk
     * @param numberOfDatablocks the number of datablocks for the disk
     * @throws Exception
     */
    public DiskDriver(File file, int numberOfSnodes, int numberOfDatablocks) throws Exception {
        if(numberOfSnodes%8 != 0 || numberOfDatablocks%8 != 0)
        {
            throw new Exception("Number of snodes and datablocks must be divisible by 8");
        }

        diskImage = file;
        NumberOfSnodes = numberOfSnodes;
        NumberOfDatablocks = numberOfDatablocks;
    }

    /**
     * Configures a new disk given the driver instance attributes
     * by creating a root directory and writing to the beginning of the disk
     * It also configures the respective bitmaps.
     * @throws InvalidSNodeException
     */
    private void newDisk() throws BitMapPositionAlreadySetException, BitMapNextFitNotFoundException, InvalidEntryException, IOException, InvalidLengthForSnodeException, InvalidSNodeException {
        SNodeBitmapPositionInDisk = 28 * NumberOfSnodes;                                                 //Snode [28]bytes
        DatablockBitmapPositionInDisk = SNodeBitmapPositionInDisk + NumberOfSnodes / 8 + NumberOfDatablocks * 128;    //Datablock 128 bytes

        byte[] data = new byte[DatablockBitmapPositionInDisk + NumberOfDatablocks / 8];
        virtualDisk = new VirtualDisk(data);
        SNodeDir temporaryRoot = new SNodeDir();
        temporaryRoot.ChangeGeneration((byte) 1);

        temporaryRoot.SetBitmap(0, new int[1]);

        SNodeBitmap = new BitMap(NumberOfSnodes);
        DatablockBitmap = new BitMap(NumberOfDatablocks);
        SNodeBitmap.allocateSlot();
        DatablockBitmap.allocateSlot();


        //Updating bitmaps
        virtualDisk.seek(SNodeBitmapPositionInDisk);
        virtualDisk.write(SNodeBitmap.toBits());
        virtualDisk.seek(DatablockBitmapPositionInDisk);
        virtualDisk.write(DatablockBitmap.toBits());

        //Writing Root
        virtualDisk.seek(0);
        virtualDisk.write(temporaryRoot.toBits());

        //Loading disk
        root = ParseSNode(0);

    }

    /**
     * Mounts virtualDisk from the diskImage
     *
     * @throws IOException in case something unexpected goes wrong
     * @throws InvalidSNodeException
     */
    public void MountDisk() throws IOException, BitMapPositionAlreadySetException, BitMapNextFitNotFoundException, InvalidEntryException, InvalidLengthForSnodeException, InvalidSNodeException {
        try {
            SNodeBitmapPositionInDisk = 28 * NumberOfSnodes;                                                 //Snode [28]bytes
            DatablockBitmapPositionInDisk = SNodeBitmapPositionInDisk + NumberOfSnodes / 8 + NumberOfDatablocks * 128;    //Datablock 128 bytes

            hardDisk = new RandomAccessFile(diskImage, "r");
            byte[] data = new byte[DatablockBitmapPositionInDisk + NumberOfDatablocks / 8];

            hardDisk.readFully(data);

            virtualDisk = new VirtualDisk(data);
            hardDisk.close();

            //Criar Bitmaps
            LoadBitmap();

            root = ParseSNode(0);
        } catch (FileNotFoundException e) {
            System.out.println("Disk image does not exist, creating a new one...");
            newDisk();
        }
    }

    /**
     * Persists virtualDisk to hardDisk
     *
     * @throws IOException when something unexpected goes wrong
     */
    public void SaveDisk()
        throws IOException {
        hardDisk = new RandomAccessFile(diskImage, "rw");
        hardDisk.seek(0);
        hardDisk.write(virtualDisk.GetByteArray());
        hardDisk.close();
    }


    /**
     * For the disk structure in memory:
     * <ul>
     *     <li>Builds and adds DEntry reference to specified directory.</li>
     *     <li>Updates snode reference in bitmap</li>
     *     <li>Updates snode datablock(s) reference in bitmap</li>
     *     <li>Update size of directory</li>
     * </ul>
     * <p>
     * For the virtualDisk:
     * <ul>
     *     <li>Sets snode reference in bitmap.</li>
     *     <li>Sets all snode datablocks references in bitmap.</li>
     *     <li>Adds dentry to directory datablock.</li>
     *     <li>Update size of directory</li>
     * </ul>
     *
     * @param dir   the directory to insert the new {@link SNode}
     * @param snode {@link SNode} to be inserted in specified Directory
     * @param name  {@link SNode} entry name
     * @return true if operation is successful, false otherwise
     * @throws InvalidEntryException when invalid entry is given
     */
    public boolean WriteSNode(SNodeDir dir, SNode snode, String name)
        throws InvalidEntryException {

        DEntry dentry = new DEntry(snode, snode.GetFileType(), name);
        int[] datablockSlots = new int[snode.GetNumberOfDatablocks()];

        int offset = 0;
        for (int i = 0; i < dir.numberOfFilesInDir(); i++) {
            offset += dir.getDEntryAtIndex(i).getLength();
        }
        if (offset + dentry.getLength() > 128) {
            return false;
        }

        //Setting up bitmaps
        for (int i = 0; i < snode.GetNumberOfDatablocks(); i++) {
            try {
                datablockSlots[i] = DatablockBitmap.allocateSlot();
            } catch (Exception err) {
                // If anything goes wrong the operation must be reverted
                // by deallocating the allocated slots
                for (int j = 0; j < i; j++) {
                    DatablockBitmap.freeSlot(datablockSlots[j]);
                }
                return false;
            }
        }

        int snoderef = 0;
        try {
            snoderef = SNodeBitmap.allocateSlot();
        } catch (Exception err) {
            for (int datablockSlot : datablockSlots) {
                DatablockBitmap.freeSlot(datablockSlot);
            }
            return false;
        }
        snode.SetBitmap(snoderef, datablockSlots);

        //Updating bitmaps
        virtualDisk.seek(SNodeBitmapPositionInDisk);
        virtualDisk.write(SNodeBitmap.toBits());
        virtualDisk.seek(DatablockBitmapPositionInDisk);
        virtualDisk.write(DatablockBitmap.toBits());

        //Adding DEntry
        dir.InsertDEntry(dentry);
        virtualDisk.seek(SNodeBitmapPositionInDisk + NumberOfSnodes / 8 + dir.getDatablocksReferences()[0] * 128 + offset);
        virtualDisk.write(dentry.toBits());

        //Increment Generation
        virtualDisk.seek(snoderef * 28 + 1);
        byte generation = virtualDisk.readByte();
        snode.ChangeGeneration(++generation);

        //Write Snode
        virtualDisk.seek(snoderef * 28);
        virtualDisk.write(snode.toBits());

        //Update size of directory
        virtualDisk.seek(dir.getIndexInBitmap() * 28);
        virtualDisk.write(dir.toBits());

        return true;
    }

    /**
     * For the disk structure in memory:
     * <ul>
     *     <li>Removes target snode from Directory DEntry list.</li>
     * </ul>
     * For the virtualDisk:
     * <ul>
     * <li>Unsets snode reference in bitmap.</li>
     * <li>Unsets all snode datablocks references in bitmap.</li>
     * <li>DEfrags specified Directory DEntry datablock.</li>
     * </ul>
     * Fails if target snode is a non-empty directory.
     *
     * @param dir   Directory from which target snode will be deleted
     * @param snode Snode to be deleted from specified Directory
     * @return Success of operation
     * @throws VirtualFileNotFoundException when file does not exist
     */
    public boolean DeleteSNode(SNodeDir dir, SNode snode)
        throws VirtualFileNotFoundException {

        if (snode.GetFileType() == FileType.Directory) {
            if (((SNodeDir) snode).numberOfFilesInDir() != 0) {
                return false;
            }
        }
        //Updates the bitmap
        SNodeBitmap.freeSlot(snode.getIndexInBitmap());

        for (int ref : snode.getDatablocksReferences()) {
            DatablockBitmap.freeSlot(ref);
        }

        virtualDisk.seek(SNodeBitmapPositionInDisk);
        virtualDisk.write(SNodeBitmap.toBits());
        virtualDisk.seek(DatablockBitmapPositionInDisk);
        virtualDisk.write(DatablockBitmap.toBits());

        //Remover DEntry
        int offset = 0;
        int size = 0;
        DEntry dentry;
        for (int i = 0; i < dir.numberOfFilesInDir(); i++) {
            dentry = dir.getDEntryAtIndex(i);

            if (dentry.getSnode() == snode) {
                size = dentry.getLength();
                dir.removeDEntry(i);
                break;
            }
            offset += dentry.getLength();
        }

        //Defragmentar o DEntry
        virtualDisk.seek(SNodeBitmapPositionInDisk + NumberOfSnodes / 8 + dir.getDatablocksReferences()[0] * 128 + offset + size);
        byte[] datablockRemainder = new byte[128 - offset + size];
        virtualDisk.readFully(datablockRemainder);
        virtualDisk.seek(SNodeBitmapPositionInDisk + NumberOfSnodes / 8 + dir.getDatablocksReferences()[0] * 128 + offset);
        virtualDisk.write(datablockRemainder);

        //Update directory
        virtualDisk.seek(dir.getIndexInBitmap() * 28);
        virtualDisk.write(dir.toBits());

        return true;
    }

    /**
     * Returns virtual disk root directory.
     *
     * @return Filesystem root directory or null if virtual disk hasn't been mounted
     */
    public SNode GetRoot() {
        return root;
    }

    /**
     * Loads both snode and datablock bitmaps' from the virtualDisk
     */
    private void LoadBitmap() {
        byte[] ReadBytes = new byte[NumberOfSnodes / 8];
        StringBuilder byteString = new StringBuilder();

        virtualDisk.seek(SNodeBitmapPositionInDisk);
        virtualDisk.readFully(ReadBytes);

        for (byte b : ReadBytes) {
            byteString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0"));
        }
        SNodeBitmap = new BitMap(byteString.toString());

        ReadBytes = new byte[NumberOfDatablocks / 8];
        virtualDisk.seek(DatablockBitmapPositionInDisk);
        virtualDisk.readFully(ReadBytes);

        byteString = new StringBuilder();
        for (byte b : ReadBytes) {
            byteString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0"));
        }
        DatablockBitmap = new BitMap(byteString.toString());

    }

    /**
     * Given a position in the virtualDisk, parses a SNode object
     * @throws InvalidSNodeException
     */
    public SNode ParseSNode(int snodePositionInVirtualDisk) throws InvalidEntryException, IOException, InvalidLengthForSnodeException, InvalidSNodeException {
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
        if(SNodeBitmap.peek(snodePositionInVirtualDisk/28) == 0)
        {
            throw new InvalidSNodeException("SNode not allocated in virtual disk");
        }

        SNode snode;

        virtualDisk.seek(snodePositionInVirtualDisk);
        FileType type = FileType.parseFileType(virtualDisk.readByte());  //Reads [1]
        byte generation = virtualDisk.readByte();                        //Reads [1]
        long creationDate = virtualDisk.readLong();                      //Reads [8]
        long modificationDate = virtualDisk.readLong();                  //Reads [8]
        short length = virtualDisk.readShort();                          //Reads [2]
        int dataBlockRef = virtualDisk.readUnsignedShort();              //Reads [2]

        int[] dataBlocksInBitmap;

        if (type == FileType.Directory) {
            snode = new SNodeDir();
            virtualDisk.seek(SNodeBitmapPositionInDisk + NumberOfSnodes / 8 + dataBlockRef * 128);
            long position = virtualDisk.getDiskPointer();

            dataBlocksInBitmap = new int[1];
            dataBlocksInBitmap[0] = dataBlockRef;

            while (virtualDisk.getDiskPointer() < position + length) {
                DEntry dEntry = ParseDEntry((int) virtualDisk.getDiskPointer());
                snode.InsertDEntry(dEntry);
            }
        } else {
            snode = new SNodeFile(type, length);
            int nDataBlocks = snode.GetNumberOfDatablocks();
            long position = virtualDisk.getDiskPointer();

            dataBlocksInBitmap = new int[nDataBlocks];


            for (int i = 0; i < nDataBlocks; i++) {

                dataBlocksInBitmap[i] = dataBlockRef;

                virtualDisk.seek(SNodeBitmapPositionInDisk + NumberOfSnodes / 8 + dataBlockRef * 128);
                virtualDisk.readFully(snode.DataBlockByIndex(i));
                virtualDisk.seek(position);
                dataBlockRef = virtualDisk.readUnsignedShort();
                position = virtualDisk.getDiskPointer();
            }
        }
        snode.ChangeCreationDate(creationDate);
        snode.ChangeModificationDate(modificationDate);
        snode.ChangeGeneration(generation);

        int snodeIndexInBitmap = snodePositionInVirtualDisk / 28;

        snode.SetBitmap(snodeIndexInBitmap, dataBlocksInBitmap);

        return snode;
    }

    /**
     * Given a position in the virtualDisk, parses a DEntry object
     * @throws InvalidSNodeException
     */

    public DEntry ParseDEntry(int dentryPositionInVirtualDisk) throws IOException, InvalidEntryException, InvalidLengthForSnodeException, InvalidSNodeException {
        /*
            DEntry Builder
            SNode Identifier:   2 bytes (unsigned)
            EntryLength:        2 bytes (unsigned)
            Filetype:           1 bytes
            FileNameLength      1 bytes (min 1 max 122): n
            FileName            n bytes
        */
        virtualDisk.seek(dentryPositionInVirtualDisk);
        int snodeRef = virtualDisk.readUnsignedShort();
        dentryPositionInVirtualDisk = (int) virtualDisk.getDiskPointer();
        SNode snode = ParseSNode(snodeRef * 28);
        virtualDisk.seek(dentryPositionInVirtualDisk);

        // Mounts DEntry
        int EntryLength = virtualDisk.readUnsignedShort();
        FileType type = FileType.parseFileType(virtualDisk.readByte());
        int filenameLength = virtualDisk.readUnsignedByte();
        byte[] tempBuffer = new byte[filenameLength];
        virtualDisk.readFully(tempBuffer);
        String filename = new String(tempBuffer, StandardCharsets.ISO_8859_1); //Latin 8 bit charset

        DEntry dEntry = new DEntry(snode, type, filename);

        virtualDisk.seek(dentryPositionInVirtualDisk - 2 + EntryLength);   //Vai para o final do DEntry
        return dEntry;
    }

    public int GetNumberOfSnodes()
    {
        return NumberOfSnodes;
    }
    public String GetDatablockBitmap()
    {
        String byteString = "";
        for(byte b : DatablockBitmap.toBits())
        {
            String bitString = "";
            bitString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0");
            for(int i = 7; i >= 0; i--)
            {
                byteString += bitString.charAt(i);
            }
        }
        return byteString;
    }

    public String GetSNodeBitmap()
    {
        String byteString = "";
        for(byte b : SNodeBitmap.toBits())
        {
            String bitString = "";
            bitString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(" ", "0");
            for(int i = 7; i >= 0; i--)
            {
                byteString += bitString.charAt(i);
            }
        }
        return byteString;
    }
}