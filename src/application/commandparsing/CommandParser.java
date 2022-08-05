package src.application.commandparsing;

import src.application.commandparsing.command.*;
import src.application.commandparsing.exception.*;

public class CommandParser {

    public Command parseCommand(String command) throws CommandNotFoundException, CommandMissingArgumentsException {
        String[] commandArguments = command.split(" ");

        switch (commandArguments[0]) {
            case "addFile":
                if (commandArguments.length != 5) {
                    throw new CommandMissingArgumentsException();
                }
                return new AddFileCommand(
                    commandArguments[1],
                    commandArguments[2],
                    commandArguments[3],
                    commandArguments[4]
                );
            case "addDir":
                if (commandArguments.length != 3) {
                    throw new CommandMissingArgumentsException();
                }

                return new AddDirectoryCommand(
                    commandArguments[1],
                    commandArguments[2]
                );
            case "delete":
                if (commandArguments.length != 3) {
                    throw new CommandMissingArgumentsException();
                }

                return new DeleteInstanceCommand(
                    commandArguments[1],
                    commandArguments[2]
                );
            case "parse":
                if (commandArguments.length != 2) {
                    throw new CommandMissingArgumentsException();
                }

                return new ParseCommandFileCommand(
                    this,
                    commandArguments[1]
                );
            case "exit":
                return new ExitCommand();
            case "saveDisk":
                return new SaveCommand();
            case "listDirectory":
                return new ListDirCommand();
            default:
                throw new CommandNotFoundException();
        }
    }
}
