package src.application.commandparsing.command;

import src.domain.snode.FileType;

/**
 * AddFileCommand represents the add file, file manager operation.
 * It requires all {@link Command} fields.
 * */
public class AddFileCommand extends Command{

    public AddFileCommand(
            String filePath,
            String fileName,
            String fileType,
            String fileLength
    ) {
        super(
            filePath,
            fileName,
            FileType.parseFileType(fileType),
            Integer.parseInt(fileLength)
        );
    }
}
