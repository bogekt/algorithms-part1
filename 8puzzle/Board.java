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

    private static boolean isBlankTile(char value) {
        return value == 0;
    }

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

                int origin = tiles[i][j];
                if (origin > 127) throw new IllegalArgumentException();

                int k = toSingleDimension(i, j);
                char value = (char) origin;
                singleDimensionTiles[k] = value;

                if (!isBlankTile(value)) {
                    if (value != k + 1) {
                        canBeGoal = false;
                        hammingCounter++;
                        manhattanCounter += Math.abs(i - toRow(value))
                                + Math.abs(j + 1 - toCol(value));
                    }
                    if (canBeGoal && k > 0) canBeGoal = singleDimensionTiles[k - 1] < value;
                }
                else if (isBlankTile(value)) {
                    // TODO neighbors
                }
                // string
                sb.append(" ").append(origin);
            }
            sb.append(System.lineSeparator());
        }

        isGoal = canBeGoal;
        hamming = hammingCounter;
        manhattan = manhattanCounter;
        boardString = sb.toString();
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
        int leftIndex, rightIndex;

        do {
            leftIndex = StdRandom.uniform(n2);
            rightIndex = leftIndex == n2 - 1 ? 0 : leftIndex + 1;
        } while (!isBlankTileIndex(leftIndex) && !isBlankTileIndex(rightIndex));

        Board twin = new Board(this);
        swap(twin.singleDimensionTiles, leftIndex, rightIndex);

        return twin;
    }

    private boolean isBlankTileIndex(int index) {
        return isBlankTile(singleDimensionTiles[index]);
    }

    private int toSingleDimension(int row, int col) {
        return row * n + col;
    }

    private int toRow(int k) {
        return Math.floorDiv(k, n);
    }

    private int toCol(int k) {
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

        Board b4 = new Board(new int[][] {
                { 0, 2, 1 },
                { 4, 3, 6 },
                { 8, 7, 5 },
                });

        Board b5 = new Board(new int[][] {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 },
                });

        // private
        // clone
        Board bClone = new Board(b);
        assert bClone != b && bClone.equals(b);
        assert bClone.n == b.n;
        assert bClone.n2 == b.n2;
        assert Arrays.equals(bClone.singleDimensionTiles, b.singleDimensionTiles);
        assert bClone.isGoal == b.isGoal;
        assert bClone.hamming == b.hamming;
        assert bClone.manhattan == b.manhattan;
        assert bClone.boardString == b.boardString;

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

        // twins
        // TODO
    }
}
