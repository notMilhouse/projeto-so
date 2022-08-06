package src.application.commandparsing;

import src.application.commandparsing.command.*;
import src.application.commandparsing.exception.*;


/**
 * CommandParser is responsible for, given a raw string command, matching it to a Command object.
 * */
public class CommandParser {

    /**
     * parseCommand matches a string to a Command, and parses any given arguments to the required fields
     * @param command represents a raw command given by user input
     * @return a Command object
     *
     * @throws CommandNotFoundException when there is no command that matches the given string
     * @throws CommandMissingArgumentsException when a command is valid but its required fields are not included in user prompt
     * */
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
                    commandArguments[1]
                );
            case "exit":
                return new ExitCommand();
            case "saveDisk":
                return new SaveCommand();
            case "listDirectory":
                return new ListDirCommand(
                    commandArguments[1]
                );
            default:
                throw new CommandNotFoundException();
        }
    }
}
