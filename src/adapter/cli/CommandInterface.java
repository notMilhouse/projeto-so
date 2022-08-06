package src.adapter.cli;

import src.application.commandparsing.CommandParser;
import src.application.commandparsing.command.Command;
import src.application.commandparsing.exception.CommandMissingArgumentsException;
import src.application.commandparsing.exception.CommandNotFoundException;

import java.util.Scanner;

/*
* CommandInterface translates user input to commands and execute them
*
* */
public class CommandInterface {
    private final Scanner commandScanner;
    private final CommandParser commandParser;
    public CommandInterface(
        Scanner commandScanner,
        CommandParser commandParser
    ) {
        this.commandParser = commandParser;
        this.commandScanner = commandScanner;
    }
    public Command run() throws CommandMissingArgumentsException, CommandNotFoundException {
        System.out.print("#> ");
        return commandParser.parseCommand(commandScanner.nextLine());
    }
}
