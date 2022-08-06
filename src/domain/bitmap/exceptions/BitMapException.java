package src.domain.bitmap.exceptions;

public class BitMapException extends Exception {
    BitMapException() {
        super("Algo deu errado no bitmap");
    }

    BitMapException(String message) {
        super(message);
    }
}
