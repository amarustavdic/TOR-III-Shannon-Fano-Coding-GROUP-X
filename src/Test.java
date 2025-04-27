import java.util.Iterator;

public class Test {

    public static void main(String[] args) {

        Bits bits = new Bits("asdf asdf".getBytes());
        Iterator<Integer> bi = bits.iterator();

        StringBuilder blockBits = new StringBuilder(16);
        while (bi.hasNext()) {
            blockBits.append(bi.next() == 1 ? '1' : '0');

            if (blockBits.length() == 16) {
                System.out.println(blockBits);
                blockBits.setLength(0);
            }
        }

        if (!blockBits.isEmpty()) System.out.println(blockBits);
    }

}
