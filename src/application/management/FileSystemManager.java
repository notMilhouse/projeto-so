package src.application.management;

import src.adapter.cli.CommandInterface;
import src.adapter.driver.DiskConverter;
import src.application.commandparsing.CommandParser;
import src.application.commandparsing.command.*;
import src.application.management.exceptions.InvalidEntryException;
import src.application.management.exceptions.InvalidSNodeException;
import src.application.management.exceptions.VirtualFileNotFoundException;
import src.domain.snode.FileType;
import src.domain.snode.SNode;
import src.domain.snode.SNodeDir;
import src.domain.snode.SNodeFile;
import src.domain.snode.dentry.DEntry;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileSystemManager implements FileManagementInterface, VirtualDiskInspectionInterface {
    private final DiskConverter diskDriver;
    private final CommandInterface userInterface;
    private final CommandParser commandParser;

    private SNodeDir root;


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
        diskDriver.Read();
        root = (SNodeDir)diskDriver.GetRoot();
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
            System.out.println(
                addFile(
                    command.filePath,
                    command.fileName,
                    command.fileType,
                    command.fileLength
                )
            );
            
        }
        else if (command instanceof AddDirectoryCommand) {
            System.out.println(
                addDirectory(
                    command.filePath,
                    command.fileName
                )
            );
            
        }
        else if (command instanceof DeleteInstanceCommand) {
            System.out.println(
                deleteFile(
                    command.filePath,
                    command.fileName
                )
            );
            
        }
        else if (command instanceof ListDirCommand) {
            System.out.println( Arrays.toString(
                listDirectory(
                    command.filePath
                )
            ));
            
        }
        else if (command instanceof ParseCommandFileCommand) {
            System.out.println(
                parseCommandFile(
                    command.filePath
                )
            );
            
        }
        else if (command instanceof SaveCommand) {
            System.out.println(
                saveVirtualDisk()
            );  
            
        }
        else if (command instanceof ExitCommand) {
            System.exit(0);
        }
    }



    //public void WriteSNode(SNodeDir dir, SNode snode, String name)
    //public boolean DeleteSNode(SNodeDir dir, SNode snode)
    //public SNode GetRoot()
    //public SNode ParseSNode(int atRef)
    //public DEntry ParseDir(int atRef)

    private SNodeDir searchDir(String pathname)
    throws VirtualFileNotFoundException
    {
        if(pathname.equals("/"))  //o diretorio é o root. Isso considerando que pathname = "/" ;
            return root;

        
        String[] directories = pathname.replaceAll("^/+", "").split("/");
        
        //agr precisamos pegar as referencias do Dentry do dir e buscar 
        return searchDirArray(root, directories);
    }

    private SNodeDir searchDirArray(SNodeDir dir, String[] directories)
    throws VirtualFileNotFoundException
    {
        if(directories.length == 1)
        {
            try
            {
                return (SNodeDir)dir.searchInDirectory(directories[0]).getSNode();
            }
            catch(VirtualFileNotFoundException err)
            {
                throw err;
            }
            catch(java.lang.ClassCastException err)
            {
                throw new VirtualFileNotFoundException("'" + directories[0] + "' não é diretório");
            }
            catch(Exception err)
            {
                throw err;
            }
        }
        else
        {
            return searchDirArray((SNodeDir)dir.searchInDirectory(directories[0]).getSNode(), Arrays.copyOfRange(directories, 1, directories.length));
        }
    }


    @Override
    public boolean addDirectory(String pathname, String filename) throws InvalidEntryException, VirtualFileNotFoundException {
        SNodeDir path = searchDir(pathname);
        SNodeDir newDir = new SNodeDir();

        return diskDriver.WriteSNode(path, newDir, filename);
    }

    @Override
    public boolean addFile(String pathname, String filename, FileType type, int length) throws InvalidEntryException, VirtualFileNotFoundException {
        SNodeDir path = searchDir(pathname);

        SNodeFile newFile = new SNodeFile(
            type,
            length
        );

        return diskDriver.WriteSNode(path, newFile, filename);
    }

    @Override
    public boolean deleteFile(String pathname, String filename) 
    throws InvalidEntryException, VirtualFileNotFoundException {
        SNodeDir path = searchDir(pathname);
        DEntry entry = path.searchInDirectory(filename);
        SNode nodeToDelete = entry.getSNode();

        return diskDriver.DeleteSNode(path, nodeToDelete);
    }

    @Override
    public String[] listDirectory(String pathname) throws InvalidEntryException, VirtualFileNotFoundException {
        SNodeDir path = searchDir(pathname);
        String[] entries = new String[path.numberOfFilesInDir()];

        for(int i = 0; i < path.numberOfFilesInDir(); i++)
        {
            entries[i] = ("/" + path.getDEntryAtIndex(i).getFileName());
        }
        return entries;
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
