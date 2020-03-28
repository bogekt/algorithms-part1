/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 28/03/2020
 *  Description: Brute-force set of points with lookup of range and nearest
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.LinkedList;

public class PointSET {
    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();

        LinkedList<Point2D> inside = new LinkedList<>();

        for (Point2D p : points)
            if (rect.contains(p)) inside.add(p);

        return inside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (points.isEmpty()) return null;
        if (points.contains(p)) return p;

        Point2D min = null;

        for (Point2D q : points) {
            if (min != null && p.distanceSquaredTo(min) <= p.distanceSquaredTo(q)) continue;
            min = q;
        }

        return min;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        testNearest(
                new Point2D[] {
                        /* A */ new Point2D(0.5, 0.0),
                        /* B */ new Point2D(1.0, 0.5),
                        /* C */ new Point2D(0.5, 0.0),
                        /* D */ new Point2D(0.25, 0.75),
                        /* E */ new Point2D(0.75, 0.0),
                        /* F */ new Point2D(0.25, 1.0),
                        /* G */ new Point2D(1.0, 0.0),
                        /* H */ new Point2D(0.5, 0.25),
                        /* I */ new Point2D(0.5, 0.5),
                        /* J */ new Point2D(0.0, 0.5),
                        },
                new Point2D(0.5, 0.75),
                new Point2D(0.5, 0.5),
                0.0625
        );

        testNearest(
                new Point2D[] {
                        /* A */  new Point2D(1.0, 1.0),
                        /* B */  new Point2D(0.75, 0.75),
                        /* C */  new Point2D(1.0, 1.0),
                        /* D */  new Point2D(0.5, 0.25),
                        /* E */  new Point2D(1.0, 0.25),
                        /* F */  new Point2D(0.75, 1.0),
                        /* G */  new Point2D(0.0, 1.0),
                        /* H */  new Point2D(0.0, 0.5),
                        /* I */  new Point2D(0.0, 0.0),
                        /* J */  new Point2D(0.0, 0.25),
                        },
                new Point2D(0.75, 1.0),
                new Point2D(0.75, 1.0),
                0
        );

        testNearest(
                new Point2D[] {
                        /* A */  new Point2D(1.0, 0.9375),
                        /* B */  new Point2D(0.1875, 0.1875),
                        /* C */  new Point2D(0.6875, 0.6875),
                        /* D */  new Point2D(0.0625, 0.0625),
                        /* E */  new Point2D(0.0625, 0.9375),
                        /* F */  new Point2D(0.0625, 0.875),
                        /* G */  new Point2D(0.125, 0.8125),
                        /* H */  new Point2D(0.1875, 0.0),
                        /* I */  new Point2D(0.3125, 0.1875),
                        /* J */  new Point2D(0.1875, 0.4375),
                        /* K */  new Point2D(0.9375, 0.8125),
                        /* L */  new Point2D(0.9375, 0.875),
                        /* M */  new Point2D(0.3125, 0.0625),
                        /* N */  new Point2D(1.0, 0.9375),
                        /* O */  new Point2D(0.5625, 0.6875),
                        /* P */  new Point2D(0.4375, 0.9375),
                        /* Q */  new Point2D(0.125, 0.625),
                        /* R */  new Point2D(0.9375, 0.9375),
                        /* S */  new Point2D(0.125, 0.1875),
                        /* T */  new Point2D(0.0, 0.5625),
                        },
                new Point2D(0.3125, 0.1875),
                new Point2D(0.3125, 0.1875),
                0
        );
    }

    private static void testNearest(
            Point2D[] insert,
            Point2D query,
            Point2D expected,
            double expectedDistance
    ) {
        PointSET pointSET = new PointSET();

        for (Point2D p : insert)
            pointSET.insert(p);

        Point2D nearest = pointSET.nearest(query);

        assert nearest.equals(expected);
        assert nearest.distanceSquaredTo(query) == expectedDistance;
    }
}
