package src.domain.snode;
  //enum dos FileTypes Ok 
public enum FileType {
    Unknown                     (Byte.parseByte("00000000", 2)),
    Regular                     (Byte.parseByte("00000001", 2)),
    Directory                   (Byte.parseByte("00000010", 2)),
    CharacterDevice             (Byte.parseByte("00000011", 2)),
    BlockDevice                 (Byte.parseByte("00000100", 2)),
    Fifo                        (Byte.parseByte("00000101", 2)),
    Socket                      (Byte.parseByte("00000110", 2)),
    SymbolicLink                (Byte.parseByte("00000111", 2));

    private final byte fileType;

    FileType(byte fileType) {
        this.fileType = fileType;
    }

    public byte toByte() {
        return fileType;
    }

    @Override
    public String toString() {
        return this.name();
    }

    public static FileType parseFileType(byte fileType) {
        return switch (fileType) {
            case 1 -> Regular;
            case 2 -> Directory;
            case 3 -> CharacterDevice;
            case 4 -> BlockDevice;
            case 5 -> Fifo;
            case 6 -> Socket;
            case 7 -> SymbolicLink;
            default -> Unknown;
        };
    }
    public static FileType parseFileType(String fileType) {
        return switch (fileType) {
            case "regular" -> Regular;
            case "directory" -> Directory;
            case "character_device" -> CharacterDevice;
            case "block_device" -> BlockDevice;
            case "fifo" -> Fifo;
            case "socket" -> Socket;
            case "symbolic_link" -> SymbolicLink;
            default -> Unknown;
        };
    }
}
