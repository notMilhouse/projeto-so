package src.domain.bitmap.exceptions;

public class BitMapNextFitNotFoundException extends BitMapException{
    public BitMapNextFitNotFoundException() {
        super("Nao ha mais espaco no mapa de bits");
    }
}
