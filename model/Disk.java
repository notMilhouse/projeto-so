package model;

import java.util.ArrayList;

public class Disk {
    ArrayList<SNode> nodes;
    ArrayList<DataBlock> blocks;

    public byte[] toBinary(){ //conversão para binário do Disco 

    }

    public Disk(byte[] binaryDiskContent) {

    }

    public Disk() {
        nodes = new ArrayList<>(
            new SNode(2, 0, dataBlock)
        );
    }
}
