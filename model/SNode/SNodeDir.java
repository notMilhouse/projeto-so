package model.SNode;
import java.util.ArrayList;

import management.exceptions.InvalidEntryException;
import model.DEntry;
import model.FileType;

public class SNodeDir extends SNodeBase {
  
    ArrayList<DEntry> DEntryList;

    int FreeSpace;

    SNodeDir(){
        super(FileType.Directory, 128);
        this.FreeSpace = 128; //128 bytes disponíveis para armazenar os DEntrys 
    }

    public boolean insertDEntry(DEntry dEntry)
    throws InvalidEntryException{

        if(dEntry.getSize()> FreeSpace){
            throw new InvalidEntryException("espaço no diretorio insuficiente"); 
        }

        this.DEntryList.add(dEntry); //inserção de um novo DEntry 
        return true;        
        
    }

    public boolean removeDEntry(){
        return false;
        //remoção de um DEntry 

        //realizar uma busca no arrylist pelo dEntry que quer remover, 
        


    }


}
