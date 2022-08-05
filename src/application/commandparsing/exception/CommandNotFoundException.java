package src.application.commandparsing.exception;

public class CommandNotFoundException extends Exception{
    public CommandNotFoundException() {
        super("Esse comando não existe!");
    }
}
