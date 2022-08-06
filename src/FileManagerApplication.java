package src;
import src.application.management.FileSystemManager;
import src.application.commandparsing.CommandParser;

import java.io.File;
import java.util.Scanner;

import src.adapter.driver.DiskConverter;
import src.adapter.cli.CommandInterface;

public class FileManagerApplication {
    public static void main(String[] args) {

        if(args.length == 0 || args[0] == null || args[1] == null || args[2] == null) {
            System.out.println("Usage 'FileManagerApplication <disk path> <number of snodes> <number of datablocks>'");
            return;
        }

        String disk = args[0];
        int numberOfSnodes = Integer.parseInt(args[1]);
        int numberOfDatablocks = Integer.parseInt(args[2]);

        new FileSystemManager(new DiskConverter(new File(disk),
                numberOfSnodes, 
                numberOfDatablocks
            ), 
            new CommandInterface(new Scanner(System.in), new CommandParser()),
            new CommandParser()
        ).run();
    }
}