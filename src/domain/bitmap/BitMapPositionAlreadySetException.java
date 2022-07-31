package src.domain.bitmap;

public class BitMapPositionAlreadySetException extends BitMapException{

    public BitMapPositionAlreadySetException() {
        super("A posicao ja esta alocada");
    }
}
