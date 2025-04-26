import java.util.Iterator;
import java.util.NoSuchElementException;

public class BitStream implements Iterable<Integer> {

    private final byte[] data;

    public BitStream(byte[] data) {
        this.data = data;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {

            private int position = 0;
            private int mask = 0b10000000;

            @Override
            public boolean hasNext() {
                return position < data.length && mask != 0;
            }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException("No more bits available.");
                int bit = (data[position] & mask) == 0 ? 0 : 1;
                mask >>= 1;

                if (mask == 0) {
                    mask = 0b10000000;
                    position++;
                }
                return bit;
            }
        };
    }

}
