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
        // Arrays.sort(points, points[0].slopeOrder());
        while (i <= points.length - LINE_SEGMENT_COUNT) {
            // prepare
            Point p = points[i];
            Arrays.sort(points, p.slopeOrder());
            // todo
            // Arrays.sort(points, i, points.length, p.slopeOrder());

            // check is colinear
            double slope = p.slopeTo(points[i + 1]);
            if (p.slopeTo(points[i + 2]) != slope || p.slopeTo(points[i + 3]) != slope) {
                i++;
                continue;
            }

            // lookup for all colinear points (> 4)
            int k = i + 3;
            int j = i + 4;
            while (j < points.length && p.slopeTo(points[j]) == slope)
                k = j++;

            // lookup for first and last point
            Point min = p;
            Point max = p;
            for (j = i; j <= k; j++) {
                Point point = points[j];
                if (min.compareTo(point) > 0) min = point;
                if (max.compareTo(point) < 0) max = point;
            }

            lineSegmentsList.add(new LineSegment(min, max));

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
        return lineSegments;
    }

    public static void main(String[] args) {
        FastCollinearPoints success2 = new FastCollinearPoints(new Point[] {
                new Point(2, 2),
                new Point(4, 4),
                new Point(3, 3),
                new Point(1, 1),

                // todo
                new Point(-2, 2),
                new Point(-4, 4),
                new Point(-3, 3),
                new Point(-1, 1),
                });

        assert success2.numberOfSegments() == 2;
    }
}
