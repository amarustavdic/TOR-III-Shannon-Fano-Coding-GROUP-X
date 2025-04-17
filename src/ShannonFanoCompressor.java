import java.util.*;

public class ShannonFanoCompressor {

    private final int totalBlocks;
    private final byte[] data;
    private ArrayList<Block> blocks = new ArrayList<>();

    public ShannonFanoCompressor(byte[] data) {
        this.data = data;
        this.totalBlocks = data.length / 2;
    }

    public byte[] compress() {
        blocks = getBlocks(); // Split into 16 bit blocks
        Collections.sort(blocks);

        // Perform Shannon-Fano division and appending recursively
        divide(blocks, 0, blocks.size()-1);

        // Collect all codewords, and return result of compression
        StringBuilder sb = new StringBuilder();
        blocks.forEach(block -> sb.append(block.getCodeword()));

        // Covert codewords string to bytes array
        return binaryStringToByteArray(sb.toString());
    }

    private ArrayList<Block> getBlocks() {
        Map<String, Integer> frequencyMap = new HashMap<>();
        int i = 0;
        while (i < data.length) {
            String fst = byteToBinaryString(data[i++]);
            String snd = (i < data.length) ? byteToBinaryString(data[i++]) : "00000000";
            String blockBits = fst + snd;
            frequencyMap.put(blockBits, frequencyMap.getOrDefault(blockBits, 0) + 1);
        }

        // Using obtained frequencies to estimate probabilities
        // TODO: This step might not be necessary, it waste of time
        ArrayList<Block> blocks = new ArrayList<>();
        frequencyMap.forEach((key, value) -> {
            blocks.add(new Block(key, (double) value / totalBlocks));
        });
        return blocks;
    }

    private void divide(List<Block> blocks, int start, int end) {
        if (start >= end) return;
        double total = 0;
        for (int i = start; i <= end; i++) total += blocks.get(i).getProbability();

        double halfTotal = total / 2, sum = 0;
        int split = start;
        while (split < end && sum + blocks.get(split).getProbability() <= halfTotal) {
            sum += blocks.get(split++).getProbability();
        }

        if (split == start || split == end) split = (start + end) / 2;

        for (int i = start; i <= split; i++) blocks.get(i).appendZero();
        for (int i = split + 1; i <= end; i++) blocks.get(i).appendOne();

        divide(blocks, start, split);
        divide(blocks, split + 1, end);
    }

    /**
     * Utility method helps to convert bytes to binary string representation efficiently.
     * @param data Single byte of data.
     * @return String of bits, obtained from the given byte.
     */
    private String byteToBinaryString(byte data) {
        char[] binary = new char[8];
        for (int i = 7; i >= 0; i--) binary[7 - i] = ((data >> i) & 1) == 1 ? '1' : '0';
        return new String(binary);
    }

    private byte[] binaryStringToByteArray(String binary) {
        int length = binary.length();
        int byteLength = (length + 7) / 8;
        byte[] byteArray = new byte[byteLength];

        for (int i = 0; i < byteLength; i++) {
            int start = i * 8;
            int end = Math.min(start + 8, length);
            String byteString = binary.substring(start, end);
            if (byteString.length() < 8) byteString = String.format("%-8s", byteString).replace(' ', '0');
            byteArray[i] = (byte) Integer.parseInt(byteString, 2);
        }
        return byteArray;
    }

    public void log() {
        blocks.forEach(System.out::println);
    }

}


class Block implements Comparable<Block> {
    private final String bits;
    private final double probability;
    private final StringBuilder codeword = new StringBuilder();

    public Block(String bits, double probability) {
        this.bits = bits;
        this.probability = probability;
    }

    @Override
    public String toString() {
        return String.format("B[ %-16s ]  P[ %-10.6f ]  W[ %-10s ]", bits, probability, codeword);
    }

    @Override
    public int compareTo(Block block) {
        if (this.probability ==  block.probability) return 0;
        return this.probability > block.probability ? -1 : 1;
    }

    public double getProbability() {
        return probability;
    }

    public void appendZero() {
        codeword.append('0');
    }

    public void appendOne() {
        codeword.append('1');
    }

    public String getCodeword() {
        return codeword.toString();
    }

}
