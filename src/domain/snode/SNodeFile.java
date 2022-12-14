package src.domain.snode;

import src.domain.snode.dentry.DEntry;
import src.domain.snode.dentry.exceptions.InvalidEntryException;
import src.domain.snode.exceptions.InvalidLengthForSnodeException;

import java.util.ArrayList;

public class SNodeFile extends SNode {

    
    private final ArrayList <byte[]> DataBlocks;

    public SNodeFile(FileType fileType, int length) throws InvalidLengthForSnodeException {
        super(fileType, length);

        DataBlocks = new ArrayList<byte[]>();

        /*
            Bit shifts 7 to the right, so we can focus on the [128] 8th bit and [256] 9th bit,
            As such the result follows:
            [00] 0: less than 128
            [01] 1: more than 127 and less than 256
            [10] 2: more than 255 and less than 384
            [11] 3: more than 383 and less than 512

            [xxx00]: if any bit higher than the 9th is set we have a overflow or underflow 
        */
        int numberOfDataBlocks = (length - 1) >> 7;

        switch(numberOfDataBlocks){
          
            case 3:
                DataBlocks.add(new byte[128]);
            case 2:
                DataBlocks.add(new byte[128]);
            case 1:
                DataBlocks.add(new byte[128]);
            case 0:
                DataBlocks.add(new byte[128]);
                break;
            default:
                //UNDERFLOW
                if(length == 0)
                {
                    DataBlocks.add(new byte[128]);
                    break;
                }
                //OVERFLOW
                throw new InvalidLengthForSnodeException();
        }
    
    }

    @Override
    public boolean InsertDEntry(DEntry dEntry) throws InvalidEntryException {
        return false; // a file cannot hold an entry
    }

    @Override
    public byte[] DataBlockByIndex(int index)
    {
        return DataBlocks.get(index);
    }
    @Override
    public int GetNumberOfDatablocks()
    {
        return DataBlocks.size();
    }
}
