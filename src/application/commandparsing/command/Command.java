package src.application.commandparsing.command;

import src.application.management.FileSystemManager;
import src.domain.snode.FileType;

public abstract class Command {

    protected String filePath;
    protected String fileName;
    protected FileType fileType;
    protected int fileLength;

    abstract public void execute();

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
