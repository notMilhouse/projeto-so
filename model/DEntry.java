package model;

import management.exceptions.InvalidEntryException;
import model.SNode.SNodeDir;
import model.SNode.SNodeFile;

public class DEntry {
    private SNodeDir sNode;
    private short entryLength;
    private FileType fileType;
    private String fileName;

    private int size; 

    DEntry( SNodeDir snode, short entryLength, FileType fileType, String fileName ) 
    throws InvalidEntryException
    {
        this.sNode = snode;
        this.entryLength = entryLength;
        this.fileType = fileType;
    

        if(fileName.length()> 122){
            throw new InvalidEntryException("Nome do arquivo maior que o estipulado");
            //TODO ver se InvalidEntryException retorna ou não 
        }
                

        this.fileName = fileName; 
        this.size = 6 + fileName.length(); //size será o tamanho da estrutura DEntry. O tamanho máximo do DEntry deve ser menor que 128 bytes 
        


    }

    public int getSize(){
        return this.size;
    }

    


}
