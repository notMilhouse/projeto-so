package test;
import java.io.File;
import java.io.IOException;

import src.adapter.driver.DiskConverter;
import src.application.management.exceptions.InvalidEntryException;
import src.domain.snode.FileType;
import src.domain.snode.*;


public class driverTest {
    public static void main(String[] args)
    throws IOException, InvalidEntryException
    {
        int N = 16;
        int M = 24;
        File disk = new File("./testDisk.vdsk");

        

        DiskConverter driver = new DiskConverter(disk, N, M);
        driver.Read();


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
        driver.DeleteSNode(root, A);
        driver.WriteSNode(C, G, "G");
        driver.WriteSNode(G, H, "H");
        driver.WriteSNode(H, I, "I");

        ListAllFiles(root, "/");
        driver.SaveDisk(disk);
    }

    static SNode I;
    static SNodeDir H;

    private static void ListAllFiles(SNodeDir snode, String workDir)
    {
        
        for(int i = 0; i < snode.numberOfFilesInDir(); i++)
        {
            System.out.println(workDir + snode.getDEntryAtIndex(i).getFileName());
            if(snode.getDEntryAtIndex(i).getFileType() == FileType.Directory)
            {
                ListAllFiles((SNodeDir)snode.getDEntryAtIndex(i).getSnode(), workDir + snode.getDEntryAtIndex(i).getFileName() + "/");
                I = snode.getDEntryAtIndex(i).getSnode();
                H = (SNodeDir)snode;
            }
        }

    }

}
