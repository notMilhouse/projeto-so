package src.application.management;

import src.adapter.driver.DiskConverter;
import src.application.management.exceptions.InvalidEntryException;
import src.application.management.exceptions.VirtualFileNotFoundException;
import src.domain.disk.Disk;
import src.domain.snode.FileType;
import src.domain.snode.SNodeDir;

public class FileSystemManager implements FileManagementInterface, VirtualDiskInspectionInterface {
    private final Disk fileSystem;
    private final DiskConverter diskDriver;

    public FileSystemManager(
        Disk diskImage,
        DiskConverter converter
    ) {
        fileSystem = diskImage;
        diskDriver = converter;
    }

    @Override
    public boolean addDirectory(String pathname, String filename) throws InvalidEntryException, VirtualFileNotFoundException {
        fileSystem.addDirectory(new SNodeDir(
            //TODO
        ));

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
        return false;
    }

    @Override
    public boolean saveVirtualDisk() {
        return false;
    }
}
