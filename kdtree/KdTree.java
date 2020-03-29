/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 28/03/2020
 *  Description: KdTree-based set of points with lookup of range and nearest
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;

public class KdTree {
    private static boolean DEBUG = false;
    private static RedBlackBST<Point2D, String> pointNames = null;
    private static final int vertical = 1;
    private static final int horizontal = -1;
    private static final RectHV rootRect = new RectHV(0, 0, 1, 1);

    private int size;
    private Node root;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
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
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;

        root = put(root, p, vertical, rootRect);
    }

    private Node put(Node x, Point2D p, int line, RectHV rect) {
        if (x == null) {
            size++;
            return new Node(p, rect);
        }

        int compare = compare(p, x.p, -1 * line);
        RectHV[] rectHVs = split(x.rect, x.p, line);
        if (compare < 0) x.lb = put(x.lb, p, -1 * line, rectHVs[0]);
        else x.rt = put(x.rt, p, -1 * line, rectHVs[1]);

        return x;
    }

    private RectHV[] split(RectHV rect, Point2D p, int line) {
        if (line == vertical)
            return new RectHV[] {
                    new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax()), // left
                    new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax()), // right
            };
        if (line == horizontal)
            return new RectHV[] {
                    new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y()), // bottom
                    new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax()), // top
            };
        throw new IllegalArgumentException();
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p, vertical) != null;
    }

    private Node get(Node x, Point2D p, int line) {
        if (x == null) return null;
        int compare = compare(p, x.p, -1 * line);
        if (compare < 0) return get(x.lb, p, -1 * line);
        else if (compare > 0) return get(x.rt, p, -1 * line);
        return p.compareTo(x.p) == 0 ? x : get(x.rt, p, -1 * line);
    }

    private int compare(Point2D p, Point2D q, int line) {
        return line == horizontal
               ? Double.compare(p.x(), q.x())
               : Double.compare(p.y(), q.y());
    }

    // draw all points to standard draw
    public void draw() {
        int level = 0;
        while (true) {
            Queue<Node> nodes = new Queue<>();
            levelNodes(nodes, root, 0, level);

            StdOut.println();
            StdOut.printf("Level %d nodes:%d", level, nodes.size());
            StdOut.println();

            if (nodes.size() == 0) break;

            int line = level % 2 == 0 ? vertical : horizontal;
            Color rectColor = line == vertical ? StdDraw.RED : StdDraw.BLUE;

            for (Node node : nodes) {
                StdOut.printf(
                        "Point %s line %s Rect %s",
                        node.p,
                        line == vertical ? "vertical" : "horizontal",
                        node.rect
                );
                StdOut.println();

                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                node.p.draw();

                StdDraw.setPenColor(rectColor);
                StdDraw.setPenRadius();
                RectHV[] rectHVs = split(node.rect, node.p, line);
                if (line == vertical)
                    StdDraw.line(
                            rectHVs[0].xmax(), // left bottomm right
                            rectHVs[0].ymin(), // left bottomm right
                            rectHVs[1].xmin(), // right top left
                            rectHVs[1].ymax()  // right top left
                    );
                if (line == horizontal)
                    StdDraw.line(
                            rectHVs[0].xmin(), // bottom top left
                            rectHVs[0].ymax(), // bottom top left
                            rectHVs[1].xmax(), // top bottom right
                            rectHVs[1].ymin()  // top bottom right
                    );
            }

            level++;
        }
    }

    private void levelNodes(Queue<Node> nodes, Node x, int current, int level) {
        if (current > level || x == null) return;
        if (current == level) nodes.enqueue(x);
        levelNodes(nodes, x.lb, current + 1, level);
        levelNodes(nodes, x.rt, current + 1, level);
    }


    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (root == null) return new Queue<Point2D>();

        return range(root, rect, new Queue<Point2D>());
    }

    private Queue<Point2D> range(Node x, RectHV rect, Queue<Point2D> inside) {
        if (!rect.intersects(x.rect))
            return inside;

        if (x.lb != null && rect.intersects(x.lb.rect)) inside = range(x.lb, rect, inside);
        if (x.rt != null && rect.intersects(x.rt.rect)) inside = range(x.rt, rect, inside);
        if (rect.contains(x.p)) inside.enqueue(x.p);

        return inside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        if (contains(p)) return p;

        return nearest(root, p, root.p);
    }

    private Point2D nearest(Node x, Point2D q, Point2D p) {
        if (DEBUG) StdOut.println(pointNames.get(x.p) + " " + x.p);
        if (x.rect.distanceSquaredTo(q) > p.distanceSquaredTo(q)) return p;
        p = x.p.distanceSquaredTo(q) < p.distanceSquaredTo(q) ? x.p : p;

        if (x.lb != null && x.rt != null) {
            Node[] nodes = firstSecond(x, q);
            p = nearest(nodes[0], q, p);
            if (nodes[1].rect.distanceSquaredTo(q) > p.distanceSquaredTo(q)) return p;
            p = nearest(nodes[1], q, p);
        }
        else {
            if (x.lb != null && x.lb.rect.distanceSquaredTo(q) < p.distanceSquaredTo(q))
                p = nearest(x.lb, q, p);
            if (x.rt != null && x.rt.rect.distanceSquaredTo(q) < p.distanceSquaredTo(q))
                p = nearest(x.rt, q, p);
        }

        return p;
    }

    private Node[] firstSecond(Node x, Point2D q) {
        boolean lbFirst = x.lb.rect.contains(q) || x.rt.rect.contains(q)
                          ? x.lb.rect.contains(q)
                          : x.lb.rect.distanceSquaredTo(q) < x.rt.rect.distanceSquaredTo(q);
        return lbFirst
               ? new Node[] { x.lb, x.rt }
               : new Node[] { x.rt, x.lb };
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        DEBUG = true;
        // size
        KdTree kdTree = new KdTree();
        int i = 0;
        for (Point2D p :
                new Point2D[] {
                        /* A */  new Point2D(1.0, 0.9375),
                        /* B */  new Point2D(0.1875, 0.1875),
                        /* C */  new Point2D(0.6875, 0.6875),
                        /* D */  new Point2D(0.0625, 0.0625),
                        /* E */  new Point2D(0.0625, 0.9375),
                        }) {
            kdTree.insert(p);
            assert kdTree.size() == ++i;
        }
        // not insert same
        kdTree.insert(new Point2D(0.0625, 0.0625));
        kdTree.insert(new Point2D(0.1875, 0.0));
        assert kdTree.size() == 6;

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

        // rect
        testRect(
                new Point2D[] {
                        /* A */ new Point2D(0.5, 0.5),
                        /* B */ new Point2D(0.75, 0.5),
                        /* C */ new Point2D(0.75, 0.75),
                        /* D */ new Point2D(0.625, 0.75),
                        /* E */ new Point2D(0.625, 0.625),
                        },
                new RectHV[] {
                        /* A */ new RectHV(0, 0, 1, 1),
                        /* B */ new RectHV(0.5, 0, 1, 1),
                        /* C */ new RectHV(0.5, 0.5, 1, 1),
                        /* D */ new RectHV(0.5, 0.5, 0.75, 1),
                        /* E */ new RectHV(0.5, 0.5, 0.75, 0.75),
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
                new Point2D(0.25, 0.75),
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

        testNearest(
                new Point2D[] {
                        /* A */  new Point2D(0.7, 0.2),
                        /* B */  new Point2D(0.5, 0.4),
                        /* C */  new Point2D(0.2, 0.3),
                        /* D */  new Point2D(0.4, 0.7),
                        /* E */  new Point2D(0.9, 0.6),
                        },
                new Point2D(0.232, 0.133),
                new Point2D(0.2, 0.3),
                0.028912999999999994
        );

        testNearest(
                new Point2D[] {
                        /* A */  new Point2D(0.7, 0.2),
                        /* B */  new Point2D(0.5, 0.4),
                        /* C */  new Point2D(0.2, 0.3),
                        /* D */  new Point2D(0.4, 0.7),
                        /* E */  new Point2D(0.9, 0.6),
                        },
                new Point2D(0.78, 0.32),
                new Point2D(0.7, 0.2),
                0.020800000000000013
        );

        testNearest(
                new Point2D[] {
                        /* A */  new Point2D(0.7, 0.2),
                        /* B */  new Point2D(0.5, 0.4),
                        /* C */  new Point2D(0.2, 0.3),
                        /* D */  new Point2D(0.4, 0.7),
                        /* E */  new Point2D(0.9, 0.6),
                        },
                new Point2D(0.78, 0.32),
                new Point2D(0.7, 0.2),
                0.020800000000000013
        );

        testNearest(
                new Point2D[] {
                        /* A */ new Point2D(0.372, 0.497),
                        /* B */ new Point2D(0.564, 0.413),
                        /* C */ new Point2D(0.226, 0.577),
                        /* D */ new Point2D(0.144, 0.179),
                        /* E */ new Point2D(0.083, 0.51),
                        /* F */ new Point2D(0.32, 0.708),
                        /* G */ new Point2D(0.417, 0.362),
                        /* H */ new Point2D(0.862, 0.825),
                        /* I */ new Point2D(0.785, 0.725),
                        /* J */ new Point2D(0.499, 0.208),
                        },
                new Point2D(0.43, 0.55),
                new Point2D(0.372, 0.497),
                0.006173000000000005
        );

        testNearest(
                new Point2D[] {
                        /* A */ new Point2D(0.9375, 0.8125),
                        /* B */ new Point2D(0.25, 0.5625),
                        /* C */ new Point2D(0.8125, 0.0),
                        /* D */ new Point2D(0.625, 0.9375),
                        /* E */ new Point2D(0.0625, 0.1875),
                        /* F */ new Point2D(0.3125, 0.4375),
                        /* G */ new Point2D(0.0, 0.625),
                        /* H */ new Point2D(0.375, 0.25),
                        /* I */ new Point2D(0.5625, 0.875),
                        /* J */ new Point2D(0.875, 0.0625),
                        },
                new Point2D(0.125, 0.75),
                new Point2D(0.0, 0.625),
                0.03125
        );

        DEBUG = false;
    }

    private static void testRect(Point2D[] points, RectHV[] rects) {
        StdOut.println("testRect");
        KdTree kdTree = new KdTree();

        for (Point2D p : points)
            kdTree.insert(p);

        for (int i = 0; i < points.length; i++) {
            RectHV rect = kdTree.get(kdTree.root, points[i], vertical).rect;

            if (!rect.equals(rects[i])) {
                StdOut.printf("Rect %s are not for Point %s", rect, rects[i], points[i]);
                StdOut.println();
                assert false;
            }
        }
    }

    private static void testContains(Point2D[] insert, Point2D[] notContains) {
        StdOut.println("testContains");
        KdTree kdTree = new KdTree();

        for (Point2D p : insert)
            kdTree.insert(p);

        Point2D[] contains = insert.clone();
        StdRandom.shuffle(contains);

        for (Point2D p : contains)
            if (!kdTree.contains(p)) {
                StdOut.println(p);
                assert false;
            }

        for (Point2D p : notContains)
            if (kdTree.contains(p)) {
                StdOut.println(p);
                assert false;
            }
    }

    private static void testNearest(
            Point2D[] insert,
            Point2D query,
            Point2D expected,
            double expectedDistance
    ) {
        if (DEBUG) pointNames = new RedBlackBST<>();
        StdOut.println("testNearest");
        KdTree kdTree = new KdTree();

        byte i = 0;
        for (Point2D p : insert) {
            kdTree.insert(p);
            if (DEBUG) pointNames.put(p, Character.toString(i++ + 65));
        }

        Point2D nearest = kdTree.nearest(query);

        assert nearest.equals(expected);
        assert nearest.distanceSquaredTo(query) == expectedDistance;
        if (DEBUG) pointNames = null;
    }
}
