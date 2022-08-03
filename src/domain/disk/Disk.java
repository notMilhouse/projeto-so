package src.domain.disk;


import src.domain.snode.SNodeFile;
import src.domain.snode.*;
import src.domain.snode.dentry.DEntry;


public class Disk {

    private SNodeDir root; 


    public Disk() {
        root = new SNodeDir(); //inicialização do disk 
    }


    public void deleteFile(SNodeDir snodeDirBase, String file ){
            
        try{
         
            snodeDirBase.removeDEntry(file);
         
        } catch(Exception e){
            System.out.print(e);
        }
        
     
    }


    public void insertDirectory(SNodeDir snodeDirBase,SNodeDir snodeInsert, String fileName){
    

        try{    //tentativa inserção de um novo diretorio 

            SNodeDir newDirectorySnode = new SNodeDir();
            DEntry newDirectory = new DEntry(newDirectorySnode, 128, FileType.Directory, fileName);
 
            snodeDirBase.InsertDEntry(newDirectory);
            

        } catch(Exception e){

            System.out.print(e);
        }
        
    }

    public void insertFile(SNodeDir snodeDirBase, SNodeFile snodefile, String fileName){
        
        try{    //tentativa inserção de um novo diretorio 

            SNodeFile newDirectorySnode = snodefile;
            DEntry newFile = new DEntry(newDirectorySnode, 128, FileType.Directory, fileName);
 
            snodeDirBase.InsertDEntry(newFile);
            

        } catch(Exception e){

            System.out.print(e);
        }
    }
 

}

