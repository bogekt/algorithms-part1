/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class KdTree {
    private static final int vertical = 1;
    private static final int horizontal = -1;

    private int size;
    private Node root;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p) {
            this.p = p;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        root = put(root, p, vertical);
        size++;
    }

    private Node put(Node x, Point2D p, int line) {
        if (x == null) return new Node(p);
        int compare = compare(p, x.p, line);
        if (compare < 0) x.lb = put(x.lb, p, -1 * line);
        else if (compare > 0) x.rt = put(x.rt, p, -1 * line);
        else x.p = p;
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return get(root, p, vertical) != null;
    }

    private Node get(Node x, Point2D p, int line) {
        if (x == null) return null;
        int compare = compare(p, x.p, line);
        if (compare < 0) return get(x.lb, p, -1 * line);
        else if (compare > 0) return get(x.rt, p, -1 * line);
        else return x;
    }

    private int compare(Point2D p, Point2D q, int line) {
        return line == horizontal
               ? Double.compare(p.x(), q.x())
               : Double.compare(p.y(), q.y());
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : iterator())
            p.draw();
    }

    private Iterable<Point2D> iterator() {
        return () -> new KdTreeIterator(root);
    }

    // Morris, Joseph M. (1979). "Traversing binary trees simply and cheaply".
    // https://en.wikipedia.org/wiki/Tree_traversal#Morris_in-order_traversal_using_threading
    private class KdTreeIterator implements Iterator<Point2D> {
        private Node node;

        public KdTreeIterator(Node root) {
            node = root;
        }

        public boolean hasNext() {
            return node != null;
        }

        public Point2D next() {
            Point2D key = null;

            while (key == null) {
                if (node.lb == null) {
                    key = node.p;
                    node = node.rt;
                }
                else {
                    Node next = node.lb;

                    while (next.rt != null && next.rt != node)
                        next = next.rt;

                    if (next.rt == null) {
                        next.rt = node;
                        node = node.lb;
                    }
                    else {
                        key = node.p;
                        next.rt = null;
                        node = node.rt;
                    }
                }
            }

            return key;
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // contains
        testContains(
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
                new Point2D[] {
                        new Point2D(0.6875, 0.7875),
                        new Point2D(0.1, 0.1),
                        new Point2D(1.1, 1.1),
                        new Point2D(-0.1, -0.1),
                        }
        );

        // nearest
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

    private static void testContains(Point2D[] insert, Point2D[] notContains) {
        KdTree kdTree = new KdTree();

        for (Point2D p : insert)
            kdTree.insert(p);

        Point2D[] contains = insert.clone();
        StdRandom.shuffle(contains);

        for (Point2D p : contains)
            assert kdTree.contains(p);

        for (Point2D p : notContains)
            assert !kdTree.contains(p);
    }

    private static void testNearest(
            Point2D[] insert,
            Point2D query,
            Point2D expected,
            double expectedDistance
    ) {
        KdTree kdTree = new KdTree();

        for (Point2D p : insert)
            kdTree.insert(p);

        Point2D nearest = kdTree.nearest(query);

        assert nearest.equals(expected);
        assert nearest.distanceSquaredTo(query) == expectedDistance;
    }
}
