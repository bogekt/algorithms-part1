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
        int slopeIndex = 0;
        for (Point point : points) {
            if (Objects.isNull(point)) throw new IllegalArgumentException("null item");
            // and check for duplicates
            for (int j = slopeIndex++ + 1; j < points.length; j++) {
                Point point2 = points[j];
                if (Objects.isNull(point2)) throw new IllegalArgumentException("null item");
                if (point2.compareTo(point) == 0) throw new IllegalArgumentException("duplicate");
            }
        }

        if (points.length < LINE_SEGMENT_COUNT) return;

        slopeIndex = 0;
        // todo
        points = points.clone();
        int collinearPointsCount = 0;
        final int last4Index = points.length - LINE_SEGMENT_COUNT;
        final int lastIndex = points.length - 1;

        while (Math.max(collinearPointsCount, slopeIndex) <= last4Index) {
            // prepare
            Point p = points[slopeIndex];
            Arrays.sort(points, slopeIndex, points.length, p.slopeOrder());

            // check is colinear
            int startCollinearIndex = slopeIndex + 1;
            double slope;
            boolean isCollinear4 = false;

            do {
                slope = p.slopeTo(points[startCollinearIndex]);
                // if (slope != p.slopeTo(points[j + 1])) continue;
                if (slope != p.slopeTo(points[startCollinearIndex + 2])) continue;

                isCollinear4 = true;
            } while (!isCollinear4 && startCollinearIndex++ <= last4Index);

            if (!isCollinear4) {
                slopeIndex++;
                continue;
            }

            // lookup for all colinear points (> 4)
            int endCollinearIndex = startCollinearIndex + 2;
            int i = endCollinearIndex + 1;

            while (i < points.length && p.slopeTo(points[i]) == slope)
                endCollinearIndex = i++;

            collinearPointsCount += endCollinearIndex - startCollinearIndex + 2;

            // lookup for first and last point
            Point min = p;
            Point max = p;
            boolean needSwap = endCollinearIndex < lastIndex;

            while (startCollinearIndex <= endCollinearIndex) {
                Point point = points[startCollinearIndex];
                if (min.compareTo(point) > 0) min = point;
                if (max.compareTo(point) < 0) max = point;
                if (needSwap) swap(points, ++slopeIndex, startCollinearIndex);
                startCollinearIndex++;
            }

            lineSegmentsList.addFirst(new LineSegment(min, max));
            slopeIndex++;
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
                });

        assert success2.numberOfSegments() == 2;

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
