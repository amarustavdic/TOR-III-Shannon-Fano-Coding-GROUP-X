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

    public int size() {
        return this.size;
    }

}
