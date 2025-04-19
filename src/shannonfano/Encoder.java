package shannonfano;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Encoder {

    private final int BLOCK_SIZE = 16;
    private final int TOTAL_BLOCKS;
    private final String filepath;
    private final byte[] data;
    private final String dataBits;

    public Encoder(File file) throws IOException {
        this.filepath = file.getAbsolutePath();
        this.data = readFile(file);
        this.dataBits = bytesToBinaryString(this.data);
        this.TOTAL_BLOCKS = (int) Math.ceil((double) dataBits.length() / BLOCK_SIZE);
    }

    public File encode() throws IOException {
        List<Block> blocks = bitsToUniqueBlocks(dataBits);
        Collections.sort(blocks);
        divide(blocks, 0, blocks.size() - 1);

        for (Block block : blocks) System.out.println(block);

        StringBuilder sb = new StringBuilder();
        blocks.forEach(block -> sb.append(block.getCodeword()));
        String binaryString = sb.toString();

        byte[] compressedBytes = binaryStringToByteArray(binaryString);

        String[] fax = filepath.split("\\.");
        String outputPath = fax[0] + ".sf";
        File compressed = new File(outputPath);

        Files.write(compressed.toPath(), compressedBytes);
        return compressed;
    }

    private byte[] readFile(File file) throws IOException {
        return Files.readAllBytes(Path.of(file.getPath()));
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

    private ArrayList<Block> bitsToUniqueBlocks(String bits) {
        HashMap<String, Block> uniqueBlocks = new LinkedHashMap<>();
        char[] rawBlock = new char[BLOCK_SIZE];
        int index = 0;

        for (int i = 0; i < bits.length(); i++) {
            rawBlock[index++] = bits.charAt(i);
            if (index == BLOCK_SIZE) {
                updateUniqueBlocksMap(uniqueBlocks, rawBlock);
                index = 0;
                rawBlock = new char[BLOCK_SIZE];
            }
        }

        if (index > 0) {
            for (int i = index; i < BLOCK_SIZE; i++) rawBlock[i] = '0'; // padding
            updateUniqueBlocksMap(uniqueBlocks, rawBlock);
        }
        return new ArrayList<>(uniqueBlocks.values());
    }

    private void updateUniqueBlocksMap(HashMap<String, Block> uniqueBlocks, char[] rawBlock) {
        String blockData = new String(rawBlock);
        if (uniqueBlocks.containsKey(blockData)) {
            uniqueBlocks.get(blockData).incFrequency(TOTAL_BLOCKS);
        } else {
            Block block = new Block(blockData);
            block.incFrequency(TOTAL_BLOCKS);
            uniqueBlocks.put(blockData, block);
        }
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

}

class Block implements Comparable<Block> {

    private final String data;
    private int frequency = 0;
    private double probability = 0;
    private StringBuilder codeword = new StringBuilder();

    public Block(String data) {
        this.data = data;
    }

    public void incFrequency(int totalBlocks) {
        this.frequency++;
        this.probability = (double) frequency / totalBlocks;
    }

    public void appendZero() {
        codeword.append('0');
    }

    public void appendOne() {
        codeword.append('1');
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Block block = (Block) obj;
        return Objects.equals(data, block.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public int compareTo(Block block) {
        if (this.probability ==  block.probability) return 0;
        return this.probability > block.probability ? -1 : 1;
    }

    @Override
    public String toString() {
        return String.format(
                "Block: %-16s | Freq: %4d | Prob: %7.6f | Codeword: %s",
                data, frequency, probability, codeword
        );
    }

    public double getProbability() {
        return probability;
    }

    public String getCodeword() {
        return codeword.toString();
    }

}