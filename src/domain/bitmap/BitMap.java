package src.domain.bitmap;

import java.util.Arrays;

public class BitMap {
    private final byte[][] bitMap;
    private final int bitAmount;
    private final int chunkAmount;

    private int current_index;

    public BitMap(int K) {
        bitAmount = K;
        chunkAmount = K / 8;
        bitMap = new byte[chunkAmount][8];

        current_index = 0;
    }

    public BitMap(String bits) {
        String[] bitsArray = bits.replace(" ", "").split("");

        bitAmount = bitsArray.length;
        chunkAmount = bitAmount / 8;

        bitMap = new byte[chunkAmount][8];

        int bit = 7;
        int chunk = 0;
        for(String bitInArray : bitsArray) {

            bitMap[chunk][bit] = Byte.parseByte(bitInArray);

            if(bit == 0) {
                bit = 7;
                chunk++;

                continue;
            }

            bit--;
        }

        current_index = 0;
    }

    /**
     * retorna o arry de bytes que representa o bitmap
     *
     * @return vetor do BitMap
     */
    public byte[][] getBitMap() {
        return bitMap;
    }

    public byte[] toBits() {
        byte[] bits = new byte[chunkAmount];

        byte chunk_sum;
        int offset;
        int index = 0;

        for(byte[] chunk : bitMap) {
            chunk_sum = 0;
            offset = 0;

            for (byte bit : chunk) {
                chunk_sum += bit << offset++;
            }

            /*
            * 00000000
            * 00000000
            * 00000000
            * 00000001
            * 00000001
            * 00000001
            * 00000000
            * 00000001
            *
            * 00011101
            *
            *
            * */

            bits[index++] = chunk_sum;
        }

        return bits;
    }

    @Override
    public String toString() {
        StringBuilder map = new StringBuilder();

        int index = 0;
        for(byte bit : toBits()) {

            String byteString = String.format("%8s", Integer.toBinaryString(bit & 0xFF)).replace(" ", "0");
            map.append(byteString);
            map.append(" ");
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
        int index = findNextFit();
        setBitAtPosition(index);

        return index;
    }

    // Find position by next fit
    private int findNextFit() throws BitMapNextFitNotFoundException {
        int index_in_chunk, current_chunk;

        int index = current_index;

        /*
        * 00000000 00000000 00000000
        *
        *
        * */

        do {
            index_in_chunk = index % 8;
            current_chunk = (index / 8) % chunkAmount;

            if(bitMap[current_chunk][index_in_chunk] == 0) {
                current_index = (index + 1) % bitAmount;
                return index;
            }

            index++;
            index %= bitAmount;
        }while(index != current_index);

        throw new BitMapNextFitNotFoundException();
    }

    //next fit
    private void setBitAtPosition(int position) throws BitMapPositionAlreadySetException {
        int index_in_chunk = position % 8;
        int current_chunk = (position / 8) % chunkAmount;

        if (bitMap[current_chunk][index_in_chunk] == 1) {
            throw new BitMapPositionAlreadySetException();
        }

        bitMap[current_chunk][index_in_chunk] = 1;
    }

    private void unsetBitAtPosition(int position) throws BitMapPositionAlreadyUnsetException {

        int index_in_chunk = position % 8;
        int current_chunk = (position / 8) % chunkAmount;

        if (bitMap[current_chunk][index_in_chunk] == 0) {
            throw new BitMapPositionAlreadyUnsetException();
        }

        bitMap[current_chunk][index_in_chunk] = 0;
    }
}
