public class Test {

    public static void main(String[] args) {

        Encoder encoder = new Encoder("asdfhjkl;adfgsc".getBytes(), 16);
        byte[] encoded = encoder.encode(false);

        System.out.println("Codebook:");
        System.out.println("-----------------------------------------------------");
        for (var block : encoder.getSourceBlocks()) System.out.println(block);
        System.out.println("-----------------------------------------------------");
        System.out.println();

        double entropy = 0;
        for (var unique : encoder.getBlocks()) {
            entropy += unique.getProbability() * unique.getInformationContent();
        }

        System.out.println("Entropy: " + entropy + " bits");

    }

}
