/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
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
        int currentCursorIndex = tailCursorIndex;
        int combinationsCount = 0;

        processor.process(makeCombination(cursors), combinationsCount++);

        if (k == n) return combinationsCount;

        int moveCursorIndex = missedMoveCursorIndex;

        while (cursors[headCursorIndex] < headCursorBound) {
            if (moveCursorIndex != missedMoveCursorIndex) {
                if (cursors[moveCursorIndex] < cursorsBounds[moveCursorIndex]) {
                    cursors[moveCursorIndex]++;
                }
                else {
                    moveCursorIndex = currentCursorIndex;

                    while (
                            moveCursorIndex < tailCursorIndex &&
                                    isNextNeighbor(cursors, moveCursorIndex)
                    )
                        moveCursorIndex++;

                    if (currentCursorIndex == moveCursorIndex) {
                        ++cursors[currentCursorIndex];

                        if (currentCursorIndex < tailCursorIndex)
                            for (int i = currentCursorIndex + 1, j = 1; i < k; i++, j++)
                                cursors[i] = cursors[currentCursorIndex] + j;

                        moveCursorIndex = missedMoveCursorIndex;
                    }
                }
            }
            else {
                ++cursors[--currentCursorIndex];

                if (currentCursorIndex < tailCursorIndex)
                    for (int i = currentCursorIndex + 1, j = 1; i < k; i++, j++)
                        cursors[i] = cursors[currentCursorIndex] + j;

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

    private static boolean isNextNeighbor(int[] cursors, int index) {
        return nextCursorDiff(cursors, index) == 1;
    }

    private static int nextCursorDiff(int[] cursors, int index) {
        return diff(cursors, index, index + 1);
    }

    private static int diff(int[] cursors, int index1, int index2) {
        return Math.abs(cursors[index1] - cursors[index2]);
    }

    public static void main(String[] args) {
        // Integer[] a = new Integer[] { 1, 2, 3 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4 };
        // Integer[] a = new Integer[] { 1, 2, 3, 4, 5 };
        Integer[] a = new Integer[] { 1, 2, 3, 4, 5, 6 };
        int n = a.length;
        int k = 3;

        List<int[]> combinations = Combinations.generate(n, k);
        int count = processCombinations(n, k, (int[] combination, int number) -> {
            System.out.println(
                    Arrays.toString(combination) +
                            " " +
                            (
                                    number < combinations.size()
                                    ? Arrays.toString(combinations.get(number))
                                    : null
                            )
            );
        });

        for (int i = count; i < combinations.size(); i++)
            System.out.println(
                    null +
                            " " +
                            Arrays.toString(combinations.get(i))
            );

        System.out.printf(
                "Generated %d combinations of %d items from %d. Expected count is %d",
                count,
                k,
                n,
                combinations.size()
        );
    }
}
