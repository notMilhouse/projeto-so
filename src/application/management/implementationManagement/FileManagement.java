package src.application.management.implementationManagement;
import src.domain.snode.SNodeDir;
import src.application.management.exceptions.InvalidEntryException;
import src.application.management.exceptions.VirtualFileNotFoundException;
import src.application.management.interfacesFiles.FileManagementInterface;
import src.domain.disk.Disk;
import src.domain.snode.FileType;

public class FileManagement implements FileManagementInterface {

    private Disk diskStruct = new Disk();
        


    @Override
    public boolean addDirectory(String pathname, String filename)
        throws InvalidEntryException, VirtualFileNotFoundException {
         


         

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
