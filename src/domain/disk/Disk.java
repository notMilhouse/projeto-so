package src.domain.disk;

import src.domain.snode.*;
import src.domain.snode.dentry.DEntry;


public class Disk {

    private SNodeDir root; 

    public Disk() {
        root = new SNodeDir(); //criação do disco inicial 
    }

    public boolean insertDirectory(String pathname,String fileName){
        
        String[] directorysName = pathname.split("/",0);
        
        SNodeDir actualDirectory = root; 
    
        for (String directory : directorysName) {
            try{ 
                actualDirectory = actualDirectory.searchInDirectory(directory).getSNode(); //realiza a busca dentro do diretório, buscando pelo elemento 
       
            
            } catch (Exception e) {
                System.out.print(e);
            }   
            
        }

        try{    //tentativa inserção de um novo diretorio 
            SNodeDir newDirectorySnode = new SNodeDir();
            DEntry newDirectory = new DEntry(newDirectorySnode, 128, FileType.Directory, fileName);

            actualDirectory.InsertDEntry(newDirectory);
            
        } catch(Exception e){

            System.out.print(e);
        }
        
    }

 
    



}
