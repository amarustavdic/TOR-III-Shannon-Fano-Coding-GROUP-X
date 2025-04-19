import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {

//        File uncompressedTxtFile = new File("resources/example.txt");
//        Encoder encoder = new Encoder(uncompressedTxtFile);
//        File compressedTxtFile = encoder.encode();
//
//        size(uncompressedTxtFile, compressedTxtFile);



        Encoder encoder = new Encoder(16);
        encoder.encode("asdf asdf asdf asdf".getBytes());











    }

    public static void size(File original, File compressed) throws IOException {
        byte[] a = Files.readAllBytes(Path.of(original.toURI()));
        byte[] b = Files.readAllBytes(Path.of(compressed.toURI()));
        System.out.println("original: " + a.length + " bytes");
        System.out.println("compressed: " + b.length + " bytes");
    }

}
