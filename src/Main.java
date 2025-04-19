import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File("/home/solo/Dropbox/Elements-of-Information-Theory.pdf");

        byte[] data = Files.readAllBytes(file.toPath());
        Encoder encoder = new Encoder(data, 16);
        byte[] result = encoder.encode();

        System.out.println("Original: " + data.length + " bytes");
        System.out.println("Encoded: " + result.length + " bytes");

        // System.out.println(new String(data));
        // System.out.println(new String(result));



    }

}
