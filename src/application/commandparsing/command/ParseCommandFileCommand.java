package src.application.commandparsing.command;

import src.application.commandparsing.CommandParser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseCommandFileCommand extends Command {

    public ParseCommandFileCommand(
        String commandFilePath
    ) {
        super(
            commandFilePath
        );
    }
}
