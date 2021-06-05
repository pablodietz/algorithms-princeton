/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date: 10/04/2020
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> reservoir = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);
        int i = 0;
        for (; i < k; i++) reservoir.enqueue(StdIn.readString());
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (StdRandom.uniform(++i) < k) {
                reservoir.dequeue();
                reservoir.enqueue(item);
            }
        }
        for (String item : reservoir) StdOut.println(item);
    }
}
