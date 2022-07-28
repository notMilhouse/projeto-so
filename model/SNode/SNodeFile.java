package model.SNode;

import java.util.ArrayList;

import model.FileType;

public class SNodeFile extends SNode {

    
    private ArrayList <byte[]> DataBlocks;

    public SNodeFile(FileType fileType, int length){
        super(fileType, length);

        DataBlocks = new ArrayList<byte[]>();

        /*
            Bit shifts 7 to the right, so we can focus on the [128] 8th bit and [256] 9th,
            As such the result follows:
            0: less than 128
            1: more than 127 and less than 256
            2: more than 255 and less than 384
            3: more than 383 and less than 512
        */
        int nOfDataBlocks = length >> 7; // TODO Ver se vamos precisar subtrair -1 do length antes de fazer o bitshift

        switch(nOfDataBlocks){
          
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
                //OVERFLOW
                break;
        }
    
    }

    


}
