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

           
             boolean successfulOperation =  diskStruct.insertDirectory(pathName, snodeInsert, fileName);
              
             return successfulOperation;

    }


    @Override
    public boolean addFile(String pathname, String filename, FileType type, int length)
        throws InvalidEntryException, VirtualFileNotFoundException {

       
        SNodeFile file = new SNodeFile(filename,length);
        boolean successfulOperation =    diskStruct.insertFile(pathName, file, fileName);

            

     
        return successfulOperation;
    }

    @Override
    public boolean deleteFile(String pathname, String filename)
            throws InvalidEntryException, VirtualFileNotFoundException {
        
        boolean successfulOperation = diskStruct.deleteFile(pathName, file);


        return successfulOperation;
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
