package src.application.management;

import src.adapter.cli.CommandInterface;
import src.adapter.driver.DiskConverter;
import src.application.commandparsing.CommandParser;
import src.application.commandparsing.command.*;
import src.application.management.exceptions.InvalidEntryException;
import src.application.management.exceptions.InvalidSNodeException;
import src.application.management.exceptions.VirtualFileNotFoundException;
import src.domain.snode.FileType;
import src.domain.snode.SNodeDir;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class FileSystemManager implements FileManagementInterface, VirtualDiskInspectionInterface {
    private final DiskConverter diskDriver;
    private final CommandInterface userInterface;
    private final CommandParser commandParser;

    public FileSystemManager(
        DiskConverter converter,
        CommandInterface commandInterface,
        CommandParser parser
    ) {
        diskDriver = converter;
        userInterface = commandInterface;
        commandParser = parser;
    }

    public void run() {
        while (true) {
            try {
                handleCommand(
                    userInterface.run()
                );
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void handleCommand(Command command) throws VirtualFileNotFoundException, InvalidEntryException {
        if (command instanceof AddFileCommand) {
            addFile(
                command.filePath,
                command.fileName,
                command.fileType,
                command.fileLength
            );
        }
        if (command instanceof AddDirectoryCommand) {
            addDirectory(
                command.filePath,
                command.fileName
            );
        }
        if (command instanceof DeleteInstanceCommand) {
            deleteFile(
                command.filePath,
                command.fileName
            );
        }
        if (command instanceof ListDirCommand) {
            listDirectory(
                command.filePath
            );
        }
        if (command instanceof ParseCommandFileCommand) {
            parseCommandFile(
                command.filePath
            );
        }
        if (command instanceof SaveCommand) {
            saveVirtualDisk();
        }
        if (command instanceof ExitCommand) {
            System.exit(0);
        }
    }



    // public void WriteSNode(SNodeDir dir, SNode snode, String name)
    // public boolean DeleteSNode(SNodeDir dir, SNode snode)
    //public SNode GetRoot()
    // public SNode ParseSNode(int atRef)
    //   public DEntry ParseDir(int atRef)

    private SNodeDir searchDir(String pathname){
        SNodeDir dir = (SNodeDir) diskDriver.GetRoot();

        String[] directorys = pathname.split("/");

        if(directorys.length == 1)  //o diretorio é o root. Isso considerando que pathname = " " ;
            return dir;

        
        //agr precisamos pegar as referencias do Dentry do dir e buscar 
       
        for (String actualDir : directorys) {
            
            try{

                dir =  (SNodeDir) dir.searchInDirectory(actualDir).getSNode();

            } catch(Exception e){

                System.out.println(e);
        
            }

        }

        return dir;

        
    }


    @Override
    public boolean addDirectory(String pathname, String filename) throws InvalidEntryException, VirtualFileNotFoundException {

        return false;
    }

    @Override
    public boolean addFile(String pathname, String filename, FileType type, int length) throws InvalidEntryException, VirtualFileNotFoundException {
        return false;
    }

    @Override
    public boolean deleteFile(String pathname, String filename) throws InvalidEntryException, VirtualFileNotFoundException {
        return false;
    }

    @Override
    public String[] listDirectory(String pathname) throws InvalidEntryException, VirtualFileNotFoundException {
        return new String[0];
    }

    @Override
    public boolean parseCommandFile(String pathname) {
        try {
            ArrayList<Command> commandList = new ArrayList<>();

            FileInputStream file = new FileInputStream(pathname);
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                commandList.add(commandParser
                    .parseCommand(
                        fileScanner.nextLine()
                    )
                );
            }

            for (Command command : commandList) {
                handleCommand(command);
            }

            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean saveVirtualDisk() {
        try{
            diskDriver.SaveDisk();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public String getSNodeInfo(int snodeId) throws InvalidSNodeException {
        return null;
    }

    @Override
    public String getSnodeBitmap() {
        return null;
    }

    @Override
    public String getDataBlockBitmap() {
        return null;
    }

}
