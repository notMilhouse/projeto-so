package test;

import src.domain.bitmap.BitMap;
import src.domain.bitmap.BitMapException;

public class BitMapTest {
    public static void main(String[] args) {
        BitMap teste = new BitMap(32);

        System.out.println(teste);

        try {
            teste.allocateSlot();

            System.out.println(teste);

            teste.allocateSlot();

            System.out.println(teste);

            teste.allocateSlot();

            System.out.println(teste);

            teste.allocateSlot();

            System.out.println(teste);

            teste.freeSlot(1);

            System.out.println(teste);

            teste.freeSlot(3);

            System.out.println(teste);

            teste.allocateSlot();

            System.out.println(teste);
        } catch (BitMapException ex) {
            System.err.println(ex.getMessage());
        }

        System.out.println("----");




        teste = new BitMap("10011101 00011101 00011101 00011101");

        System.out.println(teste);


    }
}
