package src.application.commandparsing.command;

import src.domain.snode.FileType;

import java.io.File;

public class AddDirectoryCommand extends Command{

    public AddDirectoryCommand(
        String filePath,
        String fileName
    ) {
        super(
            filePath,
            fileName,
            FileType.Directory,
            128
        );
    }
}
