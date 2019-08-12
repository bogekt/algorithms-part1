/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public class Combinations {
    public static void main(String[] args) {

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
