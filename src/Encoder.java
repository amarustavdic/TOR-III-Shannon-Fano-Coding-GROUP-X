import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Encoder {

    private final int n; // Total number of blocks
    private final HashMap<String, Block> map = new HashMap<>();
    private final LinkedList<Block> blocks = new LinkedList<>();
    private final ArrayList<Block> unique;

    public Encoder(byte[] data, int blockSize) {
        Bits dataBits = new Bits(data);
        this.n = (int) Math.ceil((double) dataBits.size() / blockSize);

        // Obtain sequence of blocks and track unique blocks
        Iterator<Integer> dbi = dataBits.iterator();
        StringBuilder sb = new StringBuilder(blockSize);
        while (dbi.hasNext()) {
            sb.append(dbi.next() == 1 ? '1' : '0');

            if (sb.length() == 16) {
                Block block = map.computeIfAbsent(sb.toString(), b -> new Block(sb.toString(), 16));
                block.update(n);
                blocks.add(block);
                sb.setLength(0);
            }
        }

        if (!sb.isEmpty()) {
            Block block = map.computeIfAbsent(sb.toString(), b -> new Block(sb.toString(), 16));
            block.update(n);
            blocks.add(block);
        }

        // Unique blocks have to be sorted based on probabilities
        this.unique = new ArrayList<>(map.values());
        Collections.sort(this.unique);

    }

    public byte[] encode(boolean codebook) throws IOException {
        // Most important part of the program
        shannonFano(unique, 0, unique.size() - 1);

        // Combine codewords from source blocks (preserve order)
        String encodedSource = blocks.stream().map(Block::getCodeword).collect(Collectors.joining());
        Bits encodedBits = new Bits(encodedSource);

        // Return just encoded data, exclude codebook
        if (!codebook) return encodedBits.getBytes();

        // Otherwise, include codebook, to be able to decode it too
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Write codebook header (number of entries)
        out.write((unique.size() >> 24) & 0xFF);
        out.write((unique.size() >> 16) & 0xFF);
        out.write((unique.size() >> 8) & 0xFF);
        out.write(unique.size() & 0xFF);

        // Write each codebook entry
        for (Block block : unique) {
            byte[] entry = block.getCodebookEntry();
            out.write(entry);
        }
        out.write(encodedBits.getBytes());
        return out.toByteArray();
    }

    private void shannonFano(List<Block> blocks, int start, int end) {
        if (start >= end) return;
        double halfTotal = blocks.subList(start, end + 1).stream().mapToDouble(Block::getProbability).sum() / 2;
        double sum = 0;
        int split = start;

        // Find split point where the cumulative probability is closest to halfTotal
        while (split < end && sum + blocks.get(split).getProbability() <= halfTotal) {
            sum += blocks.get(split++).getProbability();
        }

        if (split == start || split == end) split = (start + end) / 2;

        for (int i = start; i <= split; i++) blocks.get(i).appendZero();
        for (int i = split + 1; i <= end; i++) blocks.get(i).appendOne();

        shannonFano(blocks, start, split);
        shannonFano(blocks, split + 1, end);
    }

    /**
     * Returns a sorted list of unique blocks, ordered by probability.
     *
     * @return List of unique blocks.
     */
    public ArrayList<Block> getBlocks() {
        return unique;
    }

    /**
     * Returns a list of blocks in the same order as they appear in the source data.
     *
     * @return A LinkedList containing the blocks in the same order as the source data.
     */
    public LinkedList<Block> getSourceBlocks() {
        return blocks;
    }

}
