package src;
import src.application.management.FileSystemManager;
import src.domain.disk.Disk;
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

        int numberOfSnodes = Integer.parseInt(args[1]);
        int numberOfDatablocks = Integer.parseInt(args[2]);

        File disk = new File(args[0]);

        DiskConverter driver = new DiskConverter(disk, numberOfSnodes, numberOfDatablocks);

        new FileSystemManager(driver, 
            new CommandInterface(new Scanner(System.in), new CommandParser()),
            new CommandParser()
        ).run();
    }
}