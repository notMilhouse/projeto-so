package src.application.commandparsing.command;

import src.domain.snode.FileType;

/**
 * AddDirectoryCommand represents the delete file manager operation for both directories and files.
 * It requires a file path and a file name, and use default values for the other fields.
 * */
public class DeleteInstanceCommand extends Command{

    public DeleteInstanceCommand(
        String filePath,
        String fileName
    ) {
        super(
            filePath,
            fileName,
            FileType.Unknown,
            0
        );
    }
}
