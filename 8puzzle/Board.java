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
    private final String boardString;
    private final boolean isGoal;
    private final int blankTitleIndex;
    private final int hamming;
    private final int manhattan;

    class BoardInitializer {
        private int n;
        private int n2;
        private boolean isGoal;
        private int hammingAccumulator;
        private int manhattanAccumulator;
        private int blankTitleIndex;
        private char[] singleDimensionTiles;
        // 128 is max n + whitespace = 4 symbols
        // 128*128 is 5 symbols + whitespace = 6 symbols
        // n2 * 6 + 4
        private StringBuilder sb;
        private boolean neighbor;

        BoardInitializer(int n, boolean neighbor) {
            this.n = n;
            this.n2 = n * n;
            this.neighbor = neighbor;
            sb = new StringBuilder();
            sb.append(n).append(System.lineSeparator());
            isGoal = true;
            blankTitleIndex = 0;
            // todo
            singleDimensionTiles = new char[n2];

            if (neighbor) return;

            hammingAccumulator = 0;
            manhattanAccumulator = 0;
        }

        private void tile(int tile, int i, int j) {
            if (tile > 127) throw new IllegalArgumentException();

            int k = toSingleDimension(i, j, n);
            char value = (char) tile;

            singleDimensionTiles[k] = value;

            if (isBlankTile(value)) {
                blankTitleIndex = k;
            }
            else {
                if (value != k + 1) {
                    isGoal = false;

                    if (!neighbor) {
                        hammingAccumulator++;
                        manhattanAccumulator += Math.abs(i - toRow(value, n))
                                + Math.abs(j + 1 - toCol(value, n));
                    }
                }
                if (isGoal && k > 0) isGoal = singleDimensionTiles[k - 1] < value;
            }
            // string
            sb.append(" ").append(tile);

            if (j == n - 1) sb.append(System.lineSeparator());
        }
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();

        BoardInitializer initializer = new BoardInitializer(tiles.length, false);

        for (int i = 0; i < tiles.length; i++)
            for (int j = 0; j < tiles[i].length; j++)
                initializer.tile(tiles[i][j], i, j);

        n = initializer.n;
        n2 = initializer.n2;
        singleDimensionTiles = initializer.singleDimensionTiles.clone();
        isGoal = initializer.isGoal;
        hamming = initializer.hammingAccumulator;
        manhattan = initializer.manhattanAccumulator;
        blankTitleIndex = initializer.blankTitleIndex;
        boardString = initializer.sb.toString();
    }

    // neighbor constructor
    public Board(char[] singleDimensionTiles, int hamming, int manhattan) {
        int n = (int) Math.sqrt(singleDimensionTiles.length);
        BoardInitializer initializer = new BoardInitializer(n, true);

        for (int k = 0; k < singleDimensionTiles.length; k++) {
            int i = toRow(k, n);
            int j = toCol(k, n);
            initializer.tile(singleDimensionTiles[k], i, j);
        }

        n2 = initializer.n2;
        isGoal = initializer.isGoal;
        blankTitleIndex = initializer.blankTitleIndex;
        boardString = initializer.sb.toString();
        this.n = initializer.n;
        this.singleDimensionTiles = initializer.singleDimensionTiles.clone();
        this.hamming = hamming;
        this.manhattan = manhattan;
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
        // todo
        Bag<Board> neighbors = new Bag<>();
        int col = toCol(blankTitleIndex, singleDimensionTiles.length);
        int row = toRow(blankTitleIndex, singleDimensionTiles.length);
        // left
        if (col != 0) {
            int leftIndex = blankTitleIndex - 1;
            char[] leftSingleDimensionTiles = singleDimensionTiles.clone();
            // todo hamming + manhattan
            boolean decrease = leftSingleDimensionTiles[leftIndex] == blankTitleIndex;
            int hamming = this.hamming + (decrease ? -1 : 1);
            int manhattan = this.manhattan + (decrease ? -1 : 1);
            swap(leftSingleDimensionTiles, blankTitleIndex - 1, blankTitleIndex);
            neighbors.add(new Board(leftSingleDimensionTiles, hamming, manhattan));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int leftIndex, rightIndex;

        do {
            leftIndex = StdRandom.uniform(n2);
            rightIndex = leftIndex == n2 - 1 ? 0 : leftIndex + 1;
        } while (!isBlankTileIndex(leftIndex) && !isBlankTileIndex(rightIndex));

        char[] twinSingleDimensionTiles = singleDimensionTiles.clone();
        swap(twinSingleDimensionTiles, leftIndex, rightIndex);

        // todo hamming + manhattan
        return new Board(twinSingleDimensionTiles, -1, -1);
    }

    private boolean isBlankTileIndex(int index) {
        return isBlankTile(singleDimensionTiles[index]);
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

    private static boolean isBlankTile(char value) {
        return value == 0;
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

        Board b6 = new Board(new int[][] {
                { 2, 0, 1 },
                { 4, 3, 6 },
                { 8, 7, 5 },
                });
        // neighbors left (h:6, m:9) right (h:6, m:8) bottom (h:6, m:8)

        Board b7 = new Board(new int[][] {
                { 2, 3, 1 },
                { 4, 7, 6 },
                { 8, 0, 5 },
                });
        // neighbors left top right

        // private
        // clone
        Board bClone = new Board(b.singleDimensionTiles, -1, -1);
        assert bClone != b && bClone.equals(b);
        assert bClone.n == b.n;
        assert bClone.n2 == b.n2;
        assert Arrays.equals(bClone.singleDimensionTiles, b.singleDimensionTiles);
        assert bClone.isGoal == b.isGoal;
        assert bClone.hamming == -1;
        assert bClone.manhattan == -1;
        assert bClone.boardString.equals(b.boardString);

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
