package src.domain.snode;

import src.adapter.driver.exceptions.VirtualFileNotFoundException;
import src.domain.snode.dentry.DEntry;
import src.domain.snode.dentry.exceptions.InvalidEntryException;

import java.util.ArrayList;


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

        if(dEntry.getLength() > FreeSpace){
            throw new InvalidEntryException("invalid DEntry length");
        }

        this.DEntryList.add(dEntry);

        this.FreeSpace -= dEntry.getLength();
        this.length = 128 - this.FreeSpace;
        
        UpdateModificationDate();
        return true;      
    }

    @Override
    public byte[] DataBlockByIndex(int index) {
        return null;
    }


    /**
     * Removes DEntry from directory
     * @param index of the dentry
     * @throws VirtualFileNotFoundException when virtual file not found
     */
    public void removeDEntry(int index) throws VirtualFileNotFoundException{

        if(index < 0 || index > DEntryList.size()) {
            throw new VirtualFileNotFoundException();
        }

        this.FreeSpace += DEntryList.get(index).getLength();
        this.length = 128 - this.FreeSpace;

        DEntryList.remove(index);
    }

    /**
    *  Searches the directory for any underlying entries
    * @param fileName is the name of the file being searched
    * @return the entry for the file being searched
    */
    public DEntry searchInDirectory(String fileName) throws VirtualFileNotFoundException{
    
        for (DEntry dentry : DEntryList) {
            if(dentry.getFileName().equals(fileName)){
                return dentry;
            }
        }

        throw new VirtualFileNotFoundException();
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
