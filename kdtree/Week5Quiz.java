/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

class IntervalNode implements Comparable<IntervalNode> {
    int[] array;
    int interval;

    IntervalNode(int[] array) {
        this.array = array;
        interval = 0;
        if (array.length == 1) {
            interval = 1;
        }
        else if (array.length == 2) {
            if (array[1] <= array[0]) throw new IllegalArgumentException();
            interval += array[1] - array[0];
        }
        else {
            for (int i = 1; i < array.length; i++) {
                if (array[i] <= array[i - 1]) throw new IllegalArgumentException();
                interval += array[i] - array[i - 1];
            }
        }
    }

    public int compareTo(IntervalNode o) {
        if (array.length != o.array.length) throw new IllegalArgumentException();
        return interval - o.interval;
    }

    public static void main(String[] args) {

    }
}

public class Week5Quiz {
    public static void main(String[] args) {
        StdOut.println();
        StdOut.println("test1");
        StdOut.println();
        test1(new String[] { "enim", "elementum", "lorem-ipsum.txt" });
        StdOut.println();
        StdOut.println("test3");
        StdOut.println();
    }

    private static void test1(String[] args) {
        // read data
        String[] query = new String[args.length - 1];
        System.arraycopy(args, 0, query, 0, args.length - 1);
        // initialize the data structures from file
        String filename = args[args.length - 1];
        String[] strings = null;
        In in = new In(filename);

        while (!in.isEmpty())
            strings = in.readAllStrings();

        // build
        RedBlackBST<String, LinkedList<Integer>> bst = new RedBlackBST<>();
        int index = 0;
        for (String s : strings) {
            LinkedList<Integer> list = bst.contains(s)
                                       ? bst.get(s)
                                       : new LinkedList<>();
            list.add(index);
            if (!bst.contains(s)) bst.put(s, list);
            index++;
        }

        int interval = 0;
        LinkedList<LinkedList<Integer>> lists = new LinkedList<LinkedList<Integer>>();
        for (String s : query) {
            if (!bst.contains(s)) {
                interval = -1;
                break;
            }

            lists.add(bst.get(s));
        }

        if (interval == -1) {
            StdOut.println("Query missed");
            return;
        }

        if (query.length == 1) {
            StdOut.println("Shortest interval: 1");
            return;
        }

        LinkedList<Integer> firstIndexes = lists.get(0);
        MinPQ<IntervalNode> minPQ = new MinPQ<>();

        for (int i = 0; i < firstIndexes.size(); i++) {
            int[] queryIndexes = new int[query.length];
            queryIndexes[0] = firstIndexes.get(i);
            int j = 1;
            boolean isValid = true;
            while (j < lists.size()) {
                LinkedList<Integer> indexes = lists.get(j);
                Integer[] indexesArray = indexes.toArray(new Integer[indexes.size()]);
                index = ceilSearch(indexesArray, queryIndexes[j - 1]);
                if (index == -1) {
                    isValid = false;
                    break;
                }
                queryIndexes[j++] = indexesArray[index];
            }
            if (isValid) minPQ.insert(new IntervalNode(queryIndexes));
        }

        StdOut.println("Shortest interval: " + minPQ.min().interval);
        StdOut.println("All intervals: " + minPQ.size());
    }

    static int ceilSearch(Integer arr[], int x) {
        return ceilSearch(arr, 0, arr.length - 1, x);
    }

    /*
        Function to get index of
        ceiling of x in arr[low..high]
    */
    static int ceilSearch(Integer arr[], int low, int high, int x) {
        int mid;

      /* If x is smaller than or equal to the
         first element, then return the first element */
        if (x <= arr[low])
            return low;

      /* If x is greater than the last
         element, then return -1 */
        if (x > arr[high])
            return -1;

      /* get the index of middle element
         of arr[low..high]*/
        mid = (low + high) / 2;  /* low + (high - low)/2 */

      /* If x is same as middle element,
         then return mid */
        if (arr[mid] == x)
            return mid;

      /* If x is greater than arr[mid], then
         either arr[mid + 1] is ceiling of x or
         ceiling lies in arr[mid+1...high] */
        else if (arr[mid] < x) {
            if (mid + 1 <= high && x <= arr[mid + 1])
                return mid + 1;
            else
                return ceilSearch(arr, mid + 1, high, x);
        }

      /* If x is smaller than arr[mid],
         then either arr[mid] is ceiling of x
         or ceiling lies in arr[mid-1...high] */
        else {
            if (mid - 1 >= low && x > arr[mid - 1])
                return mid;
            else
                return ceilSearch(arr, low, mid - 1, x);
        }
    }
}