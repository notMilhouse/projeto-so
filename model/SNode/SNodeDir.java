package model.SNode;
import java.util.ArrayList;

import model.DEntry;
import model.FileType;

public class SNodeDir extends SNodeBase {
  
    ArrayList<DEntry> DEntryList;

    SNodeDir(){
        super(FileType.Directory, 128);
    }
}
