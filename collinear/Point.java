/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point. Formally, if the two points are
     * (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 - x0). For completeness, the slope
     * is defined to be +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical; and Double.NEGATIVE_INFINITY if
     * (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (that == null) throw new NullPointerException();
        if (compareTo(that) == 0) return Double.NEGATIVE_INFINITY;
        if (x == that.x) return +0.0;
        if (y == that.y) return Double.POSITIVE_INFINITY;

        return ((double) that.y - y) / (that.x - x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally, the invoking
     * point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if
     * y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument point (x0 = x1 and y0 =
     * y1); a negative integer if this point is less than the argument point; and a positive integer
     * if this point is greater than the argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (that == null) throw new NullPointerException();
        if (y == that.y && x == that.x) return 0;
        if (y == that.y) return x - that.x;

        return y - that.y;
    }

    /**
     * Compares two points by the slope they make with this point. The slope is defined as in the
     * slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new PointSlopeOrderComparator();
    }

    private class PointSlopeOrderComparator implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            double slope1 = slopeTo(o1);
            double slope2 = slopeTo(o2);

            if (slope1 == slope2) return 0;
            if (slope1 > slope2) return +1;
            if (slope1 < slope2) return -1;

            throw new IllegalArgumentException();
        }
    }


    /**
     * Returns a string representation of this point. This method is provide for debugging; your
     * program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */

        Point p22 = new Point(2, 2);
        Point p11 = new Point(1, 1);
        Point p33 = new Point(3, 3);
        Point p44 = new Point(4, 4);
        assert p11.slopeTo(p22) == p22.slopeTo(p33) && p22.slopeTo(p33) == p33.slopeTo(p11);

        Comparator<Point> slopeOrder = p11.slopeOrder();
        assert slopeOrder.compare(p22, p33) == 0
                && slopeOrder.compare(p33, p44) == 0
                && slopeOrder.compare(p44, p22) == 0;

        Point pMax1 = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Point pMax2 = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assert pMax1.compareTo(pMax2) == 0 && pMax2.compareTo(pMax1) == 0 && pMax1 != pMax2;

        Point[] pArray = new Point[] {
                p22,
                p44,
                pMax1,
                p33,
                p11,

                new Point(2, 3),
                new Point(4, 1),
                pMax1,
                new Point(6, 6),
                new Point(5, 5),
                pMax1,
                new Point(3, 2),
                pMax2,
                new Point(1, 4),
                };

        Arrays.sort(pArray, pArray[0].slopeOrder());
        Arrays.sort(pArray);

        int i = pArray.length - 4;
        int pMax1Count = 0;
        while (i < pArray.length) {
            Point p = pArray[i++];

            if (p == pMax1) pMax1Count++;
            assert (p == pMax1 || p == pMax2);
            assert p.compareTo(pMax1) == 0 && p.compareTo(pMax2) == 0;
        }

        assert pMax1Count == 3;
    }
}
