package model;

public class BitMap {
    private byte[][] bitMap;

    BitMap(int M) {
        this.bitMap[][] = {{0}};
    }

/** 
 * M/8
    (byte)10100010 011100011 10001001 
     [0][1]=1
     [0][2]=0
     [0][3]=1
       . 
       .
       .
     [0][8]=0
     [1][0]=0 
     [1][8] 
*/

    public boolean searchFreeSpaceInVector(){
    }

    public boolean insertNode(int position){
    }


    public boolean removeNode(int position){   
    }

    
    private int validatePositionInVector(int position){    
    }
    

    private byte[] convertBitMapInArrayOfBytes(){

    }





    /**
     * retorna o arry de bytes que representa o bitmap
     * @return vetor do BitMap 
     */
    public byte[][] getBitMap(){
        return bitMap;
    }



}
