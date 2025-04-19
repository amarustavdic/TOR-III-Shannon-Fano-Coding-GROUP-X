import java.util.LinkedList;

public class Encoder {

    private final int blockSize;

    public Encoder(int blockSize) {
        this.blockSize = blockSize;
    }

    public byte[] encode(byte[] data) {
        String bits = bytesToBinaryString(data);
        LinkedList<Block> blocks = bitsToBlocks(bits);

        for (Block block : blocks) System.out.println(block);

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

    private LinkedList<Block> bitsToBlocks(String dataBits) {
        LinkedList<Block> blocks = new LinkedList<>();
        char[] blockBits = new char[blockSize];
        for (int i = 0, j = 0; j < dataBits.length(); i++, j++) {
            if (i > blockSize - 1) {
                blocks.add(new Block(new String(blockBits)));
                blockBits = new char[blockSize];
                i = 0;
            }
            blockBits[i] = dataBits.charAt(j);
        }
        return blocks;
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
