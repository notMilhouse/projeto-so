package src.adapter.cli;

import src.application.commandparsing.CommandParser;
import src.application.commandparsing.exception.CommandMissingArgumentsException;
import src.application.commandparsing.exception.CommandNotFoundException;

import java.util.Scanner;

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

    public void run() throws CommandMissingArgumentsException, CommandNotFoundException {
        System.out.println("CLI teste");

        while(true) {
            System.out.print("#> ");
            commandParser
                .parseCommand(commandScanner.nextLine())
                .execute();
        }
    }

}
