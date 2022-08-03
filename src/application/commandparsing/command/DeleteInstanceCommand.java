package src.application.commandparsing.command;

import src.domain.snode.FileType;

public class DeleteInstanceCommand extends Command{

    public DeleteInstanceCommand(
        String filePath,
        String fileName
    ) {
        this.filePath = filePath;
        this.fileName = fileName;
        fileType = FileType.Unknown;
        fileLength = 0;
    }
    @Override
    public void execute() {
        //TODO logica de deletar, delegando pro objeto de dominio o delete pra evitar redundancia caso seja diferente pra file e dir
        System.out.println("deletaria uma instancia");
        System.out.println(super.toString());
    }
}
