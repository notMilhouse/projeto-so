package model.SNode;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import model.BitMap;
import model.FileType;


public abstract class SNodeBase {
    private static BitMap bitMap; //BitMap é uma estrutura a parte do SNode

    private FileType FileType;
    private Byte Generation;
    private ZonedDateTime CreationDate; //pra vermos isso depois 
    private ZonedDateTime ModificationDate;
    private int Length;

    /** Criando Novo SNode
    */
    public SNodeBase(FileType directory, int length)
    {

        this.Length = length;
        this.Generation = 0;
        this.CreationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()); //tempo de criação do SNode  
        this.ModificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());

    }

    /** Carregando SNode
    */
    public SNodeBase()
    {
        
    }


}
