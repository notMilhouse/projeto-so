package model.SNode;
import java.util.ArrayList;

import management.exceptions.InvalidEntryException;
import management.exceptions.VirtualFileNotFoundException;
import model.DEntry;
import model.FileType;

public class SNodeDir extends SNode {
  
    ArrayList<DEntry> DEntryList;

    int FreeSpace;

    public SNodeDir(){
        super(FileType.Directory, 128); 
        this.FreeSpace = 128; //128 bytes disponíveis para armazenar os DEntrys 
    }

    public boolean insertDEntry(DEntry dEntry)
    throws InvalidEntryException{

        if(dEntry.getSize()> FreeSpace){
            throw new InvalidEntryException("espaço no diretorio insuficiente"); 
        }

        this.DEntryList.add(dEntry); //inserção de um novo DEntry 
        this.FreeSpace+= dEntry.getSize();

        UpdateModificationDate();
        
        
        return true;        
        

    }


    /**
     * Remoção de um DEntry dentro do diretório 
     * @param nameFile
     * @throws VirtualFileNotFoundException
     */
    public void removeDEntry(String nameFile) throws VirtualFileNotFoundException{
       
        //remoção de um DEntry 

        int index = searchDEntryList(nameFile);

        if(index == -1) {
            throw new VirtualFileNotFoundException("arquivo não encontrado");
        }

        DEntryList.remove(index);
    }

    /**
     * busca pelo arquivo dentro do DEntry 
     * @param nameFile
     * @return
     * @throws VirtualFileNotFoundException
     */

    private int searchDEntryList(String nameFile) throws VirtualFileNotFoundException  {
      
        for(DEntry node : DEntryList){
            if(node.getFileName() == nameFile){
                return DEntryList.indexOf(node);
            }
        }

        return -1; 

    }

}
