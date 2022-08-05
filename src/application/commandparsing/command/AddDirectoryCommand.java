package src.application.commandparsing.command;

import src.domain.snode.FileType;

public class AddDirectoryCommand extends Command{

    public AddDirectoryCommand(
        String filePath,
        String fileName
    ) {
        this.filePath = filePath;
        this.fileName = fileName;
        fileType = FileType.Directory;
        fileLength = 128; //TODO check length
    }
    @Override
    public void execute() {
        fileManager
    }
}
