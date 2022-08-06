package src.adapter.driver.exceptions;
public class VirtualFileNotFoundException extends Exception { 
    public VirtualFileNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public VirtualFileNotFoundException() {
        super("Virtual file not found");
    }
}