package src.adapter.cli;

import src.application.commandparsing.CommandParser;
import src.application.commandparsing.command.Command;
import src.application.commandparsing.exception.CommandMissingArgumentsException;
import src.application.commandparsing.exception.CommandNotFoundException;

import java.util.Scanner;

/**
* CommandInterface is responsible for translating user input to commands
* @see Command
* */
public class CommandInterface {
    private final Scanner commandScanner;
    private final CommandParser commandParser;

    /**
     * Constructor of the command interface.
     * @param commandScanner the scanner for getting user input, can be defined in runtime, allowing for custom sources
     *                       other than the default System.in.
     * @param commandParser the command matcher, mapping a given raw command to a {@link Command} object representing the
     *                      desired operation.
     * */
    public CommandInterface(
        Scanner commandScanner,
        CommandParser commandParser
    ) {
        this.commandParser = commandParser;
        this.commandScanner = commandScanner;
    }

    /**
     * Initializes the routine for user interaction with the application
     * by reading the next line available from the scanner and parsing it as a command
     * using the parser.
     * */
    public Command run() throws CommandMissingArgumentsException, CommandNotFoundException {
        System.out.print("#> ");
        return commandParser.parseCommand(commandScanner.nextLine());
    }
}
