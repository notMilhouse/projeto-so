package src.domain.snode;

//import java.sql.Time;

import src.domain.snode.dentry.DEntry;
import src.domain.snode.dentry.exceptions.InvalidEntryException;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * SNode is a snode in the file system
 * */
public abstract class SNode {

    protected FileType fileType;
    protected byte generation;
    protected ZonedDateTime creationDate;
    protected ZonedDateTime modificationDate;
    protected int length;
    protected int indexInBitMap;
    protected int[] datablocksReferences;

    /**
     * Constructs a new SNode given its filetype and length
     */
    public SNode(FileType filetype, int length) {
        this.length = length;
        this.generation = 0;
        this.creationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()); // tempo de criação do SNode
        this.modificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        this.fileType = filetype;
    }

    /**
     * updates a snode modification date
     * */
    protected void UpdateModificationDate() {
        this.modificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
    }

    /**
     * Defines the snode bitmap current pointer and its associated data blocks references
     * */
    public void SetBitmap(int snodeIndex, int[] dataBlocksIndexes) {
        indexInBitMap = snodeIndex;
        datablocksReferences = dataBlocksIndexes;
    }

    /**
     * @return the snode file type object
     */
    public FileType GetFileType() {
        return fileType;
    }


    /**
     * Change the creation date of the snode when it is created
     * @param time the time of the creation of the snode
     */
    public void ChangeCreationDate(long time) {

        creationDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    /**
     * Change the modification date of the snode
     * @param time the time of the modification of the snode
     */
    public void ChangeModificationDate(long time) {
        modificationDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    public void ChangeGeneration(Byte generation) {
        this.generation = generation;
    }

    public int getIndexInBitmap() {
        return indexInBitMap;
    }

    public int[] getDatablocksReferences() {
        return datablocksReferences;
    }

    public byte getGeneration()
    {
        return generation;
    }

    public String getCreationDate()
    {
        return creationDate.toString();
    }

    public String getModificationDate()
    {
        return modificationDate.toString();
    }

    public int getLength()
    {
        return length;
    }

    public abstract boolean InsertDEntry(DEntry dEntry)
        throws InvalidEntryException;

    public abstract byte[] DataBlockByIndex(int index);

    public int GetNumberOfDatablocks() {
        return datablocksReferences.length;
    }

    /**
     * Serializes the data structure to an array of bytes that can be easily written to a disk, for example
     *
     * @return byte array
     * */
    public byte[] toBits() {
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

        byte[] snodeToBits = new byte[28];
        int index = 0;

        snodeToBits[index++] = fileType.toBits();
        snodeToBits[index++] = generation;

        byte[] creationDateAsByteArray = ByteBuffer
                .allocate(8)
                .putLong(creationDate.toInstant().toEpochMilli())
                .array();

        System.arraycopy(
            creationDateAsByteArray, 0,
            snodeToBits, index,
            8
        );

        index += 8;

        byte[] modificationDateAsByteArray = ByteBuffer
            .allocate(8)
            .putLong(modificationDate.toInstant().toEpochMilli())
            .array();

        System.arraycopy(
            modificationDateAsByteArray, 0,
            snodeToBits, index,
            8
        );

        index += 8;

        byte[] lengthAsByteArray = ByteBuffer
            .allocate(2)
            .putShort((short)length)
            .array();

        System.arraycopy(
            lengthAsByteArray, 0,
            snodeToBits, index,
            2
        );

        index += 2;
        for(int dataBlockReference : datablocksReferences) {
            System.arraycopy(
                ByteBuffer.allocate(2).putShort((short)dataBlockReference).array(), 0,
                snodeToBits, index,
                2
            );
            index += 2;
        }
        //TODO quando ele passar de 32767

        return snodeToBits;
    }
}
