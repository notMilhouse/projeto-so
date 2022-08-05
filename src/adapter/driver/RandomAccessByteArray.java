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

}
