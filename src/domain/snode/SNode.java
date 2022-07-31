package src.domain.snode;

//import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


import src.application.management.exceptions.InvalidEntryException;
import src.domain.snode.dentry.DEntry;

import src.domain.bitmap.BitMap;

public abstract class SNode {
    protected static BitMap bitMap; // BitMap é uma estrutura a parte do SNode

    protected FileType fileType;
    protected Byte Generation;
    protected ZonedDateTime CreationDate; // pra vermos isso depois
    protected ZonedDateTime ModificationDate;
    protected int Length;
    protected int indexInBitMap; 
    protected int[] datablocksInBitmap;    
    


    /**
     * 
     * Criando Novo SNode
     */
    public SNode(FileType directory, int length) {

        this.Length = length;
        this.Generation = 0;
        this.CreationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()); // tempo de criação do SNode
        this.ModificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());

    }

    public void UpdateModificationDate(){
        this.ModificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
    }



    public void SetBitmap(int snodeIndex, int[] dataBlocksIndexes)
    {
        indexInBitMap = snodeIndex;
        datablocksInBitmap = dataBlocksIndexes;
    }
    

    
    
    
    /**
     * Carregando SNode
     */
    public void ChangeCreationDate(long time) {

        CreationDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }
    public void ChangeModificationDate(long time) {
        ModificationDate = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }
    public void ChangeGeneration(Byte generation) {
        Generation = generation;
    }



    //
    public boolean InsertDEntry(DEntry dEntry)
    throws InvalidEntryException{
        return false;
    }
    public byte[] DataBlockByIndex(int index)
    {
        return null;
    }

    public int GetNumberOfDatablocks()
    {
        return -1;
    }
    //

    public static void setBitMap(int numFiles) {
    }


}
