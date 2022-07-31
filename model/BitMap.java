package model;

public class BitMap {
    private byte[] bitMap;

    BitMap(int M) {
        this.bitMap = new byte[M/8];
       
        for (int b : bitMap) {  //inicialização com todos espaços disponíveis 
            this.bitMap[b] = (byte)0;
        }

    
    }

    BitMap(byte[] bitMap){
        this.bitMap = bitMap;   //pegando o bitmap da memória
    }
    
    
    //next fit
    public boolean insertNode(int position) {


      

        int slot = (int) position/8  ;
        int positionInSlot = Math.abs(position - 8 * slot);

        if( bitMap[slot][positionInSlot] == 1)
            return false;  
        else {
            bitMap[slot][positionInSlot] = 1;
            return true;
        }
    }


    public boolean removeNode(int position){   
        int slot = (int) position/8  ;
        int positionInSlot = Math.abs(position - 8 * slot);

        if( bitMap[slot][positionInSlot] == 0)
            return false;  
        else {
            bitMap[slot][positionInSlot] = 0;
            return true;
        }
    }



    /**
     * retorna o arry de bytes que representa o bitmap
     * @return vetor do BitMap 
     */
    public byte[] getBitMap(){
        return bitMap;
    }



    public MapSlot findSlot() {
        String map = bitMap.toString();
    }


}
