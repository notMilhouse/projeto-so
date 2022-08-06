package test;

import src.adapter.cli.CommandInterface;
import src.application.commandparsing.CommandParser;
import src.application.commandparsing.exception.CommandMissingArgumentsException;
import src.application.commandparsing.exception.CommandNotFoundException;

import java.util.Scanner;

public class CommandInterfaceTest {
    public static void main(String[] args) throws CommandMissingArgumentsException, CommandNotFoundException {
        new CommandInterface(
            new Scanner(System.in),
            new CommandParser()
        ).run("");
    }
}
