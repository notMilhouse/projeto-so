package src.domain.disk;

import src.domain.snode.*;


public class Disk {

    private SNodeDir root; 

    Disk() {
        root = new SNodeDir(); //criação do disco inicial 
    }



    
    public SNodeDir searchDirectory(String pathname){
        String[] directorysName = pathname.split("/",0);
        
        SNodeDir actualDirectory = root; 
    

        for (String directory : directorysName) {
            
            actualDirectory = actualDirectory.searchInDirectory(directory).getSNode(); //temos um problema aqui, o DEntry é um DEntry genérico, ele tem o tipo SNode,    
        }
       
        return actualDirectory;
   
   
    }





}
