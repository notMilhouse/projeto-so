package src.domain.snode;
import java.util.ArrayList;

import src.application.management.exceptions.InvalidEntryException;
import src.application.management.exceptions.VirtualFileNotFoundException;
import src.domain.snode.dentry.DEntry;


public class SNodeDir extends SNode {
  
    private ArrayList<DEntry> DEntryList;
    private int FreeSpace;

    public SNodeDir(){
        super(FileType.Directory, 0); 
        this.FreeSpace = 128; //128 bytes disponíveis para armazenar os DEntrys
        this.DEntryList = new ArrayList<DEntry>();
    }

    @Override
    public boolean InsertDEntry(DEntry dEntry)
    throws InvalidEntryException{

        if(dEntry.getLength()> FreeSpace){
            throw new InvalidEntryException("espaço no diretorio insuficiente");
        }

        this.DEntryList.add(dEntry); //inserção de um novo DEntry 

        this.FreeSpace -= dEntry.getLength();
        this.length = 128 - this.FreeSpace;
        
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

        this.FreeSpace += DEntryList.get(index).getLength();
        this.length = 128 - this.FreeSpace;

        DEntryList.remove(index);
    }

    public void removeDEntry(int index) throws VirtualFileNotFoundException{
       
        //remoção de um DEntry 


        if(index < 0 || index > DEntryList.size()) {
            throw new VirtualFileNotFoundException("arquivo não encontrado");
        }

        this.FreeSpace += DEntryList.get(index).getLength();
        this.length = 128 - this.FreeSpace;

        DEntryList.remove(index);
    }


    /**
     * busca pelo arquivo dentro do DEntry 
     * @param nameFile
     * @return
     * @throws VirtualFileNotFoundException
     * 
     */
    private int searchDEntryList(String nameFile) throws VirtualFileNotFoundException  {
      
        for(DEntry node : DEntryList){
            if(node.getFileName() == nameFile){
                return DEntryList.indexOf(node);
            }
        }
        return -1; 
    }

    /**
    *   Busca no diretorio por subdiretorios 
    * @param fileName
    * @return
    */
    public DEntry searchInDirectory(String fileName) throws VirtualFileNotFoundException{//TODO tratar excessão 
    
        for (DEntry dentry : DEntryList) {
            if(dentry.getFileName() == fileName){
                return dentry;
            }
        }

        throw new VirtualFileNotFoundException("diretório inexistente");



    }


    public DEntry getDEntryAtIndex(int index)
    {
        return DEntryList.get(index);
    }
    public int numberOfFilesInDir()
    {
        return DEntryList.size();
    }

    @Override
    public int GetNumberOfDatablocks()
    {
        return 1;
    }


}
