/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        LinkedList<Point2D> inside = new LinkedList<>();

        for (Point2D p : points)
            if (rect.contains(p)) inside.add(p);

        return inside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (points.isEmpty()) return null;

        ArrayList<Point2D> nearestPoints = new ArrayList<>(points.size());
        for (Point2D x : points)
            nearestPoints.add(p);

        Comparator<Point2D> c = new DistanceToOrder(p);
        nearestPoints.sort(c);
        int i = Collections.binarySearch(nearestPoints, p, c);

        Point2D floor = i - 1 >= 0 ? nearestPoints.get(i - 1) : null;
        Point2D celling = i + 1 < nearestPoints.size() ? nearestPoints.get(i + 1) : null;
        if (floor == null) return celling;
        else if (celling == null) return floor;

        return floor.distanceSquaredTo(p) < celling.distanceSquaredTo(p)
               ? floor
               : celling;
    }

    // compare points according to their distance to this point
    private class DistanceToOrder implements Comparator<Point2D> {
        private final Point2D point;

        public DistanceToOrder(Point2D point) {
            this.point = point;
        }

        public int compare(Point2D p, Point2D q) {
            double dist1 = point.distanceSquaredTo(p);
            double dist2 = point.distanceSquaredTo(q);
            if (dist1 < dist2) return -1;
            else if (dist1 > dist2) return +1;
            else return 0;
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
    }
}
