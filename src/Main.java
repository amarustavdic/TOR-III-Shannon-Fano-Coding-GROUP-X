import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) throw new IllegalArgumentException("Please provide a file name");
        String inputFileName = args[0];
        String outputFileName = (args.length > 1) ? args[1] : "compressed.sf";

        File file = new File(inputFileName);
        byte[] data = Files.readAllBytes(file.toPath());
        EncoderOld encoderOld = new EncoderOld(data, 16);
        byte[] result = encoderOld.encode();

        System.out.println("Original: " + data.length + " bytes");
        System.out.println("-----------------------------------------");
        System.out.println("Codebook Size: " + encoderOld.getCodebookSize() + " bytes");
        System.out.println("Compressed Src: " + (result.length - encoderOld.getCodebookSize()) + " bytes");
        System.out.println("Combined Compressed: " + result.length + " bytes");
        System.out.println("-----------------------------------------");
        System.out.println("Diff.: " + (data.length - result.length) + " bytes");

        Files.write(Paths.get(outputFileName), result);

    }

}
