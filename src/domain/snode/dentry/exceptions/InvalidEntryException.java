package src.domain.snode.dentry.exceptions;
public class InvalidEntryException extends Exception { 
    public InvalidEntryException(String errorMessage) {
        super(errorMessage);
    }

    public InvalidEntryException() {
        super("invalid entry");
    }
}