public class Test {

    public static void main(String[] args) {

        Encoder encoder = new Encoder("asdf asdf".getBytes(), 16);
        encoder.encode();

        for (var block : encoder.getBlocks()) {
            System.out.println(block);
        }


    }

}
