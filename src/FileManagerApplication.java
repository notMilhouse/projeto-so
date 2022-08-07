package src;

import src.adapter.cli.CommandInterface;
import src.adapter.driver.DiskDriver;
import src.application.commandparsing.CommandParser;
import src.application.management.FileSystemManager;

import java.io.File;
import java.util.Scanner;

public class FileManagerApplication {

    /**
     * The application flow is as follows:
     * <ol>
     *     <li>Application arguments are parsed</li>
     *     <li>Define file manager information</li>
     *     <li>Initializes the disk driver, the application parser and user interface </li>
     *     <li>Instantiates the file manager and runs it</li>
     * </ol>
     * */
    public static void main(String[] args) {
        if(args.length == 0 || args[0] == null || args[1] == null || args[2] == null) {
            System.out.println("Usage 'FileManagerApplication <disk path> <number of snodes> <number of datablocks>'");
            return;
        }

        int numberOfSnodes = Integer.parseInt(args[1]);
        int numberOfDatablocks = Integer.parseInt(args[2]);

        


        try {
            File disk = new File(args[0]);
            DiskDriver driver = new DiskDriver(disk, numberOfSnodes, numberOfDatablocks);
            CommandParser applicationParser = new CommandParser();
            CommandInterface userInterface = new CommandInterface(
                new Scanner(System.in),
                    applicationParser
                );

            new FileSystemManager(driver,
                userInterface,
                applicationParser
            ).run();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}