package src.domain.snode;

//import java.sql.Time;

import src.application.management.exceptions.InvalidEntryException;
import src.domain.bitmap.BitMap;
import src.domain.snode.dentry.DEntry;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public abstract class SNode {
    protected static BitMap bitMap; // BitMap é uma estrutura a parte do SNode

    protected FileType fileType;
    protected byte generation;
    protected ZonedDateTime creationDate;
    protected ZonedDateTime modificationDate;
    protected int length;
    protected int indexInBitMap;
    protected int[] datablocksReferences;

    /**
     * Criando Novo SNode
     */
    public SNode(FileType filetype, int length) {
        this.length = length;
        this.generation = 0;
        this.creationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()); // tempo de criação do SNode
        this.modificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        this.fileType = filetype;
    }

    public void UpdateModificationDate() {
        this.modificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
    }

    public void SetBitmap(int snodeIndex, int[] dataBlocksIndexes) {
        indexInBitMap = snodeIndex;
        datablocksReferences = dataBlocksIndexes;
    }

    public FileType GetFileType() {
        return fileType;
    }

    /**
     * Carregando SNode
     */
    public void ChangeCreationDate(long time) {

        creationDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

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

    //
    public boolean InsertDEntry(DEntry dEntry)
        throws InvalidEntryException {
        return false;
    }

    public byte[] DataBlockByIndex(int index) {
        return null;
    }

    public int GetNumberOfDatablocks() {
        return datablocksReferences.length;
    }

    //
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

        snodeToBits[index++] = fileType.toByte();
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

        index = 10;

        byte[] modificationDateAsByteArray = ByteBuffer
            .allocate(8)
            .putLong(modificationDate.toInstant().toEpochMilli())
            .array();

        System.arraycopy(
            modificationDateAsByteArray, 0,
            snodeToBits, index,
            8
        );

        index = 18;

        byte[] lengthAsByteArray = ByteBuffer
            .allocate(2)
            .putShort((short)length)
            .array();

        System.arraycopy(
            lengthAsByteArray, 0,
            snodeToBits, index,
            2
        );

        index = 20;

        for(int dataBlockReference : datablocksReferences) {
            System.arraycopy(
                ByteBuffer.allocate(4).putInt(dataBlockReference).array(), 0,
                snodeToBits, index,
                2
            );

            index += 2;
        }

        return snodeToBits;
    }
}
