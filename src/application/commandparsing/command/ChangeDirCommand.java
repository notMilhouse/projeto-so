package src.application.commandparsing.command;

/**
 * ChangeDirCommand represents the change dir file manager operation.
 * It requires a file path, and use default values for the other fields.
 * */
public class ChangeDirCommand extends Command{
    public ChangeDirCommand(
        String filePath
    ) {
        super(
            filePath
        );
    }
}
