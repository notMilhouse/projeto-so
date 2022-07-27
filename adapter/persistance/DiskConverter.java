package adapter.persistance;

import java.io.FileInputStream;

import model.VirtualDisk;
import model.Disk;
import model.SNode;

/**
 * DiskRecovery
 */
public class DiskConverter {

    public Disk recoverFromDisk(VirtualDisk disk, int N, int M) {
        SNode[] snodes = SNode.retriveNodes(disk.getBytes(N * 28));
        
        return new Disk();
    }
}