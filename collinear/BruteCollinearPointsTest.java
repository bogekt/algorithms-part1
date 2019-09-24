/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class BruteCollinearPointsTest {
    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();
        LinkedList<Point> pointsLinkedList = new LinkedList<Point>();

        while (!in.isEmpty()) {
            int x = in.readInt();
            int y = in.readInt();

            pointsLinkedList.add(new Point(x, y));
        }

        assert n == pointsLinkedList.size();

        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(
                pointsLinkedList.toArray(new Point[0]));

        for (LineSegment lineSegment : bruteCollinearPoints.segments()) {
            StdOut.println(lineSegment.toString());
        }

        StdOut.println();
        StdOut.println("Total number of segments:" + bruteCollinearPoints.numberOfSegments());
    }
}


