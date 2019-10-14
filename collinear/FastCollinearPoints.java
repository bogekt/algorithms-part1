/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

public class FastCollinearPoints {
    private static final LineSegment[] EMPTY_LINE_SEGMENTS = new LineSegment[0];
    private static final int LINE_SEGMENT_COUNT = 4;

    private static final class PointNaturalOrderComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return o1.compareTo(o2);
        }
    }

    private LineSegment[] lineSegments = EMPTY_LINE_SEGMENTS;
    private final LinkedList<LineSegment> lineSegmentsList = new LinkedList<LineSegment>();

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        // just not null
        if (points == null) throw new IllegalArgumentException();
        // has any null point
        int pIndex = 0;
        for (Point point : points) {
            if (Objects.isNull(point)) throw new IllegalArgumentException("null item");
            // and check for duplicates
            for (int j = pIndex++ + 1; j < points.length; j++) {
                Point point2 = points[j];
                if (Objects.isNull(point2)) throw new IllegalArgumentException("null item");
                if (point2.compareTo(point) == 0) throw new IllegalArgumentException("duplicate");
            }
        }

        if (points.length < LINE_SEGMENT_COUNT) return;

        pIndex = 0;
        points = points.clone();
        final int last4Index = points.length - LINE_SEGMENT_COUNT;
        final int last3Index = points.length - LINE_SEGMENT_COUNT + 1;

        while (pIndex <= last4Index) {
            // prepare
            Point p = points[pIndex];
            Arrays.sort(points, pIndex, points.length, p.slopeOrder());
            int qIndex = pIndex++ + 1;

            while (qIndex <= last3Index) {
                // check is colinear
                double slope = p.slopeTo(points[qIndex]);
                int sIndex = qIndex + 2;

                if (slope != p.slopeTo(points[sIndex])) {
                    qIndex++;
                    continue;
                }
                // lookup for all colinear points (> 4)
                int sIndexNext = sIndex + 1;

                while (sIndexNext < points.length && p.slopeTo(points[sIndexNext]) == slope)
                    sIndex = sIndexNext++;
                // lookup for first and last point
                Point min = p;
                Point max = p;

                while (qIndex <= sIndex) {
                    Point point = points[qIndex++];
                    if (min.compareTo(point) > 0) min = point;
                    if (max.compareTo(point) < 0) max = point;
                }

                lineSegmentsList.addFirst(new LineSegment(min, max));
            }
        }

        lineSegments = lineSegmentsList.toArray(EMPTY_LINE_SEGMENTS);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }


    private static <T> void swap(T[] array, int i, int j) {
        T x = array[i];
        array[i] = array[j];
        array[j] = x;
    }

    public static void main(String[] args) {
        FastCollinearPoints success2 = new FastCollinearPoints(new Point[] {
                new Point(2, 3),
                new Point(4, 1),
                new Point(6, 6),
                new Point(5, 5),
                new Point(3, 2),
                new Point(1, 4),

                new Point(2, 2),
                new Point(4, 4),
                new Point(3, 3),
                new Point(1, 1),

                new Point(3, 5),
                new Point(4, 7),
                new Point(5, 9),
                });

        assert success2.numberOfSegments() == 3;

        FastCollinearPoints fail = new FastCollinearPoints(new Point[] {
                new Point(2, 2),
                new Point(3, 3),
                new Point(1, 1),

                new Point(2, 3),
                new Point(4, 1),
                new Point(3, 2),
                });

        assert fail.numberOfSegments() == 0;
    }
}
