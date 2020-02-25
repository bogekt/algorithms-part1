/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
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

        Point2D min = null;

        for (Point2D q : points) {
            if (q.equals(p)) continue;
            if (min != null && p.distanceSquaredTo(min) <= p.distanceSquaredTo(q)) continue;
            min = q;
        }

        return min;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();

        for (Point2D p : new Point2D[] {
                /*A*/ new Point2D(0.5, 0.0),
                /*B*/ new Point2D(1.0, 0.5),
                /*C*/ new Point2D(0.5, 0.0),
                /*D*/ new Point2D(0.25, 0.75),
                /*E*/ new Point2D(0.75, 0.0),
                /*F*/ new Point2D(0.25, 1.0),
                /*G*/ new Point2D(1.0, 0.0),
                /*H*/ new Point2D(0.5, 0.25),
                /*I*/ new Point2D(0.5, 0.5),
                /*J*/ new Point2D(0.0, 0.5),
                })
            pointSET.insert(p);

        Point2D expected = new Point2D(0.5, 0.5);
        Point2D query = new Point2D(0.5, 0.75);
        Point2D nearest = pointSET.nearest(query);

        assert nearest.equals(expected);
        assert nearest.distanceSquaredTo(query) == 0.0625;
    }
}
