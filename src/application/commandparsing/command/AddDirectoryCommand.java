package src.application.commandparsing.command;

import src.domain.snode.FileType;

/**
 * AddDirectoryCommand represents the add directory file manager operation.
 * It requires a file path and a file name, and use default values for the other fields.
 * */
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
