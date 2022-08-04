package test;
import java.io.File;
import src.adapter.driver.DiskConverter;
import src.domain.snode.FileType;
import src.domain.snode.SNodeDir;;


public class driverTest {
    public static void main(String[] args)
    {
        int N = 16;
        int M = 24;
        File disk = new File("E:\\USP\\OS\\Trabalho2\\projeto-so/SampleVirtualDisk.vdsk");

        DiskConverter driver = new DiskConverter(disk, N, M);
        driver.Read();
        SNodeDir root = (SNodeDir)driver.GetRoot();
        ListAllFiles(root, "/");

        

    }

    private static void ListAllFiles(SNodeDir snode, String workDir)
    {
        for(int i = 0; i < snode.numberOfFilesInDir(); i++)
        {
            System.out.println(workDir + snode.getDEntryAtIndex(i).getFileName());
            if(snode.getDEntryAtIndex(i).getFileType() == FileType.Directory)
            {
                ListAllFiles((SNodeDir)snode.getDEntryAtIndex(i).getSnode(), workDir + snode.getDEntryAtIndex(i).getFileName() + "/");
            }
        }
    }

}
