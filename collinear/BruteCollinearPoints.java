/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private static final Comparator<Point> naturalOrder = new PointNaturalOrderComparator();
    private static final LineSegment[] EmptyLineSegments = new LineSegment[0];
    private static final int LineSegmentCount = 4;

    private int numberOfSegments = 0;
    private LineSegment[] lineSegments = EmptyLineSegments;

    private static class PointNaturalOrderComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return o1.compareTo(o2);
        }
    }

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        if (points.length < LineSegmentCount) return;
        // todo;
        if (points.length == LineSegmentCount) return;

        LinkedList<LineSegment> lineSegmentsList = new LinkedList<LineSegment>();

        lineSegments = lineSegmentsList.toArray(EmptyLineSegments);
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments;
    }


    private interface CombinationProcessor {
        void process(int[] combination, int number);
    }

    private static int processCombinations(int n, int k, CombinationProcessor processor) {
        if (n < k) throw new IllegalArgumentException();

        final int missedMoveCursorIndex = -1;
        int[] cursors = new int[k];
        int[] cursorsBounds = new int[k];
        int headCursorBound = n - k;
        for (int i = 0; i < k; i++) {
            cursors[i] = i;
            cursorsBounds[i] = headCursorBound + i;
        }
        int headCursorIndex = 0;
        int tailCursorIndex = k - 1;
        int moveCursorIndex = tailCursorIndex;
        int combinationsCount = 0;

        processor.process(makeCombination(cursors), combinationsCount++);

        if (k == n) return combinationsCount;

        while (cursors[headCursorIndex] < headCursorBound) {
            // _xxx_..._x
            // ---------^ move item till last index
            if (cursors[moveCursorIndex] < cursorsBounds[moveCursorIndex])
                ++cursors[moveCursorIndex];
            else {
                // _xxx_..._x
                // ---^----- find last item, which not reach bounds
                while (cursors[moveCursorIndex] >= cursorsBounds[moveCursorIndex])
                    --moveCursorIndex;

                // _xx_xx_...
                // ----^----- increment found item and rearange next after it
                ++cursors[moveCursorIndex];

                if (moveCursorIndex < tailCursorIndex)
                    for (int i = moveCursorIndex + 1, j = 1; i < k; i++, j++)
                        cursors[i] = cursors[moveCursorIndex] + j;

                // _xx_xx_...
                // -----^--- set new move item
                moveCursorIndex = tailCursorIndex;
            }

            processor.process(makeCombination(cursors), combinationsCount++);
        }

        return combinationsCount;
    }

    private static int[] makeCombination(int[] cursors) {
        int k = cursors.length;
        int[] combination = new int[k];

        for (int i = 0; i < k; i++)
            combination[i] = cursors[i];

        return combination;
    }

    public static void main(String[] args) {
        // Integer[] a = new Integer[] { 1, 2, 3 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4, 5 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4, 5, 6 };
        Integer[] a = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
        int n = a.length;
        int k = 3;

        List<int[]> combinations = Combinations.generate(n, k);
        List<int[]> testCombinations = new LinkedList<>();
        int count = processCombinations(
                n,
                k,
                (int[] combination, int number) -> testCombinations.add(combination)
        );

        assert count == testCombinations.size() && count == combinations.size();

        for (int i = 0; i < n; i++) {
            int[] combination = testCombinations.get(i);
            int[] testCombination = testCombinations.get(i);

            for (int j = 0; j < k; j++)
                assert testCombination[j] == combination[j];
        }
    }
}
