package model.parse;
import java.util.*;
public class CMDParser
{

    Dictionary<> DictCMD = 
    {
        "addFile": addFile,
        ""

    }

    public void addFile(String args[])
    {

    }


    void parseLine(String[] line)
    {
        DictCMD[line[0]](line);
    }

}