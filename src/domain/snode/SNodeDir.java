package src.domain.snode;
import java.util.ArrayList;

import src.application.management.exceptions.InvalidEntryException;
import src.application.management.exceptions.VirtualFileNotFoundException;
import src.domain.snode.dentry.DEntry;

public class SNodeDir extends SNode {
  
    ArrayList<DEntry> DEntryList;

    int FreeSpace;

    public SNodeDir(){
        super(FileType.Directory, 128); 
        this.FreeSpace = 128; //128 bytes disponíveis para armazenar os DEntrys
        this.DEntryList = new ArrayList<DEntry>();
    }

    @Override
    public boolean InsertDEntry(DEntry dEntry)
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

    public void removeDEntry(int index) throws VirtualFileNotFoundException{
       
        //remoção de um DEntry 


        if(index < 0 || index > DEntryList.size()) {
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

    public DEntry getDEntryAtIndex(int index)
    {
        return DEntryList.get(index);
    }
    public int numberOfFilesInDir()
    {
        return DEntryList.size();
    }

}
