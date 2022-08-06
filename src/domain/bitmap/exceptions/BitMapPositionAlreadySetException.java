package src.domain.bitmap.exceptions;

public class BitMapPositionAlreadySetException extends BitMapException{

    public BitMapPositionAlreadySetException() {
        super("A posicao ja esta alocada");
    }
}
