import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Bits implements Iterable<Integer> {

    private final int size;
    private final byte[] data;

    public Bits(byte[] data) {
        this.data = data.clone();
        this.size = this.data.length * 8;
    }

    public Bits(String bits) {
        this.size = (int) Math.ceil((double) bits.length() / 8);
        this.data = new byte[size];

        for (int i = 0; i < size; i++) {
            int start = i * 8;
            int end = Math.min(start + 8, bits.length());
            String byteString = bits.substring(start, end);
            if (byteString.length() < 8) byteString = String.format("%-8s", byteString).replace(' ', '0');
            this.data[i] = (byte) Integer.parseInt(byteString, 2);
        }
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

    @Override
    public void forEach(Consumer<? super Integer> action) {
        iterator().forEachRemaining(action);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return Spliterators.spliterator(iterator(), size, Spliterator.ORDERED | Spliterator.SIZED | Spliterator.NONNULL);
    }

    public Stream<Integer> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer integer : this) sb.append(integer);
        return sb.toString();
    }

    public int size() {
        return this.size;
    }

    public byte[] getBytes() {
        return this.data;
    }

}
