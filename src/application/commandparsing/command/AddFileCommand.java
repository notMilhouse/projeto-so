package src.application.commandparsing.command;

import src.domain.snode.FileType;

public class AddFileCommand extends Command{

    public AddFileCommand(
            String filePath,
            String fileName,
            String fileType,
            String fileLength
    ) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileType = FileType.parseFileType(fileType);
        this.fileLength = Integer.parseInt(fileLength);
    }

    @Override
    public void execute() {
        //TODO add file logic calling management layer
        System.out.println("adicionaria um arquivo");
        System.out.println(super.toString());
    }
}
