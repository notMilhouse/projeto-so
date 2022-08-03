package src.application.commandparsing.command;

public class ExitCommand extends Command{
    @Override
    public void execute() {
        System.exit(0);
    }
}
