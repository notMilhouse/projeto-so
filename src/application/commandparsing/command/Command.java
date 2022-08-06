package src.application.commandparsing.command;

import src.domain.snode.FileType;

import java.io.File;

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

    @Override
    public String toString(){
        return String.format(
            "path: %s\nname: %s\ntype: %s\nlength: %s",
            filePath,
            fileName,
            fileType.toString(),
            fileLength
        );
    }
}
