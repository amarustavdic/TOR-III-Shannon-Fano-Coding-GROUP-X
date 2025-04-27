import java.util.HashMap;

public class Decoder {

    private final HashMap<String, String> map = new HashMap<>();

    public Decoder(byte[] data) {

        // first 4 bytes (size of codebook, how many entries) N
        // 1 byte (number of bytes needed for original (decoded) word) D
        // 1 byte (number of bytes needed for encoded word) E
        // N * D * E bytes (codebook)
        // the rest of the bytes is encoded source data



    }




}

