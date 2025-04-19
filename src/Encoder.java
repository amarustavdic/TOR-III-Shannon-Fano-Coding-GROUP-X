import java.util.*;

public class Encoder {

    private final byte[] data;
    private final int blockSize;
    private final int totalBlocks;
    private final HashMap<String, Block> uniqueBlocks;

    public Encoder(byte[] data, int blockSize) {
        this.data = data;
        this.blockSize = blockSize;
        this.totalBlocks = (int) Math.ceil(data.length * 8 / (double) blockSize);
        this.uniqueBlocks = new HashMap<>();
    }

    public byte[] encode() {
        String bits = bytesToBinaryString(data);
        LinkedList<Block> blocks = bitsToBlocks(bits);
        ArrayList<Block> unique = new ArrayList<>(uniqueBlocks.values());
        Collections.sort(unique);
        divide(unique, 0, unique.size() - 1);

        double t = 0;
        for (var u : unique) {
            System.out.println(u);
            t += u.probability;
        }

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
        Arrays.fill(blockBits, '0');
        for (int i = 0, j = 0; j < dataBits.length(); i++, j++) {
            if (i > blockSize - 1) {
                Block block = uniqueBlocks.computeIfAbsent(new String(blockBits), Block::new);
                block.incFrequency();
                blocks.add(block);
                blockBits = new char[blockSize];
                Arrays.fill(blockBits, '0');
                i = 0;
            }
            blockBits[i] = dataBits.charAt(j);
        }
        Block block = uniqueBlocks.computeIfAbsent(new String(blockBits), Block::new);
        block.incFrequency();
        blocks.add(block);
        return blocks;
    }

    private void divide(List<Block> blocks, int start, int end) {
        if (start >= end) return;
        double total = 0;
        for (int i = start; i <= end; i++) total += blocks.get(i).probability;

        double halfTotal = total / 2, sum = 0;
        int split = start;
        while (split < end && sum + blocks.get(split).probability <= halfTotal) {
            sum += blocks.get(split++).probability;
        }

        if (split == start || split == end) split = (start + end) / 2;

        for (int i = start; i <= split; i++) blocks.get(i).appendZero();
        for (int i = split + 1; i <= end; i++) blocks.get(i).appendOne();

        divide(blocks, start, split);
        divide(blocks, split + 1, end);
    }

    class Block implements Comparable<Block> {
        private final String blockBits;
        private int frequency = 0;
        private double probability = 0;
        private final StringBuilder codeword = new StringBuilder();

        public Block(String blockBits) {
            this.blockBits = blockBits;
        }

        public void incFrequency() {
            frequency++;
            probability = frequency / (double) totalBlocks;
        }

        public void appendZero() {
            codeword.append('0');
        }

        public void appendOne() {
            codeword.append('1');
        }

        @Override
        public String toString() {
            return String.format(
                    "%-" + blockSize + "s | f: %4d | p: %7.6f | w: %s",
                    blockBits, frequency, probability, codeword
            );
        }

        @Override
        public int compareTo(Block block) {
            if (this.probability ==  block.probability) return 0;
            return this.probability > block.probability ? -1 : 1;
        }

    }

}
