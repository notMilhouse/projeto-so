package src.domain.snode.dentry;

import src.domain.snode.FileType;
import src.domain.snode.SNode;
import src.domain.snode.dentry.exceptions.InvalidEntryException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * DEntry represents an entry in a directory and contains its business rules
 * */
public class DEntry {
    private final SNode sNode;
    private final FileType fileType;
    private final String fileName;

    private final int length;

    /**
     * Constructs a DEntry from a snode, filetype and a filename
     * @param snode is the snode to which the dentry refers to
     * @param fileType is the type of the node
     * @param fileName is the name of the entry
     * */
    public DEntry(SNode snode, FileType fileType, String fileName )
    throws InvalidEntryException
    {
        this.sNode = snode;
        this.fileType = fileType;

        if(fileName.length() > 122){
            throw new InvalidEntryException("Nome do arquivo maior que o estipulado");
        }

        this.fileName = fileName; 

        this.length = (6 + fileName.length()) + (16 - (6 + fileName.length())%16);
    }

    public SNode getSNode(){
        return this.sNode;
    }

    public String getFileName(){
        return this.fileName;
    }

    public int getLength(){
        return this.length;
    }

    public FileType getFileType()
    {
        return fileType;
    }

    public SNode getSnode()
    {
        return sNode;
    }

    /**
     * Serializes the data structure to an array of bytes that can be easily written to a disk, for example
     *
     * @return byte array
     * */
    public byte[] toBits()
    {
        /*
         *
         * DEntry Builder
            SNode Identifier:   2 bytes (unsigned)
            EntryLength:        2 bytes (unsigned)
            Filetype:           1 bytes
            FileNameLength      1 bytes (min 1 max 122): n
            FileName            n bytes
        *
        * */
        byte[] dentryToBits = new byte[getLength()];
        int index = 0;

        int snodeId = sNode.getIndexInBitmap();

        System.arraycopy(
            ByteBuffer.allocate(2).putShort((short)snodeId).array(), 0,
            dentryToBits, index,
            2
        );

        index += 2;

        System.arraycopy(
            ByteBuffer.allocate(2).putShort((short)length).array(), 0,
            dentryToBits, index,
            2
        );

        index += 2;

        dentryToBits[index++] = fileType.toBits();
        dentryToBits[index++] = (byte)fileName.length(); //? talvez isso precise de um allocate

        byte[] fileNameByteArray = fileName.getBytes(StandardCharsets.ISO_8859_1);

        System.arraycopy(
            fileNameByteArray, 0,
            dentryToBits, index,
            fileName.length()
        );

        return dentryToBits;
    }
}
