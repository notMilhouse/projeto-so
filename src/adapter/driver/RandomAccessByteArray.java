package src.adapter.driver;
import java.nio.ByteBuffer;

public class RandomAccessByteArray 
{
    long pos = 0;
    byte[] data;


    public RandomAccessByteArray(byte[] byteArray)
    {
        data = byteArray;
    }

    public byte[] GetByteArray()
    {
        return data;
    }

    public long getFilePointer()
    {
        return pos;
    }

    public void seek(long pos)
    {
        this.pos = pos;
    }
    public void write(byte[] bytes)
    {
        for(int i = 0; i < bytes.length; i++)
        {
            data[(int)pos++] = bytes[i]; 
        }
    }

    //TODO VERIFICAR SE N É AQUI!!
    public void readFully(byte[] bytes)
    {
        for(int i = 0; i < bytes.length; i++)
        {
            bytes[i] = data[(int)pos++]; 
        }
    }
    public byte readByte()
    {
        return data[(int)pos++];
    }

    public int readUnsignedByte()
    {
        return readByte() & 0xFF;
    }

    public int readUnsignedShort()
    {
        byte[] buffer = new byte[2];
        readFully(buffer);
        ByteBuffer wrapper = ByteBuffer.wrap(buffer);
        short ShortValue = wrapper.getShort();
        return Short.toUnsignedInt(ShortValue);
    }

    public short readShort()
    {
        byte[] buffer = new byte[2];
        readFully(buffer);
        ByteBuffer wrapper = ByteBuffer.wrap(buffer);
        return wrapper.getShort();
    }

    public long readLong()
    {
        byte[] buffer = new byte[8];
        readFully(buffer);
        ByteBuffer wrapper = ByteBuffer.wrap(buffer);
        return wrapper.getLong();
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
