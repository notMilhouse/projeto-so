package model;

import model.SNode.SNodeDir;
import model.SNode.SNodeFile;

public class DEntry {
    private SNodeDir sNode;
    private short entryLength;
    private FileType fileType;
    private String fileName;

    private int size; 

    DEntry( SNodeDir snode, short entryLength, FileType fileType, String fileName ) {
        this.sNode = snode;
        this.entryLength = entryLength;
        this.fileType = fileType;
    

        if(fileName.length()< 122){
            this.fileName = fileName;
        }else { 
            //TODO pensar ainda na excessão do problema 
        }


        this.size = 6 + fileName.length(); //size será o tamanho da estrutura DEntry 
        //pensando que a soma das estruturas deve ser menor que 128 

    }

    public int getSize(){
        return this.size;
    }

    


}
