package src.application.commandparsing.command;

/**
 * ParseCommandFileCommand represents the parse file manager operation.
 * It requires a file path containing the path to a command file.
 * */
public class ParseCommandFileCommand extends Command {

    public ParseCommandFileCommand(
        String commandFilePath
    ) {
        super(
            commandFilePath
        );
    }
}
