package parse;

import management.exceptions.*;
import model.*;
import model.SNode.*;
import java.io.*;
import java.nio.ByteBuffer;

public class Parser
{
    File disk;
    RandomAccessFile diskAccess;
    //FileInputStream diskIn;
    //FileOutputStream diskOut;
    SNode root;

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
            diskAccess = new RandomAccessFile(disk, "r");
            root = ParseSNode(0);
            diskAccess.close();
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
            diskAccess = new RandomAccessFile(disk, "w");
            //Persiste no arquivo
            diskAccess.close();
        }
        catch(Exception err)
        {
            System.err.println(err);
        }
    }

    /**
    *
    */
    public SNode ParseSNode(int atRef)
    {
        /*
            Snode builder
            Type: 1 byte
            generation:     1 byte
            creationDate    8 bytes
            creationDate    8 bytes
            length          2 bytes
            DataBlockRef1   2 bytes (unsigned)
            DataBlockRef2   2 bytes (unsigned)
            DataBlockRef3   2 bytes (unsigned)
            DataBlockRef4   2 bytes (unsigned)
        */
        SNode snode;
        try
        {
            diskAccess.seek(atRef);
            FileType type = FileType.parseFileType(diskAccess.readByte());
            byte generation = diskAccess.readByte();
            long creationDate = diskAccess.readLong();
            long modificationDate = diskAccess.readLong();
            short length = diskAccess.readShort();
            if(type == FileType.Directory)
            {
                //Instanciar DEntry
                snode = new SNodeDir();
                long position = diskAccess.getFilePointer();
                //tenho q arrumar isso daqui, está errado
                while(diskAccess.getFilePointer() < position + length) //TODO Ver se esse while faz sentido
                {
                    DEntry dEntry = ParseDir(diskAccess.readUnsignedShort());
                    snode.InsertDEntry(dEntry); //Erro de intellisense
                }
            }
            else
            {

                snode = new SNodeFile(type, length);
            }
        }
        catch(Exception err)
        {
            System.err.println(err);
            return null;
        }

        //Montar SNode
        
        /*
        generation = 0;
        creationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli(); //tempo de criação do SNode  
        modificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        */
        return snode;    
    }

    public DEntry ParseDir(int atRef) throws IOException, InvalidEntryException
    {
        /*
            DEntry Builder
            SNode Identifier:   2 bytes (unsigned)
            EntryLength:        2 bytes (unsigned)
            Filetype:           1 bytes
            FileNameLength      1 bytes (min 1 max 122): n
            FileName            n bytes
        */

        //Da pra simplificar isso daqui..
        diskAccess.seek(atRef);
        int snodeRef = diskAccess.readUnsignedShort();
        atRef = (int) diskAccess.getFilePointer();
        SNode snode = ParseSNode(snodeRef);
        diskAccess.seek(atRef);

        //Montar DEntry
        int EntryLength = diskAccess.readUnsignedShort();
        FileType type = FileType.parseFileType(diskAccess.readByte());
        int filenameLength = diskAccess.readUnsignedByte();
        byte[] tempBuffer = new byte[filenameLength];
        diskAccess.readFully(tempBuffer);
        String filename = new String(tempBuffer, "ISO-8859-1"); //Latin 8 bit charset

        DEntry dEntry = new DEntry(snode, EntryLength, type, filename);

        diskAccess.seek(atRef - 2 + EntryLength);   //Vai para o final do DEntry

        return dEntry;
    }
    /*
    public byte[] ReadBytes(int numberOfBytes)
    throws IOException
    {
        byte[] byteArray = new byte[numberOfBytes];
        diskAccess.read(byteArray, 0, numberOfBytes);
        return byteArray;
    }

    public long ByteArrayToLong(byte[] byteArray)
    {
        ByteBuffer wrapper = ByteBuffer.wrap(byteArray);
        return wrapper.getLong();
    }
    public int ByteArrayToInt(byte[] byteArray)
    {
        ByteBuffer wrapper = ByteBuffer.wrap(byteArray);
        return wrapper.getInt();
    }
    public int ByteArrayToUnsignedShort(byte[] byteArray)
    {
        ByteBuffer wrapper = ByteBuffer.wrap(byteArray);
        short ShortValue = wrapper.getShort();
        return ShortValue >= 0? ShortValue : 0x10000 + ShortValue;
    }
    public short ByteArrayToShort(byte[] byteArray)
    {
        ByteBuffer wrapper = ByteBuffer.wrap(byteArray);
        return wrapper.getShort();
    }
    */
}