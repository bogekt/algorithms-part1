/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class Board implements Cloneable {
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


        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                if (j >= tiles[i].length) throw new IllegalArgumentException();
                int value = tiles[i][j];
                if (value > 127) throw new IllegalArgumentException();
                setTile(i, j, (char) value);
            }
    }

    private Board(char[] singleDimensionTiles) {
        n = (int) Math.sqrt(singleDimensionTiles.length);
        n2 = singleDimensionTiles.length;
        this.singleDimensionTiles = singleDimensionTiles.clone();
    }

    // string representation of this board
    public String toString() {
        // 128 is max n + whitespace = 4 symbols
        // 128*128 is 5 symbols + whitespace = 6 symbols
        // n2 * 6 + 4
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(System.lineSeparator());

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(" ").append(tile(i, j));
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return -1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return -1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 1; i < n2; i++)
            if (!less(i - 1, i)) return false;

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
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int left, right;

        do {
            left = StdRandom.uniform(n2);
            right = left == n2 - 1 ? 0 : left + 1;
        } while (left != 0 && right != 0);

        return new Board(singleDimensionTiles).swap(left, right);
    }

    public Board clone() {
        return new Board(singleDimensionTiles);
    }

    private int toSingleDimension(int row, int col) {
        return row * n + col;
    }

    private int tile(int row, int col) {
        return singleDimensionTiles[toSingleDimension(row, col)];
    }

    private Board setTile(int row, int col, char value) {
        singleDimensionTiles[toSingleDimension(row, col)] = value;

        return this;
    }

    private boolean less(int i, int j) {
        return singleDimensionTiles[i] < singleDimensionTiles[j];
    }

    private Board swap(int i, int j) {
        char temp = singleDimensionTiles[i];
        singleDimensionTiles[i] = singleDimensionTiles[j];
        singleDimensionTiles[j] = temp;

        return this;
    }

    private int blankTileIndex() {
        for (int i = 0; i < n2; i++)
            if (singleDimensionTiles[i] == 0) return i;
        throw new NoSuchElementException();
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        Board b = new Board(new int[][] {
                { 0, 1, 2 },
                { 3, 4, 5 },
                { 6, 7, 8 },
                });

        Board b2 = new Board(new int[][] {
                { 0, 1, 6 },
                { 8, 7, 5 },
                { 2, 4, 3 },
                });

        // same as b
        Board b3 = new Board(new int[][] {
                { 0, 1, 2 },
                { 3, 4, 5 },
                { 6, 7, 8 },
                });

        // string
        String bString = b.toString();
        StdOut.print(bString);
        assert bString.startsWith(String.valueOf(b.dimension()));
        assert bString.contains(" 0 1 2");
        assert bString.contains(" 3 4 5");
        assert bString.contains(" 6 7 8");

        // equals
        // null
        assert !b.equals(null) && !b2.equals(null) && !b3.equals(null);
        // object
        Object o = new Object();
        assert !b.equals(o) && !b2.equals(o) && !b3.equals(o);
        // same type
        assert !b.equals(b2) && !b2.equals(b) && !b3.equals(b2) && !b2.equals(b3);
        // equals
        assert b.equals(b) && b2.equals(b2) && b3.equals(b3);
        assert b3.equals(b) && b.equals(b3);


        // goal
        assert b.isGoal() && b3.isGoal() && !b2.isGoal();
        b.swap(3, 4);
        assert !b.isGoal();
        b.swap(3, 4);
        assert b.isGoal();
        assert !b2.isGoal();

        // clone
        Board bClone = b.clone();
        assert bClone != b && bClone.equals(b);
        assert bClone.isGoal();
        bClone.swap(3, 4);
        assert !bClone.isGoal();
        bClone.swap(3, 4);
        assert bClone.isGoal();

        // twins
    }
}
