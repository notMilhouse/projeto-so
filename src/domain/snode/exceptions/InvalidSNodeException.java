package src.domain.snode.exceptions;
public class InvalidSNodeException extends Exception { 
    public InvalidSNodeException(String errorMessage) {
        super(errorMessage);
    }
}