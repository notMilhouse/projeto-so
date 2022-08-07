package test;

import src.adapter.driver.DiskDriver;
import src.domain.snode.FileType;
import src.domain.snode.SNodeDir;
import src.domain.snode.SNodeFile;

import java.io.File;


public class driverTest {
    public static void main(String[] args)
        throws Exception {
        int N = 16;
        int M = 24;
        File disk = new File("./testDisk.vdsk");

        

        DiskDriver driver = new DiskDriver(disk, N, M);
        driver.MountDisk();


        SNodeDir root = (SNodeDir)driver.GetRoot();

        SNodeFile A = new SNodeFile(FileType.Regular, 500);
        SNodeFile B = new SNodeFile(FileType.BlockDevice, 128);
        SNodeDir C = new SNodeDir();
        SNodeFile E = new SNodeFile(FileType.Regular, 301);
        SNodeDir G = new SNodeDir();
        SNodeDir H = new SNodeDir();
        SNodeFile I = new SNodeFile(FileType.Fifo, 350);

        driver.WriteSNode(root, A, "A");
        driver.WriteSNode(root, B, "B");
        driver.WriteSNode(root, C, "C");
        driver.WriteSNode(C, E, "E");
        System.out.println(driver.DeleteSNode(root, A));
        driver.WriteSNode(C, G, "G");
        driver.WriteSNode(G, H, "H");
        driver.WriteSNode(H, I, "I");

        ListAllFiles(root, "/");
        driver.SaveDisk();
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
