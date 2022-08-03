package src.application.commandparsing;

import src.application.commandparsing.command.AddDirectoryCommand;
import src.application.commandparsing.command.AddFileCommand;
import src.application.commandparsing.command.Command;
import src.application.commandparsing.command.DeleteInstanceCommand;
import src.application.commandparsing.command.ExitCommand;
import src.application.commandparsing.command.ParseCommandFileCommand;
import src.application.commandparsing.exception.CommandMissingArgumentsException;
import src.application.commandparsing.exception.CommandNotFoundException;

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
            default:
                throw new CommandNotFoundException();
        }
    }
    //TODO missing commands for saving disk and listing directory entries
    //TODO change parseCommandFile to parse
    /*
    *
    *
    * */
}
