package src.adapter.driver;

public class RandomAccessByteArray 
{
    long pos = 0;
    


    public RandomAccessByteArray(byte[] byteArray)
    {

    }


    public void seek(long pos)
    {

    }
    public void write(byte[] bytes)
    {

    }
    public void readFully(byte[] bytes)
    {

    }
    public byte readByte()
    {
        return null;
    }
    public int readUnsignedShort()
    {
        return 2;
    }
    public short readShort()
    {
        return 2;
    }
    public long readLong()
    {
        return 2;
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
