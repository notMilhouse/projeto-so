package model;

public enum FileType {
    Unknown(new Byte("0b00000000")),
    Regular(new Byte("0b00000001")),
    Directory(new Byte("0b00000010")),
    CharacterDevice(new Byte("0b00000011")),
    BlockDevice(new Byte("0b00000100")),
    Fifo(new Byte("0b00000101")),
    Socket(new Byte("0b00000110")),
    SymbolicLink(new Byte("0b00000111"));

    private final Byte fileType;
    private FileType(Byte fileType) {
        this.fileType = fileType;
    }

    public Byte toByte() {
        return fileType;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public static FileType parseFileType(byte fileType) {
        switch (fileType) {
            case 0b00000001:
                return Regular;
            case 0b00000010:
                return Directory;
            case 0b00000011:
                return CharacterDevice;
            case 0b00000100:
                return BlockDevice;
            case 0b00000101:
                return Fifo;
            case 0b00000110:
                return Socket;
            case 0b00000111:
                return SymbolicLink;
            default:
                return Unknown;
        }
    }

    public static FileType parseFileType(String fileType) {
        switch (fileType) {
            case "regular":
                return Regular;
            case "directory":
                return Directory;
            case 0b00000011:
                return CharacterDevice;
            case 0b00000100:
                return BlockDevice;
            case 0b00000101:
                return Fifo;
            case 0b00000110:
                return Socket;
            case 0b00000111:
                return SymbolicLink;
            default:
                return Unknown;
        }
    }
}
