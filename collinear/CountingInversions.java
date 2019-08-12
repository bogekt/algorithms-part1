/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountingInversions {
    private final static List<int[]> TestCombinations = new ArrayList<>();

    public static void main(String[] args) {
        // Integer[] a = new Integer[] { 1, 2, 3 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4, 5 };
        Integer[] a = new Integer[] { 1, 2, 3, 4, 5, 6 };
        int n = a.length;
        int k = 2;
        int inversions = countInversions(a);
        System.out.printf(
                "inversions in array %d is %d ",
                a.length,
                inversions
        );

        System.out.println("test combinations");

        for (int[] combination : TestCombinations) {
            System.out.println(Arrays.toString(combination));
        }
        TestCombinations.clear();

        System.out.printf(
                "generated %d test combinations of %d items from %d ",
                TestCombinations.size(),
                2,
                a.length
        );

        List<int[]> combinations = Combinations.generate(n, k);

        System.out.println("expected combinations");

        for (int[] combination : combinations) {
            System.out.println(Arrays.toString(combination));
        }

        System.out.printf(
                "generated %d combinations of %d items from %d ",
                combinations.size(),
                k,
                n
        );
    }

    private static int countInversions(Comparable[] a) {
        return countInversions(a, 0, 0, a.length);
    }

    private static int countInversions(Comparable[] a, int inversions, int lo, int hi) {
        if (hi - lo <= 1) return inversions;

        int mid = lo + (hi - lo) / 2;

        inversions += countInversions(a, inversions, lo, mid);
        inversions += countInversions(a, inversions, mid + 1, hi);

        for (int i = lo; i <= mid; i++)
            for (int j = mid; j < hi; j++) {
                if (i == j) continue;
                TestCombinations.add(new int[] { i, j });
                if (a[i].compareTo(a[j]) > 0) inversions++;
            }

        return inversions;
    }
}
