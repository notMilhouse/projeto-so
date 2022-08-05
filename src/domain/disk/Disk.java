package src.domain.disk;


import src.domain.snode.SNodeFile;

import java.security.Principal;

import src.domain.snode.*;
import src.domain.snode.dentry.DEntry;


public class Disk {

    private SNodeDir root;

    public Disk() {
        root = new SNodeDir(); //inicialização do disk 
    }

    public boolean deleteFile(String pathName, String file ){

        SNodeDir SNodeDirBase = SearchDir(pathName);

        try{
            SNodeDirBase.removeDEntry(file);
            return true;

        } catch(Exception e){
            System.out.print(e);
            return false;
        }
    }


    public boolean insertDirectory(String pathName, String fileName){
    

        SNodeDir snodeDirBase = SearchDir(pathName);


        try{    //tentativa inserção de um novo diretorio 

            SNodeDir newDirectorySnode = new SNodeDir();
            DEntry newDirectory = new DEntry(newDirectorySnode, FileType.Directory, fileName);
 
            snodeDirBase.InsertDEntry(newDirectory);
            
            return true; 

        } catch(Exception e){

            System.out.print(e);
        
            return false; 
        }
        
    }

    public boolean insertFile(String pathName, SNodeFile snodefile, String fileName){


        SNodeDir snodeDirBase = SearchDir(pathName);
        
        try{    //tentativa inserção de um novo diretorio 

            SNodeFile newDirectorySnode = snodefile;
            DEntry newFile = new DEntry(newDirectorySnode, FileType.Directory, fileName);
 
            snodeDirBase.InsertDEntry(newFile);
            
            return true; 

        } catch(Exception e){

            System.out.print(e);

            return false;
        }
    }

    private SNodeDir SearchDir(String pathName){
        
        String[] pathname = pathName.split("/"); 

        SNodeDir directory = root;


        for (String actualDir : pathname) {
            
            try{

                directory = (SNodeDir) directory.searchInDirectory(actualDir).getSNode();

            } catch(Exception e){

                System.out.println(e);
            }
        }
        return directory;
    }
}

