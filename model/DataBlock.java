package model;

public class DataBlock {
    private static byte[] bitMap;

    private byte[] dataBlockBytes;


    public static void setBitMap(int M) {
        bitMap = new byte[M / 8];
    }

    public DataBlock() {
        dataBlockBytes = new byte[128];
    }
}
