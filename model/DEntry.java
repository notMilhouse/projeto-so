package model;

import management.exceptions.InvalidEntryException;
import model.SNode.SNode;
import model.SNode.SNodeDir;
import model.SNode.SNodeFile;

public class DEntry {
    private SNode sNode; //isso pode ser tanto um snode de diretorio como de arquivo 
    private int entryLength;
    private FileType fileType;
    private String fileName;

    private int size; 




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

    


}
