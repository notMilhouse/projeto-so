package src.application.management.implementationManagement;
import src.adapter.driver.DiskConverter;
import src.application.management.FileManagementInterface;
import src.application.management.VirtualDiskInspectionInterface;
import src.application.management.exceptions.InvalidEntryException;
import src.application.management.exceptions.InvalidSNodeException;
import src.application.management.exceptions.VirtualFileNotFoundException;
import src.domain.snode.FileType;
import src.domain.snode.SNodeDir;

public class FileSystemManager implements FileManagementInterface, VirtualDiskInspectionInterface {

    private final DiskConverter diskDriver;

    public FileSystemManager(DiskConverter converter) {
        diskDriver = converter;
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
        return false;
    }

    @Override
    public boolean saveVirtualDisk() {
        return false;
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
