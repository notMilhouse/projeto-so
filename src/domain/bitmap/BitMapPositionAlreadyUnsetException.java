package src.domain.bitmap;

public class BitMapPositionAlreadyUnsetException extends BitMapException{
    public BitMapPositionAlreadyUnsetException() {
        super("A posicao ja esta desalocada");
    }
}
