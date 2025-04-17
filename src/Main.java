public class Main {

    public static void main(String[] args) {

        // For now using simple string as data, but ideally uncompressed should be obtained from file
        byte[] uncompressed = "Hello there, it is nice to say hello, you know hello there!".getBytes();
        var compressor = new ShannonFanoCompressor(uncompressed);
        byte[] compressed = compressor.compress();

        compressor.log();
        System.out.println();
        System.out.println("Uncompressed: " + uncompressed.length + " bytes");
        System.out.println("Compressed: " + compressed.length + " bytes");

    }

}
