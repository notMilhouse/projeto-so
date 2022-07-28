package parse;

import model.*;
import model.SNode.*;
import java.io.*;
import java.nio.ByteBuffer;

public class Parser
{
    File disk;
    FileInputStream diskIn;
    FileOutputStream diskOut;
    SNodeBase root;

    int NumberOfSnodes;
    int NumberOfDatablocks;

    Parser(File file, int numberOfSnodes, int numberOfDatablocks)
    {
        disk = file;
        NumberOfSnodes = numberOfSnodes;
        NumberOfDatablocks = numberOfDatablocks;
    }

    public void Read()
    {
        try
        {
            diskIn = new FileInputStream(disk);
            root = ParseSNode(0);
            diskIn.close();
        }
        catch(Exception err)
        {
            System.err.println(err);
        }
        
    }

    public void Write()
    {
        try
        {
            diskOut = new FileOutputStream(disk);
            //Persiste no arquivo
            diskOut.close();
        }
        catch(Exception err)
        {
            System.err.println(err);
        }
    }

    /**
    *
    */
    public SNodeBase ParseSNode(int atRef)
    {
        ByteBuffer wrapper;
        try
        {
            int type = wrapper.wrap(ReadBytes(2)).getInt();
        }
        catch(Exception err)
        {
            System.err.println(err);
        }

        //Montar SNode
        
        /*
        generation = 0;
        creationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli(); //tempo de criação do SNode  
        modificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        */


        return SNodeBase snode;    
    }
    public DEntry ParseDir(int atRef)
    {

        SNode node = ParseSNode(ref);
        //Montar DEntry
        return DEntry;
    }

    public byte[] ReadBytes(int numberOfBytes)
    throws IOException
    {
        byte[] byteArray = new byte[numberOfBytes];
        diskIn.read(byteArray, 0, numberOfBytes);
        return byteArray;
    }

    public int ByteArrayToInt(byte[] byteArray)
    {
        return 0;
    }
    public short ByteArrayToShort(byte[] byteArray)
    {
        return 0;
    }
}