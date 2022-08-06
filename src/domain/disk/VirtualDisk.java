package src.domain.disk;
import java.nio.ByteBuffer;

/**
 * VirtualDisk is a random access byte array used to represent the file system in memory
 * */
public class VirtualDisk
{
    private long diskPointer = 0;
    private final byte[] data;

    /**
     * Constructs a virtual disk from a byte array
     * */
    public VirtualDisk(byte[] byteArray)
    {
        data = byteArray;
    }

    /**
     * Get data field
     * @return byte array containing the virtual disk data
     * */
    public byte[] GetByteArray()
    {
        return data;
    }

    /**
     * Get diskPointer field
     * @return a long containing the virtual disk pointer current value
     * */
    public long getDiskPointer()
    {
        return diskPointer;
    }

    /**
     * Updates diskPointer to given position
     * */
    public void seek(long position)
    {
        this.diskPointer = position;
    }

    /**
     * Writes bytes from the current position of the diskPointer to the virtual disk
     * */
    public void write(byte[] bytes)
    {
        for (byte aByte : bytes) {
            data[(int) diskPointer++] = aByte;
        }
    }

    /**
     * Copy virtual disk data to a byte array
     * */
    public void readFully(byte[] bytes)
    {
        for(int i = 0; i < bytes.length; i++)
        {
            bytes[i] = data[(int) diskPointer++];
        }
    }

    /**
     * Reads a byte from the virtual disk and updates the disk pointer position
     * @return a byte
     * */
    public byte readByte()
    {
        return data[(int) diskPointer++];
    }

    /**
     * Reads a byte from the virtual disk and updates the disk pointer position as an unsigned byte
     * @return a byte
     * */
    public int readUnsignedByte()
    {
        return readByte() & 0xFF;
    }

    /**
     * Reads two bytes from the virtual disk and updates the disk pointer position as an unsigned short
     * @return an int value representing a short value
     * */
    public int readUnsignedShort()
    {
        byte[] buffer = new byte[2];
        readFully(buffer);
        ByteBuffer wrapper = ByteBuffer.wrap(buffer);
        short ShortValue = wrapper.getShort();
        return Short.toUnsignedInt(ShortValue);
    }

    /**
     * Reads two bytes from the virtual disk and updates the disk pointer position as a short
     * @return a short value
     * */
    public short readShort()
    {
        byte[] buffer = new byte[2];
        readFully(buffer);
        ByteBuffer wrapper = ByteBuffer.wrap(buffer);
        return wrapper.getShort();
    }

    /**
     * Reads eight bytes from the virtual disk and updates the disk pointer position as a long
     * @return a long value
     * */
    public long readLong()
    {
        byte[] buffer = new byte[8];
        readFully(buffer);
        ByteBuffer wrapper = ByteBuffer.wrap(buffer);
        return wrapper.getLong();
    }
}
