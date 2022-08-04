package src.domain.snode.dentry;

import src.application.management.exceptions.InvalidEntryException;
import src.domain.snode.FileType;
import src.domain.snode.SNode;

public class DEntry {
    private final SNode sNode; //isso pode ser tanto um snode de diretorio como de arquivo
    private final int entryLength;
    private final FileType fileType;
    private final String fileName;

    private final int size;

    public DEntry( SNode snode, int entryLength, FileType fileType, String fileName ) 
    throws InvalidEntryException
    {
        this.sNode = snode;
        this.entryLength = entryLength;
        this.fileType = fileType;
    

        if(fileName.length()> 122){
            throw new InvalidEntryException("Nome do arquivo maior que o estipulado");
           //tratar a excesão 
        }
                

        this.fileName = fileName; 
        //TODO O size tem que arredondar para proxima potencia de 2

    
        this.size = 6 + fileName.length(); //size será o tamanho da estrutura DEntry. O tamanho máximo do DEntry deve ser menor que 128 bytes 
        


    }

    public String getFileName(){
        return this.fileName;
    }

    public int getSize(){
        return this.size;
    }

    public FileType getFileType()
    {
        return fileType;
    }

    public SNode getSnode()
    {
        return sNode;
    }

    


}
