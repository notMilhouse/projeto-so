package src.application.commandparsing.command;

import src.domain.snode.FileType;


/**
 * Command is a representation of an operation in the FileSystemManager
 * containing the data required to perform the operation itself.
 * It allows for easier inclusion of new operations and decouples those concepts from the manager scope.
 * Operations may or may not need the fields given by the class,
 * which situations are represented by the different constructors.
 * */
public abstract class Command {

    public final String filePath;
    public final String fileName;
    public final FileType fileType;
    public final int fileLength;

    protected Command() {
        filePath = "";
        fileName = "";
        fileType = null;
        fileLength = 0;
    }
    protected Command(String path) {
        filePath = path;
        fileName = "";
        fileType = null;
        fileLength = 0;
    }

    protected Command(
        String filePath,
        String fileName,
        FileType fileType,
        int fileLength
    ) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileLength = fileLength;
    }
}
