package model;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SNode {
    private static byte[] bitMap;

    private FileType fileType;
    private Byte generation;
    private Long creationDate;
    private Long modificationDate;
    private Short length;

    private DataBlockReference dataBlock_01;
    private DataBlockReference dataBlock_02;
    private DataBlockReference dataBlock_03;
    private DataBlockReference dataBlock_04;

    public static void setBitMap(int N) {
        bitMap = new byte[N/8];
    }

    public SNode(
        byte fileType,
        short length,
        DataBlockReference dataBlock
    ) {
        this.fileType = FileType.parseFileType(fileType);
        
        generation = 0;
        creationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        modificationDate = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli();
        
        this.length = new Short(length);

        dataBlock_01 = dataBlock; //tem q achar esse id ai
        dataBlock_02 = null;
        dataBlock_03 = null;
        dataBlocaak_04 = null;
    }

    public void updateSNode(SNodeOperation operation){
        operation.attempt();
    }

    //todo serializable e blablabla fazer disso uma interface pra todos os modelos

    interface SNodeOperation{
        public SNode attempt();
    }

    class SNodeAddFileOperation implements SNodeOperation {
        @Override
        public SNode attempt() {

        }
    }

    class SNodeDeleteFileOperation implements SNodeOperation {
        @Override
        public SNode attempt() {

        }
    }

    class SNodeAddDirectoryOperation implements SNodeOperation {
        @Override
        public SNode attempt() {

        }
    }

    class SNodeDeleteDirectoryOperation implements SNodeOperation {
        @Override
        public SNode attempt() {

        }
    }

}
