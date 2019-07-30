/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 19/07/2019
 *  Description: The client program, that takes an integer k as a command-line argument.
 *  It reads a sequence of strings from standard input using StdIn.readString()
 *  and prints exactly k of them, uniformly at random.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int readsCount = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty() && readsCount-- > 0) {
            String s = StdIn.readString();
            queue.enqueue(s);
        }

        for (String s : queue) {
            StdOut.println(s);
        }
    }
}
