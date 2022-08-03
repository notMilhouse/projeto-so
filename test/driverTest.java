package test;
import java.io.File;
import src.adapter.driver.DiskConverter;;


public class driverTest {
    public static void main(String[] args)
    {
        int N = 16;
        int M = 24;
        File disk = new File("E:\\USP\\OS\\Trabalho2\\projeto-so/SampleVirtualDisk.vdsk");

        DiskConverter driver = new DiskConverter(disk, N, M);
        driver.Read();
    }
}
