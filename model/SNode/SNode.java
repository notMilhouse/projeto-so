package model.SNode;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import model.BitMap;
import model.FileType;

public abstract class SNode {
    protected static BitMap bitMap; // BitMap é uma estrutura a parte do SNode

    protected FileType fileType;
    protected Byte Generation;
    protected ZonedDateTime CreationDate; // pra vermos isso depois
    protected ZonedDateTime ModificationDate;
    protected int Length;

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



    

}
