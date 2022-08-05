package src;

import java.io.FileInputStream;
import src.domain.snode.*; //TODO probleminhas aqui kkk

public class FileManagerApplication {
    public static void main(String[] args) {

        if(args.length == 0 || args[0] == null || args[1] == null || args[2] == null) {
            System.out.println("Por favor, informe o nome do disco virtual, bem como o número de arquivos e de blocos de dados referentes a ele.");
            return;
        }

        String diskFileName = args[0];
        int numFiles = Integer.parseInt(args[1]);
        int numDataBlocks = Integer.parseInt(args[2]);

        FileInputStream diskFile;

        
        //SNode.setBitMap(numFiles);
    }
}
