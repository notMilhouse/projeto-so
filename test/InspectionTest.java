package test;

import src.adapter.cli.CommandInterface;
import src.adapter.driver.DiskDriver;
import src.application.commandparsing.CommandParser;
import src.application.management.FileSystemManager;

import java.io.File;
import java.util.Scanner;

public class InspectionTest {
    public static void main(String[] args) throws Exception {
        if(args.length == 0 || args[0] == null || args[1] == null || args[2] == null) {
            System.out.println("Usage 'FileManagerApplication <disk path> <number of snodes> <number of datablocks>'");
            return;
        }

        int numberOfSnodes = Integer.parseInt(args[1]);
        int numberOfDatablocks = Integer.parseInt(args[2]);

        Scanner scanner = new Scanner(System.in);

        File disk = new File(args[0]);
        DiskDriver driver = new DiskDriver(disk, numberOfSnodes, numberOfDatablocks);
        CommandParser applicationParser = new CommandParser();
        
        CommandInterface userInterface = new CommandInterface(
            scanner,
            applicationParser);


        try {
            FileSystemManager fs = new FileSystemManager(driver,
                userInterface,
                applicationParser
            );
            driver.MountDisk();
            while(true)
            {
                System.out.println(fs.getSNodeInfo(Integer.parseInt(scanner.nextLine())));
                System.out.println(fs.getSnodeBitmap());
                System.out.println(fs.getDataBlockBitmap());
            }
            
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
