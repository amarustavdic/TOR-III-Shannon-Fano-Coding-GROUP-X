import java.util.*;

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

    public byte[] encode() {
        shannonFano(unique, 0, unique.size() - 1);

        // TODO

        return null;
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

}
