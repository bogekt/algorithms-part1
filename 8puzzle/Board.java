/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private final int n2;
    private final int n;
    private final char[] singleDimensionTiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();

        n = tiles.length;
        n2 = n * n;
        singleDimensionTiles = new char[n2];

        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles[i].length; j++)
                if (tiles[i][j] > 127) throw new IllegalArgumentException();
                else singleDimensionTiles[toSingleDimension(i, j, n)] = (char) tiles[i][j];
    }

    // copy constructor
    public Board(char[] singleDimensionTiles, int n, int n2) {
        this.n = n;
        this.n2 = n2;
        this.singleDimensionTiles = singleDimensionTiles.clone();
        assert n * n == n2;
        assert singleDimensionTiles.length == n2;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(System.lineSeparator());

        for (int k = 0; k < n2; k++) {
            sb.append(" ").append(Integer.valueOf(singleDimensionTiles[k]));
            if (toCol(k, n) == n - 1) sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;

        for (int k = 0; k < n2; k++)
            if (isBlankTileIndex(k) || isGoalTile(k)) continue;
            else hamming++;

        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;

        for (int k = 0; k < n2; k++) {
            char tile = singleDimensionTiles[k];

            if (isBlankTileIndex(k) || isGoalTile(k)) continue;

            int i = toRow(k, n);
            int j = toCol(k, n);

            manhattan += Math.abs(i - toRow(tile, n)) + Math.abs(j + 1 - toCol(tile, n));
        }

        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (!isBlankTileIndex(n2 - 1)) return false;

        for (int k = 0; k < n2 - 1; k++)
            if (!isGoalTile(k)) return false;
            else if (isBlankTileIndex(k)) return false;

        return true;
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;

        return Arrays.equals(singleDimensionTiles, that.singleDimensionTiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // todo
        Bag<Board> neighbors = new Bag<>();
        // int col = toCol(blankTitleIndex, singleDimensionTiles.length);
        // int row = toRow(blankTitleIndex, singleDimensionTiles.length);
        // // left§
        // if (col != 0) {
        //     int leftIndex = blankTitleIndex - 1;
        //     char[] leftSingleDimensionTiles = singleDimensionTiles.clone();
        //     // todo hamming + manhattan
        //     boolean decrease = leftSingleDimensionTiles[leftIndex] == blankTitleIndex;
        //     // int hamming = this.hamming + (decrease ? -1 : 1);
        //     // int manhattan = this.manhattan + (decrease ? -1 : 1);
        //     swap(leftSingleDimensionTiles, blankTitleIndex - 1, blankTitleIndex);
        //     // neighbors.add(new Board(leftSingleDimensionTiles, hamming, manhattan));
        // }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int leftIndex, rightIndex;
        int blankTileIndex = blankTileIndex();

        do {
            leftIndex = StdRandom.uniform(n2);
            rightIndex = leftIndex == n2 - 1 ? 0 : leftIndex + 1;
        } while (leftIndex != blankTileIndex && rightIndex != blankTileIndex);

        char[] twinSingleDimensionTiles = singleDimensionTiles.clone();
        swap(twinSingleDimensionTiles, leftIndex, rightIndex);

        return new Board(twinSingleDimensionTiles, n, n2);
    }

    // does tile is the same, as in goal board
    private boolean isGoalTile(int index) {
        return singleDimensionTiles[index] == index + 1;
    }

    private int blankTileIndex() {
        for (int k = 0; k < n2; k++)
            if (isBlankTileIndex(k)) return k;
        throw new IndexOutOfBoundsException();
    }

    private boolean isBlankTileIndex(int index) {
        return singleDimensionTiles[index] == 0;
    }

    private static int toSingleDimension(int row, int col, int n) {
        return row * n + col;
    }

    private static int toRow(int k, int n) {
        return Math.floorDiv(k, n);
    }

    private static int toCol(int k, int n) {
        return k % n;
    }

    private static void swap(char[] array, int i, int j) {
        char temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board b = new Board(new int[][] {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 },
                });
        // neighbors left (h:1, m:1) top (h:1, m:1)

        Board b2 = new Board(new int[][] {
                { 0, 2 },
                { 1, 3 },
                });
        // neighbors right (h:3, m:3) bottom (h:1, m:1)

        // same as b
        Board b3 = new Board(new int[][] {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 },
                });

        Board b4 = new Board(new int[][] {
                { 0, 2, 1 },
                { 4, 3, 6 },
                { 8, 7, 5 },
                });
        // neighbors right (h:5, m:8) bottom (h:5, m:8)

        Board b5 = new Board(new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 },
                });
        // neighbors left (h:6, m:11) top (h:5, m:11) right (h:5, m:9) bottom (h:5, m:9)

        // Board b6 = new Board(new int[][] {
        //         { 2, 0, 1 },
        //         { 4, 3, 6 },
        //         { 8, 7, 5 },
        //         });
        // // neighbors left (h:6, m:9) right (h:6, m:8) bottom (h:6, m:8)
        //
        // Board b7 = new Board(new int[][] {
        //         { 2, 3, 1 },
        //         { 4, 7, 6 },
        //         { 8, 0, 5 },
        //         });
        // // neighbors left top right

        // private
        // clone
        Board bClone = new Board(b.singleDimensionTiles, b.n, b.n2);
        assert bClone != b && bClone.equals(b);
        assert bClone.n == b.n;
        assert bClone.n2 == b.n2;
        assert Arrays.equals(bClone.singleDimensionTiles, b.singleDimensionTiles);

        // dimension
        assert b.dimension() == 3;
        assert b2.dimension() == 2;

        // string
        String bString = b.toString();
        StdOut.print(bString);
        assert bString.startsWith(String.valueOf(b.dimension()));
        assert bString.contains(" 1 2 3");
        assert bString.contains(" 4 5 6");
        assert bString.contains(" 7 8 0");
        String b2String = b2.toString();
        StdOut.print(b2String);
        assert b2String.startsWith(String.valueOf(b2.dimension()));
        assert b2String.contains(" 0 2");
        assert b2String.contains(" 1 3");

        // equals
        // null
        assert !b.equals(null) && !b2.equals(null) && !b3.equals(null);
        // object
        Object o = new Object();
        assert !b.equals(o) && !b2.equals(o) && !b3.equals(o);
        // same type
        assert !b.equals(b2) && !b2.equals(b) && !b3.equals(b2) && !b2.equals(b3);
        // equal
        assert b.equals(b) && b2.equals(b2) && b3.equals(b3);
        assert !b.equals(b4) && !b4.equals(b);
        assert b3.equals(b) && b.equals(b3);


        // goal
        assert b.isGoal() && b3.isGoal();
        assert !b2.isGoal() && !b4.isGoal();

        // hamming
        assert b.hamming() == 0 && b3.hamming() == 0;
        assert b2.hamming() == 2;
        assert b4.hamming() == 5;
        assert b5.hamming() == 5;

        // manhattan
        assert b.manhattan() == 0 && b3.manhattan() == 0;
        assert b2.manhattan() == 2;
        assert b4.manhattan() == 8;
        assert b5.manhattan() == 10;

        // neighbors
        // left
        assert !b2.neighbors().iterator().hasNext();

        // twins
        // todo
    }
}
