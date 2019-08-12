/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Comparator;
import java.util.LinkedList;

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

        for (int i = 0; i < points.length; i += LineSegmentCount) {
            Point p = points[i];
            Point q = points[i + 1];
            Point r = points[i + 2];
            Point s = points[i + 3];
            // todo
        }

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


    public static void main(String[] args) {
    }
}
