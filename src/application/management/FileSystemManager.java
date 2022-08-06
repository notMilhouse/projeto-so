package src.application.management;

import src.adapter.cli.CommandInterface;
import src.adapter.driver.DiskDriver;
import src.application.commandparsing.CommandParser;
import src.application.commandparsing.command.*;
import src.domain.snode.dentry.exceptions.InvalidEntryException;
import src.domain.snode.exceptions.InvalidLengthForSnodeException;
import src.domain.snode.exceptions.InvalidSNodeException;
import src.adapter.driver.exceptions.VirtualFileNotFoundException;
import src.domain.snode.FileType;
import src.domain.snode.SNode;
import src.domain.snode.SNodeDir;
import src.domain.snode.SNodeFile;
import src.domain.snode.dentry.DEntry;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * FileSystemManager is the kernel of the application.
 * It represents the manager itself, being responsible for handling command and manipulating a virtual disk.
 * */
public class FileSystemManager implements FileManagementInterface, VirtualDiskInspectionInterface {
    private final DiskDriver diskDriver;
    private final CommandInterface userInterface;
    private final CommandParser commandParser;

    private SNodeDir root;
    private String workDir = "";


    /**
     * Constructs a manager given a disk driver, a command interface and a command parser
     * Those fields, being defined in the constructor allow for customization of the manager and its
     * further reuse.
     * */
    public FileSystemManager(
        DiskDriver converter,
        CommandInterface commandInterface,
        CommandParser parser
    ) {
        diskDriver = converter;
        userInterface = commandInterface;
        commandParser = parser;
    }

    /**
     * run() is the mounting point of the file system manager, it initializes its workflow
     *
     * @throws Exception to be handled by the application class
     * */
    public void run() throws Exception {
        diskDriver.MountDisk();
        root = (SNodeDir)diskDriver.GetRoot();

        System.out.println(userInterface.manual());

        while (true) {
            try {
                handleCommand(
                    userInterface.run(workDir)
                );
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Given a command, executes the proper operation
     *
     * @param command is a Command object representing an operation and carrying its required data
     * */
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
        else if (command instanceof ChangeDirCommand) {
            changeDirectory(
                command.filePath
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
        else if (command instanceof HelpCommand) {
            System.out.println(
                userInterface.manual()
            );
        }
    }

    /**
     * used for finding a directory in the disk
     *
     * @param pathname is a path for the wanted directory
     * @return a SnodeDir object representing the directory
     *
     * @throws VirtualFileNotFoundException when virtual file is not found
     * */
    private SNodeDir searchDirectory(String pathname)
    throws VirtualFileNotFoundException
    {
        if(pathname.equals("/") && workDir.equals(""))  //o diretorio é o root. Isso considerando que pathname = "/" ;
            return root;

        
        pathname = workDir + pathname.replaceAll("^/+", "");
        String[] directories = pathname.replaceAll("^/+", "").split("/");

        return searchDirArray(root, directories);
    }

    /**
     * Searches a directory by searching its path steps recursively
     *
     * @param dir is where you are searching for a directory
     * @param directories is an array of directory names representing the subsequent steps in a previously given path
     * @return a SnodeDir object representing the directory
     *
     * @throws VirtualFileNotFoundException when virtual file is not found
     * */
    private SNodeDir searchDirArray(SNodeDir dir, String[] directories)
    throws VirtualFileNotFoundException
    {
        if(directories.length == 1)
        {
            try
            {
                return (SNodeDir)dir.searchInDirectory(directories[0]).getSNode();
            } catch(java.lang.ClassCastException err)
            {
                throw new VirtualFileNotFoundException("'" + directories[0] + "' não é diretório");
            }
        }
        else
        {
            return searchDirArray((SNodeDir)dir.searchInDirectory(directories[0]).getSNode(), Arrays.copyOfRange(directories, 1, directories.length));
        }
    }

    /*
     * Changes work directory to specified directory
     */
    private void changeDirectory(String pathname) throws VirtualFileNotFoundException
    {
        if(pathname.equals("/"))
        {
            workDir = "";
            return;
        }
        if(pathname.equals(".."))
        {
            String[] dirs = workDir.split("/");
            workDir = String.join("/", Arrays.copyOfRange(dirs, 0, dirs.length-1));
            return;
        }
        SNodeDir path = searchDirectory(pathname);
        if(path != null)
        {
            workDir = workDir.replaceAll("/+$", "");
            workDir += pathname;
            workDir += "/";
        }
    }


    @Override
    public boolean addDirectory(String pathname, String filename) throws InvalidEntryException, VirtualFileNotFoundException {
        SNodeDir path = searchDirectory(pathname);
        SNodeDir newDir = new SNodeDir();

        return diskDriver.WriteSNode(path, newDir, filename);
    }

    @Override
    public boolean addFile(String pathname, String filename, FileType type, int length) throws InvalidEntryException, VirtualFileNotFoundException {
        SNodeDir path = searchDirectory(pathname);

        SNodeFile newFile = null;
        try {
            newFile = new SNodeFile(
                type,
                length
            );
        } catch (InvalidLengthForSnodeException e) {
            System.out.println(e.getMessage());
            return false;
        }

        return diskDriver.WriteSNode(path, newFile, filename);
    }

    @Override
    public boolean deleteFile(String pathname, String filename) 
    throws InvalidEntryException, VirtualFileNotFoundException {
        SNodeDir path = searchDirectory(pathname);
        DEntry entry = path.searchInDirectory(filename);
        SNode nodeToDelete = entry.getSNode();

        return diskDriver.DeleteSNode(path, nodeToDelete);
    }

    @Override
    public String[] listDirectory(String pathname) throws InvalidEntryException, VirtualFileNotFoundException {
        SNodeDir path = searchDirectory(pathname);
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
        /** SNode Id
         *  Generation
         *  Creation Date
         *  Modification Date
         *  Size
         *  Datablocks Ids:
         */
        if(snodeId > diskDriver.GetNumberOfSnodes() || snodeId < 0)
        {
            throw new InvalidSNodeException("SNode id <"+ snodeId +"> is invalid for mounted disk");
        }
        SNode snode;
        try
        {
            snode = diskDriver.ParseSNode(snodeId*28);
        }
        catch(Exception err)
        {
            throw new InvalidSNodeException("Invalid SNode");
        }
        StringBuilder Info = new StringBuilder();
        Info.append("SNode id: " + snode.getIndexInBitmap() + "\n");
        Info.append("   Type                : " + snode.GetFileType() + "\n");
        Info.append("   Generation          : " + snode.getGeneration() + "\n");
        Info.append("   Creation Date       : " + snode.getCreationDate() + "\n");
        Info.append("   Modification Date   : " + snode.getModificationDate() + "\n");
        Info.append("   Size                : " + snode.getLength() + "\n");
        if(snode.GetFileType() == FileType.Directory)
        {
            Info.append("   Entries             : [");
            for(int i = 0; i < ((SNodeDir)snode).numberOfFilesInDir(); i++)
            {
                Info.append(((SNodeDir)snode).getDEntryAtIndex(i).getFileName() + ", ");
            }
            Info.append("]\n");

            return Info.toString();
        }
        Info.append("   Datablocks          : " + Arrays.toString(snode.getDatablocksReferences()) + "\n");
        return Info.toString();
    }

    @Override
    public String getSnodeBitmap() {
        return diskDriver.GetSNodeBitmap();
    }

    @Override
    public String getDataBlockBitmap() {
        return diskDriver.GetDatablockBitmap();
    }

}
