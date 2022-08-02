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
        fileLength = 128;
    }
    @Override
    public void execute() {
        //TODO blabla
        System.out.println("adicionaria um diretorio");
        System.out.println(super.toString());
    }
}
