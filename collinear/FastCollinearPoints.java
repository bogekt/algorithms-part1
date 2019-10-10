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
        int i = 0;
        for (Point point : points) {
            if (Objects.isNull(point)) throw new IllegalArgumentException("null item");
            // and check for duplicates
            for (int j = i++ + 1; j < points.length; j++) {
                Point point2 = points[j];
                if (Objects.isNull(point2)) throw new IllegalArgumentException("null item");
                if (point2.compareTo(point) == 0) throw new IllegalArgumentException("duplicate");
            }
        }

        if (points.length < LINE_SEGMENT_COUNT) return;

        i = 0;
        // todo
        points = points.clone();
        int collinearPointsCount = 0;
        int last4Index = points.length - LINE_SEGMENT_COUNT;

        while (Math.max(collinearPointsCount, i) <= last4Index) {
            // todo
            // skip collinear (null) points
            // i = findNotNull(points, i, last4Index);
            //
            // if (i == -1) break;
            // prepare
            Point p = points[i];
            Arrays.sort(points, i, points.length, p.slopeOrder());

            // todo
            // skip collinear (null) points
            // int j = findNotNull(points, i + 1, last4Index);
            //
            // if (j == -1) break;
            // check is colinear
            int j = i + 1;
            double slope;
            boolean isCollinear4 = false;

            do {
                slope = p.slopeTo(points[j]);

                if (slope != p.slopeTo(points[j + 1])) continue;
                if (slope != p.slopeTo(points[j + 2])) continue;

                isCollinear4 = true;
            } while (!isCollinear4 && j++ <= last4Index);

            if (!isCollinear4) {
                i++;
                continue;
            }

            // lookup for all colinear points (> 4)
            int k = j + 2;
            int temp = k + 1;

            while (temp < points.length && p.slopeTo(points[temp]) == slope)
                k = temp++;

            collinearPointsCount += k - j + 2;

            // lookup for first and last point
            Point min = p;
            Point max = p;

            while (j <= k) {
                Point point = points[j];
                if (min.compareTo(point) > 0) min = point;
                if (max.compareTo(point) < 0) max = point;
                // todo
                // points[j] = null;
                j++;
            }

            lineSegmentsList.addFirst(new LineSegment(min, max));
            i++;
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


    private static <T> int findNotNull(T[] array, int start, int end) {
        if (start < 0) return -1;

        int i = start;
        int notNullIndex = -1;

        while (i <= end && i < array.length) {
            if (array[i] != null) {
                notNullIndex = i;
                break;
            }

            i++;
        }

        return notNullIndex;
    }

    public static void main(String[] args) {
        FastCollinearPoints success2 = new FastCollinearPoints(new Point[] {
                new Point(2, 2),
                new Point(4, 4),
                new Point(3, 3),
                new Point(1, 1),

                new Point(2, 3),
                new Point(4, 1),
                new Point(6, 6),
                new Point(5, 5),
                new Point(3, 2),
                new Point(1, 4),
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
