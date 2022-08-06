package src.application.commandparsing.command;

/**
 * ListDirCommand represents the list directory file manager operation.
 * It requires a file path, and use default values for the other fields.
 * */
public class ListDirCommand extends Command{
    public ListDirCommand(
        String filePath
    ) {
        super(
            filePath
        );
    }
}
