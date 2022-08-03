package src.domain.snode;
  //enum dos FileTypes Ok 
public enum FileType {
    Unknown                     ((byte)0),
    Regular                     ((byte)1),
    Directory                   ((byte)2),
    CharacterDevice             ((byte)3),
    BlockDevice                 ((byte)4),
    Fifo                        ((byte)5),
    Socket                      ((byte)6),
    SymbolicLink                ((byte)7);

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
        switch (fileType) {
            case ((byte)1):
                return Regular;
            case ((byte)2):
                return Directory;
            case ((byte)3):
                return CharacterDevice;
            case ((byte)4):
                return BlockDevice;
            case ((byte)5):
                return Fifo;
            case ((byte)6):
                return Socket;
            case ((byte)7):
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
            case "character_device":
                return CharacterDevice;
            case "block_device":
                return BlockDevice;
            case "fifo":
                return Fifo;
            case "socket":
                return Socket;
            case "symbolic_link":
                return SymbolicLink;
            default:
                return Unknown;
        }
    }
}
