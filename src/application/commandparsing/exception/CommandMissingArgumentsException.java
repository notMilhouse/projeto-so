package src.application.commandparsing.exception;

public class CommandMissingArgumentsException extends Exception{
    public CommandMissingArgumentsException() {
        super("Argumentos faltando para o comando dado");
    }
}
