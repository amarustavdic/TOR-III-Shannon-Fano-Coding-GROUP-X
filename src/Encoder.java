import java.util.HashMap;
import java.util.LinkedList;

public class Encoder {

    private final int blockSize;
    private final HashMap<String, Integer> frequencies;
    private final HashMap<String, Double> probabilities;

    public Encoder(int blockSize) {
        this.blockSize = blockSize;
        this.frequencies = new HashMap<>();
        this.probabilities = new HashMap<>();
    }

    public byte[] encode(byte[] data) {
        String bits = bytesToBinaryString(data);
        LinkedList<Block> blocks = bitsToBlocks(bits);
        estimateProbabilities(blocks.size());

        for (Block block : blocks) System.out.println(block);
        System.out.println(frequencies);
        System.out.println(probabilities);

        return null;
    }

    private String byteToBinaryString(byte data) {
        char[] binary = new char[8];
        for (int i = 7; i >= 0; i--) binary[7 - i] = ((data >> i) & 1) == 1 ? '1' : '0';
        return new String(binary);
    }

    private String bytesToBinaryString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte entry : data) sb.append(byteToBinaryString(entry));
        return sb.toString();
    }

    /**
     * Coverts stream of bits in list of blocks, and in the meantime,
     * it keeps track of frequencies, for the blocks in a global map.
     * @param dataBits data bit stream, provided as string.
     * @return list of blocks, data is in same order as in the input.
     */
    private LinkedList<Block> bitsToBlocks(String dataBits) {
        LinkedList<Block> blocks = new LinkedList<>();
        char[] blockBits = new char[blockSize];
        for (int i = 0, j = 0; j < dataBits.length(); i++, j++) {
            if (i > blockSize - 1) {
                String bb = new String(blockBits);
                frequencies.compute(bb, (k, v) -> v == null ? 1 : v + 1);
                blocks.add(new Block(bb));
                blockBits = new char[blockSize];
                i = 0;
            }
            blockBits[i] = dataBits.charAt(j);
        }
        return blocks;
    }

    private void estimateProbabilities(int totalBlocks) {
        for (var entry : frequencies.entrySet()) {
            probabilities.put(entry.getKey(), entry.getValue() / (double) totalBlocks);
        }
    }

    class Block {
        private final String blockBits;

        public Block(String blockBits) {
            this.blockBits = blockBits;
        }

        @Override
        public String toString() {
            return blockBits;
        }

    }

}
