package model.SNode;
import java.util.ArrayList;

import model.DEntry;
import model.FileType;

public class SNodeDir extends SNodeBase {
  
    ArrayList<DEntry> DEntryList;

    int FreeSpace;

    SNodeDir(){
        super(FileType.Directory, 128);
        this.FreeSpace = 128; //128 bytes disponíveis para armazenar os DEntrys 
    }


    public boolean insertDEntry(DEntry dEntry){

        if( dEntry.getSize() <  FreeSpace){
            this.DEntryList.add(dEntry); //inserção de um novo DEntry 

            return true;
        }
        else { 
            return false; 
        }

    }

    public boolean removeDEntry(){
        //remoção de um DEntry 
    }


}
