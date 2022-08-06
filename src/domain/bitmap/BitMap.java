package src.domain.bitmap;


import src.domain.bitmap.exceptions.BitMapNextFitNotFoundException;
import src.domain.bitmap.exceptions.BitMapPositionAlreadySetException;
import src.domain.bitmap.exceptions.BitMapPositionAlreadyUnsetException;

/**
 * BitMap is a 2-dimensional array representing a map of bits
 * */
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

    /**
     * Constructs a bitmap from a string representing a bitmap
     * @param bits a string representing a bitmap
     * */
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
     * Get the bitmap array that represents the bitmap data
     *
     * @return a bitmap
     */
    public byte[][] getBitMap() {
        return bitMap;
    }

    /**
     * Serializes the data structure to an array of bytes that can be easily written to a disk, for example
     *
     * @return byte array
     * */
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

    /**
     * freeSlot() provides a way of unsetting a slot in the bitmap given an index
     * @param index an index in the bitmap
     * */
    public void freeSlot(int index) {
        try {
            unsetBitAtPosition(index);
        } catch (BitMapPositionAlreadyUnsetException ex) {
            System.out.println(ex.getMessage());
        }
    }


    /**
     * allocateSlot() provides a way of setting a slot in the bitmap given an index
     * @return an index in the bitmap that has been set
     * */
    public int allocateSlot() throws BitMapPositionAlreadySetException, BitMapNextFitNotFoundException {
        int index = findNextFit();
        setBitAtPosition(index);

        return index;
    }

    /**
     * findNextFit() implements the next fit rule for finding an available position in the bitmap
     * @return an index for an available position in the bitmap
     * @throws BitMapNextFitNotFoundException if there is no position available
     * */
    private int findNextFit() throws BitMapNextFitNotFoundException {
        int index_in_chunk, current_chunk;

        int index = current_index;

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

    /**
     * sets (define as 1) a bitmap position
     *
     * @throws BitMapPositionAlreadySetException if the position is already set
     * */
    private void setBitAtPosition(int position) throws BitMapPositionAlreadySetException {
        int index_in_chunk = position % 8;
        int current_chunk = (position / 8) % chunkAmount;

        if (bitMap[current_chunk][index_in_chunk] == 1) {
            throw new BitMapPositionAlreadySetException();
        }

        bitMap[current_chunk][index_in_chunk] = 1;
    }

    /**
     * unsets (define as 0) a bitmap position
     *
     * @throws BitMapPositionAlreadyUnsetException if the position is already unset
     * */
    private void unsetBitAtPosition(int position) throws BitMapPositionAlreadyUnsetException {

        int index_in_chunk = position % 8;
        int current_chunk = (position / 8) % chunkAmount;

        if (bitMap[current_chunk][index_in_chunk] == 0) {
            throw new BitMapPositionAlreadyUnsetException();
        }

        bitMap[current_chunk][index_in_chunk] = 0;
    }
}
