import java.util.Iterator;

public class Decoder {

    public static void main(String[] args) {

        BitStream bs = new BitStream("Some sample text.".getBytes());
        Iterator<Integer> it = bs.iterator();

        while (it.hasNext()) System.out.println(it.next());


    }

}

