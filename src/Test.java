import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class Test {

    public static void main(String[] args) {

        HashMap<String, Block> uniqueBlocks = new HashMap<>();
        LinkedList<Block> blocks = new LinkedList<>();

        Bits bits = new Bits("asdf asdf".getBytes());
        Iterator<Integer> bi = bits.iterator();

        int n = (int) Math.ceil((double) bits.size() / 16);
        StringBuilder blockBits = new StringBuilder(16);
        while (bi.hasNext()) {
            blockBits.append(bi.next() == 1 ? '1' : '0');

            if (blockBits.length() == 16) {
                Block block = uniqueBlocks.computeIfAbsent(
                        blockBits.toString(),
                        b -> new Block(blockBits.toString(), 16)
                );
                block.update(n);
                blocks.add(block);
                blockBits.setLength(0);
            }
        }

        if (!blockBits.isEmpty()) {
            Block block = uniqueBlocks.computeIfAbsent(
                    blockBits.toString(),
                    b -> new Block(blockBits.toString(), 16)
            );
            block.update(n);
            blocks.add(block);
        }

        // Printing out
        for (Block block : blocks) {
            System.out.println(block);
        }


    }

}
