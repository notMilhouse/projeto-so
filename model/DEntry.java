package model;

public class DEntry {
    private SNode sNode;
    private short entryLength;
    private FileType fileType;
    private String fileName;

    
    /**
     *              SNode[] | BitMap   | DataBlock[] | BitMap  => Memória Fisica 
     * 
     * 
     *              DEntry.SNode
     * 
     * 
     *              SE Nova entrada for um diretorio 
     *                  SNode.DEntrys[]  -> aponta para o subdiretorio/arqiovp = 1 bloco 
     *              Se a entrada for um arquivo
     *                  SNode.ArryList<bytes>  = 1 - 4 blocos de 128 bytes 
     * 
     */

    DEntry( SNode snode, short entryLength, FileType fileType, String fileName ) {
        this.sNode = snode;
        this.entryLength = entryLength;
        this.fileType = fileType;
        this.fileName = fileName;
    }
    
    public SNode getSNode() {
        return sNode;
    }
}
