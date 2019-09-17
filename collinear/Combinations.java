/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public class Combinations {
    public static void main(String[] args) {
        // Integer[] a = new Integer[] { 1, 2, 3 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4, 5 };
        Integer[] a = new Integer[] { 1, 2, 3, 4, 5, 6 };
        int n = a.length;
        int k = 4;

        List<int[]> combinations = Combinations.generate(n, k);

        for (int[] combination : combinations) {
            for (int i : combination)
                System.out.print(i + 1);
            System.out.println();
        }

        System.out.printf(
                "generated %d combinations of %d items from %d ",
                combinations.size(),
                k,
                n
        );
    }


    public static List<int[]> generate(int n, int k) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[k], 0, n - 1, 0);

        return combinations;
    }


    private static void helper(List<int[]> combinations, int data[], int start, int end,
                               int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        }
        else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }
}
