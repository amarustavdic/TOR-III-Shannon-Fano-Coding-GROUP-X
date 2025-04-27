import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

//        if (args.length < 1) throw new IllegalArgumentException("Please provide a file name");
//        String inputFileName = args[0];
//        String outputFileName = (args.length > 1) ? args[1] : "compressed.sf";

        File file = new File("resources/example.txt");
        byte[] data = Files.readAllBytes(file.toPath());
        Encoder encoder = new Encoder(data, 16);
        byte[] result = encoder.encode(true);

        System.out.println("Original: " + data.length + " bytes");
        System.out.println("-----------------------------------------");
        System.out.println("Compressed Src: " + (result.length - data.length) + " bytes");
        System.out.println("Combined Compressed: " + result.length + " bytes");
        System.out.println("-----------------------------------------");
        System.out.println("Diff.: " + (data.length - result.length) + " bytes");

        Files.write(Paths.get("resources/compressed.sf"), result);

        System.out.println();
        double entropy = 0;
        for (var unique : encoder.getBlocks()) {
            entropy += unique.getProbability() * unique.getInformationContent();
        }

        System.out.println("Entropy: " + entropy + " bits");


        System.out.println();
        System.out.println("Codebook:");
        for (var block : encoder.getBlocks()) System.out.println(block);

    }

}
