package src.domain.bitmap;

public class BitMap {
    private final int[] bitMap;
    private int bitAmount;
    private int chunkAmount;

    public BitMap(int K) {
        bitAmount = K;
        chunkAmount = K / 8;
        bitMap = new int[bitAmount];
    }

    public BitMap(String bits) {
        String[] bitsArray = bits.replace(" ", "").split("");
        bitAmount = bitsArray.length;
        chunkAmount = bitAmount / 8;

        bitMap = new int[bitAmount];

        int index = 0;
        for(String bit : bitsArray) {
            bitMap[index] = Integer.parseInt(bit);
            index++;
        }
    }

    /**
     * retorna o arry de bytes que representa o bitmap
     *
     * @return vetor do BitMap
     */
    public int[] getBitMap() {
        return bitMap;
    }

    public byte[] toBits() {
        byte[] bits = new byte[chunkAmount];

        String[] stringMap = toString().split(" ");

        int index = 0;
        for(String byteString : stringMap) {
            int integerRepresentation = Integer.parseInt(byteString, 2);
            bits[index] = (byte) (integerRepresentation);
            index++;
        }

        return bits;
    }

    @Override
    public String toString() {
        StringBuilder map = new StringBuilder();

        int index = 0;
        for(int bit : bitMap) {
            map.append(bit);
            index++;

            if(index == 8) {
                index = 0;
                map.append(" ");
            }
        }

        return map.toString();
    }

    public void freeSlot(int index) {
        try {
            unsetBitAtPosition(index);
        } catch (BitMapPositionAlreadyUnsetException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int allocateSlot() throws BitMapPositionAlreadySetException, BitMapNextFitNotFoundException {
        try {
            int index = findNextFit();
            setBitAtPosition(index);

            return index;
        } catch (BitMapException ex) {
            throw ex;
        }
    }

    // Find position by next fit
    private int findNextFit() throws BitMapNextFitNotFoundException {
        int index = 0;
        for(int bit : bitMap) {
            if(bit == 0) return index;
            index++;
        }

        throw new BitMapNextFitNotFoundException();
    }

    //next fit
    private void setBitAtPosition(int position) throws BitMapPositionAlreadySetException {
        if (bitMap[position] == 1) {
            throw new BitMapPositionAlreadySetException();
        }

        bitMap[position] = 1;
    }

    private void unsetBitAtPosition(int position) throws BitMapPositionAlreadyUnsetException {
        if (bitMap[position] == 0) {
            throw new BitMapPositionAlreadyUnsetException();
        }

        bitMap[position] = 0;
    }
}
