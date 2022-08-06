package src.domain.bitmap.exceptions;

public class BitMapPositionAlreadyUnsetException extends BitMapException{
    public BitMapPositionAlreadyUnsetException() {
        super("A posicao ja esta desalocada");
    }
}
