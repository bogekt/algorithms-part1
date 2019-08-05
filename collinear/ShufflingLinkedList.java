/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.LinkedList;

public class ShufflingLinkedList {
    public static void main(String[] args) {
        int n = 10;
        LinkedList<Integer> list = new LinkedList<Integer>();

        for (int i = 0; i < n; i++)
            list.add(i);

        shuffle1(list);

        for (int i : list)
            StdOut.print(i + " ");
    }

    private static <T> void shuffle1(LinkedList<T> list) {
        if (list.isEmpty() || list.size() == 1) return;

        LinkedList<T> list1 = new LinkedList<>();
        LinkedList<T> list2 = new LinkedList<>();

        while (!list.isEmpty()) {
            list1.add(list.remove());

            if (list.isEmpty()) break;

            list2.add(list.remove());
        }

        shuffle1(list1);
        shuffle1(list2);

        while (!list1.isEmpty() || !list2.isEmpty()) {
            if (list1.isEmpty()) list.add(list2.remove());
            else if (list2.isEmpty()) list.add(list1.remove());
            else list.add(
                        StdRandom.uniform(2) == 1
                        ? list1.remove()
                        : list2.remove()
                );
        }
    }
}
