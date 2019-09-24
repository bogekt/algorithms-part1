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
    private static final Comparator<Point> NATURAL_ORDER = new PointNaturalOrderComparator();
    private static final LineSegment[] EMPTY_LINE_SEGMENTS = new LineSegment[0];
    private static final int LINE_SEGMENT_COUNT = 4;
    private static final PointNaturalOrderComparator POINT_NATURAL_ORDER_COMPARATOR
            = new PointNaturalOrderComparator();

    private static final class PointNaturalOrderComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return o1.compareTo(o2);
        }
    }

    private LineSegment[] lineSegments = EMPTY_LINE_SEGMENTS;
    private ColinearPointsProcessor colinearPointsProcessor;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        if (points.length < LINE_SEGMENT_COUNT) return;

        colinearPointsProcessor = new ColinearPointsProcessor(points.clone());
        processCombinations(
                points.length,
                LINE_SEGMENT_COUNT,
                colinearPointsProcessor::process
        );
        lineSegments = colinearPointsProcessor.lineSegmentsList.toArray(EMPTY_LINE_SEGMENTS);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments;
    }


    private class ColinearPointsProcessor {
        private final LinkedList<LineSegment> lineSegmentsList = new LinkedList<LineSegment>();
        private final Point[] points;

        ColinearPointsProcessor(Point[] points) {
            this.points = points;
        }

        private void process(int[] combination, int number) {
            Point p = points[combination[0]];
            Point q = points[combination[1]];
            Point r = points[combination[2]];
            Point s = points[combination[3]];

            double slope = p.slopeTo(q);

            if (slope != p.slopeTo(r)) return;
            if (slope != p.slopeTo(s)) return;

            Point[] lineSegmentPoints = new Point[] { p, q, r, s };
            Arrays.sort(lineSegmentPoints, POINT_NATURAL_ORDER_COMPARATOR);

            lineSegmentsList.add(new LineSegment(lineSegmentPoints[0], lineSegmentPoints[3]));
        }
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
        final int[][] COMBINATIONS_4_2 = new int[][] {
                { 1, 2 }, { 1, 3 }, { 1, 4 }, { 2, 3 }, { 2, 4 }, { 3, 4 },
                };
        final int[][] COMBINATIONS_7_3 = new int[][] {
                { 1, 2, 3 }, { 1, 2, 4 }, { 1, 2, 5 }, { 1, 2, 6 }, { 1, 2, 7 }, { 1, 3, 4 },
                { 1, 3, 5 }, { 1, 3, 6 }, { 1, 3, 7 }, { 1, 4, 5 }, { 1, 4, 6 }, { 1, 4, 7 },
                { 1, 5, 6 }, { 1, 5, 7 }, { 1, 6, 7 }, { 2, 3, 4 }, { 2, 3, 5 }, { 2, 3, 6 },
                { 2, 3, 7 }, { 2, 4, 5 }, { 2, 4, 6 }, { 2, 4, 7 }, { 2, 5, 6 }, { 2, 5, 7 },
                { 2, 6, 7 }, { 3, 4, 5 }, { 3, 4, 6 }, { 3, 4, 7 }, { 3, 5, 6 }, { 3, 5, 7 },
                { 3, 6, 7 }, { 4, 5, 6 }, { 4, 5, 7 }, { 4, 6, 7 }, { 5, 6, 7 },
                };

        checkCombinations(4, 2, COMBINATIONS_4_2);
        checkCombinations(7, 3, COMBINATIONS_7_3);
    }

    private static void checkCombinations(int n, int k, int[][] combinations) {
        List<int[]> testCombinations = new LinkedList<>();
        int count = processCombinations(
                n,
                k,
                (int[] combination, int number) -> testCombinations.add(combination)
        );

        assert count == testCombinations.size() && count == combinations.length;

        for (int i = 0; i < n; i++) {
            int[] combination = testCombinations.get(i);
            int[] testCombination = testCombinations.get(i);

            for (int j = 0; j < k; j++)
                assert testCombination[j] == combination[j];
        }
    }
}
