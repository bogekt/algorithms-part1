/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private final int n2;
    private final int n;
    private final char[] singleDimensionTiles;
    private final String boardString;
    private final boolean isGoal;
    private final int hamming;
    private final int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();

        n = tiles.length;
        n2 = n * n;

        singleDimensionTiles = new char[n2];
        boolean canBeGoal = true;
        int hammingCounter = 0;
        int manhattanCounter = 0;
        // 128 is max n + whitespace = 4 symbols
        // 128*128 is 5 symbols + whitespace = 6 symbols
        // n2 * 6 + 4
        StringBuilder sb = new StringBuilder();
        sb.append(n).append(System.lineSeparator());

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (j >= tiles[i].length) throw new IllegalArgumentException();

                int value = tiles[i][j];
                if (value > 127) throw new IllegalArgumentException();

                int k = toSingleDimension(i, j);
                singleDimensionTiles[k] = (char) value;

                if (notLast(k)) {
                    if (k > 0) canBeGoal = singleDimensionTiles[k - 1] < value;
                    if (singleDimensionTiles[k] != k + 1) hammingCounter++;
                    manhattanCounter += Math.abs(singleDimensionTiles[k] - (k + 1));
                }
                else if (value != 0) {
                    canBeGoal = false;
                    hammingCounter++;
                    manhattanCounter++;
                }

                if (value == 0) {
                    // TODO neighbors
                }
                // string
                sb.append(" ").append(value);
            }
            sb.append(System.lineSeparator());
        }

        isGoal = canBeGoal;
        hamming = hammingCounter;
        manhattan = manhattanCounter;
        boardString = sb.toString();
    }

    private boolean notLast(int k) {
        return k < n2 - 1;
    }

    // copy constructor
    private Board(Board other) {
        n = other.n;
        n2 = other.n2;
        singleDimensionTiles = other.singleDimensionTiles.clone();
        isGoal = other.isGoal;
        hamming = other.hamming;
        manhattan = other.manhattan;
        boardString = other.boardString;
    }

    // string representation of this board
    public String toString() {
        return boardString;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return isGoal;
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

        Board twin = new Board(this);
        swap(twin.singleDimensionTiles, left, right);

        return twin;
    }

    private int toSingleDimension(int row, int col) {
        return row * n + col;
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

        Board b2 = new Board(new int[][] {
                { 0, 2 },
                { 1, 3 },
                });

        // same as b
        Board b3 = new Board(new int[][] {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 },
                });

        // private
        // clone
        Board bClone = new Board(b);
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
        assert b3.equals(b) && b.equals(b3);


        // goal
        assert b.isGoal() && b3.isGoal() && !b2.isGoal();
        // swap(b.singleDimensionTiles, 3, 4);
        // assert !b.isGoal();
        // swap(b.singleDimensionTiles, 3, 4);
        // assert b.isGoal();
        // assert !b2.isGoal();

        // twins
    }
}
