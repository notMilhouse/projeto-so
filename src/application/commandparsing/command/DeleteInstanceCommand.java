package src.application.commandparsing.command;

import src.domain.snode.FileType;

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
