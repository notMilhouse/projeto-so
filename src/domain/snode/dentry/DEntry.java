package src.domain.snode.dentry;

import src.application.management.exceptions.InvalidEntryException;
import src.domain.snode.FileType;
import src.domain.snode.SNode;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DEntry {
    private final SNode sNode; //isso pode ser tanto um snode de diretorio como de arquivo
    private final FileType fileType;
    private final String fileName;

    private final int length;

    public DEntry( SNode snode, FileType fileType, String fileName ) 
    throws InvalidEntryException
    {
        this.sNode = snode;
        this.fileType = fileType;

        if(fileName.length()> 122){
            throw new InvalidEntryException("Nome do arquivo maior que o estipulado");
           //tratar a excesão 
        }

        this.fileName = fileName; 

        this.length = (6 + fileName.length()) + (6 + fileName.length())%16;
        
        //size será o tamanho da estrutura DEntry. O tamanho máximo do DEntry deve ser menor que 128 bytes
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
            ByteBuffer.allocate(4).putInt(snodeId).array(), 0,
            dentryToBits, index,
            2
        );

        index += 2;

        System.arraycopy(
            ByteBuffer.allocate(4).putInt(length).array(), 0,
            dentryToBits, index,
            2
        );

        index += 2;

        dentryToBits[index++] = fileType.toByte();
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
