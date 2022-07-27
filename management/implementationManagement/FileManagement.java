package management.implementationManagement;
import management.exceptions.InvalidEntryException;
import management.exceptions.VirtualFileNotFoundException;
import management.interfacesFiles.FileManagementInterface;
import model.FileType;

public class FileManagement implements FileManagementInterface {

    @Override
    public boolean addDirectory(String pathname, String filename)
            throws InvalidEntryException, VirtualFileNotFoundException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addFile(String pathname, String filename, FileType type, int length)
            throws InvalidEntryException, VirtualFileNotFoundException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean deleteFile(String pathname, String filename)
            throws InvalidEntryException, VirtualFileNotFoundException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String[] listDirectory(String pathname) throws InvalidEntryException, VirtualFileNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean parseCommandFile(String pathname) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean saveVirtualDisk() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
