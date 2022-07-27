package model.parse;
public class Parser
{
    File disk;
    Parser(File file)
    {
        disk = file;
    }

    public void Read()
    {
        SNode root = ParseSNode(0);

    }

    public void Write()
    {
        //Persiste no arquivo
    }

    /**
    *
    */
    public SNode ParseSNode(int atRef)
    {
        //Montar SNode

        /*
        generation = 0;
        creationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli(); //tempo de criação do SNode  
        modificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        */


        return SNode snode;    
    }
    public DEntry ParseDir(int atRef)
    {

        SNode node = ParseSNode(ref);
        //Montar DEntry
        return DEntry;
    }
}