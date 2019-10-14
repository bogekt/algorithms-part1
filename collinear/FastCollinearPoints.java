/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 14/10/2019
 *  Description: Lookup of collinear semgent with 4 points and more
 *  using sorting of them by slope order
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

public class FastCollinearPoints {
    private static final int LINE_SEGMENT_COUNT = 4;

    private static final class PointNaturalOrderComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return o1.compareTo(o2);
        }
    }

    private LineSegment[] lineSegments = new LineSegment[0];

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
        final LinkedList<Point> lineSegmentnsPoints = new LinkedList<Point>();

        while (pIndex <= last4Index) {
            // prepare sorted points
            Point p = points[pIndex];
            Arrays.sort(points, pIndex, points.length, p.slopeOrder());
            int qIndex = pIndex++ + 1;

            while (qIndex <= last3Index) {
                // check is collinear 4 point
                double slope = p.slopeTo(points[qIndex]);
                int sIndex = qIndex + 2;

                if (slope != p.slopeTo(points[sIndex])) {
                    qIndex++;
                    continue;
                }
                // skip same line segments points by checking them on colliearity
                if (lineSegmentnsPoints.size() > 0 && !processLineSegmentPoints(
                        lineSegmentnsPoints,
                        (min, max, i) -> !(p.slopeTo(min) == slope && p.slopeTo(max) == slope)
                )) {
                    qIndex++;
                    continue;
                }
                // lookup for colinear points > 4
                int sIndexNext = sIndex + 1;

                while (sIndexNext < points.length && p.slopeTo(points[sIndexNext]) == slope)
                    sIndex = sIndexNext++;
                // lookup for start and end points (min and max in natural order)
                Point start = p;
                Point end = p;

                while (qIndex <= sIndex) {
                    Point point = points[qIndex++];

                    if (start.compareTo(point) > 0) start = point;
                    if (end.compareTo(point) < 0) end = point;
                }

                // todo
                // In current solution sometimes we can have sub-segmets.
                // We can try to avoid them, implenting check sub-segmets here
                // and compare them start end with new found.
                // As result we can take with greater distance.
                lineSegmentnsPoints.add(start);
                lineSegmentnsPoints.add(end);
            }
        }

        lineSegments = new LineSegment[lineSegmentnsPoints.size() / 2];
        processLineSegmentPoints(
                lineSegmentnsPoints,
                (p, q, i) -> (lineSegments[i] = new LineSegment(p, q)) != null
        );
    }

    private interface LineSegmentPointsProcessor {
        boolean process(Point start, Point end, int number);
    }

    private static boolean processLineSegmentPoints(
            LinkedList<Point> points,
            LineSegmentPointsProcessor processor
    ) {
        Point p = null;
        boolean isOdd = true;
        int i = 0;

        for (Point q : points) {
            if (isOdd) p = q;
            else if (!processor.process(p, q, i++)) return false;

            isOdd = !isOdd;
        }

        return true;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    public static void main(String[] args) {
        Point[] points = new Point[] {
                new Point(1, 4),
                new Point(2, 3),
                new Point(3, 2),
                new Point(4, 1),

                new Point(1, 1),
                new Point(2, 2),
                new Point(4, 4),
                new Point(3, 3),
                new Point(6, 6),

                new Point(3, 5),
                new Point(4, 7),
                new Point(5, 9),
                };

        Point[] pointsSuccess3 = points.clone();
        StdRandom.shuffle(pointsSuccess3);
        FastCollinearPoints success3 = new FastCollinearPoints(pointsSuccess3);
        assert success3.numberOfSegments() == 3;

        FastCollinearPoints fail = new FastCollinearPoints(Arrays.copyOfRange(points, 2, 6));
        assert fail.numberOfSegments() == 0;
    }
}
