package src.application.commandparsing.command;

import src.application.commandparsing.CommandParser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseCommandFileCommand extends Command {
    private CommandParser commandParser;
    private String commandFile;

    public ParseCommandFileCommand(
        CommandParser commandParser,
        String commandFile
    ) {
        this.commandParser = commandParser;
        this.commandFile = commandFile;
    }

    @Override
    public void execute() {
        ArrayList<Command> commandList = parseCommandFile(commandFile);

        assert commandList != null;
        for (Command command : commandList) {
            command.execute();
        }
    }

    private ArrayList<Command> parseCommandFile(String fileName) {
        ArrayList<Command> commandList = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(fileName);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                commandList.add(commandParser
                    .parseCommand(
                        fileScanner.nextLine()
                    )
                );
            }

            return commandList;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null; //TODO kill
    }
}
