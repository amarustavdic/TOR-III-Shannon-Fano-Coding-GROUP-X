public class Block implements Comparable<Block> {

    private final int size;
    private final String bits;
    private int frequency = 0;
    private double probability = 0;
    private double informationContent = 0;
    private final StringBuilder codeword = new StringBuilder();

    public Block(String bits, int size) {
        this.bits = bits;
        this.size = size;
    }

    /**
     * Updates frequency, probability.
     *
     * @param n Total number of blocks in source.
     */
    public void update(int n) {
        this.frequency++;
        this.probability = frequency / (double) n;
        this.informationContent = - (Math.log(probability) / Math.log(2));
    }

    public void appendZero() {
        this.codeword.append('0');
    }

    public void appendOne() {
        this.codeword.append('1');
    }

    @Override
    public int compareTo(Block block) {
        if (this.probability ==  block.probability) return 0;
        return this.probability > block.probability ? -1 : 1;
    }

    @Override
    public String toString() {
        return String.format(
                "%-" + size + "s | f: %4d | p: %7.6f | w: %s",
                bits, frequency, probability, codeword
        );
    }

    public double getProbability() {
        return probability;
    }

    public double getInformationContent() {
        return informationContent;
    }

    public String getCodeword() {
        return codeword.toString();
    }

    public byte[] getCodebookEntry() {
        int blockLenBits = bits.length();
        int codewordLenBits = codeword.length();
        int blockBytes = (int) Math.ceil(blockLenBits / 8.0);
        int codewordBytes = (int) Math.ceil(codewordLenBits / 8.0);

        int totalSize = 1 + blockBytes + 1 + codewordBytes;
        byte[] entry = new byte[totalSize];
        int i = 0;
        entry[i++] = (byte) blockLenBits;

        // Write block bits
        byte[] blockPacked = new Bits(bits).getBytes();
        System.arraycopy(blockPacked, 0, entry, i, blockBytes);
        i += blockBytes;

        // Write codeword bits length
        entry[i++] = (byte) codewordLenBits;

        // Write codeword bits
        byte[] codewordPacked = new Bits(codeword.toString()).getBytes();
        System.arraycopy(codewordPacked, 0, entry, i, codewordBytes);

        return entry;
    }

}
