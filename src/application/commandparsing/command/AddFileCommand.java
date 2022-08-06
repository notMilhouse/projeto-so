package src.application.commandparsing.command;

import src.domain.snode.FileType;

import java.io.File;

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
