import java.util.Arrays;

public class Test {

    public static void main(String[] args) {

        Encoder encoder = new Encoder("asdf asdf".getBytes(), 16);
        byte[] encoded = encoder.encode(false);

        for (var block : encoder.getSourceBlocks()) {
            System.out.println(block);
        }

        System.out.println(Arrays.toString(encoded));

    }

}
