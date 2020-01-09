/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Solver {
    // private final Board initial;
    private MinPQ<SearchNode> priorityQueue;
    private SearchNode goalNode;

    private class SearchNode implements Comparable<SearchNode> {
        private static final int notMemoized = -1;

        private final Board board;
        private final int move;
        private final SearchNode previous;

        private int boardPriority = notMemoized;

        SearchNode(Board board, int move, SearchNode previous) {
            this.board = board;
            this.move = move;
            this.previous = previous;
        }

        public int priority() {
            return move + boardPriority();
        }

        public Iterable<SearchNode> neighbors() {
            Bag<SearchNode> neighbors = new Bag<>();

            for (Board neighborBoard : board.neighbors())
                neighbors.add(new SearchNode(neighborBoard, move + 1, this));

            return neighbors;
        }

        public int compareTo(SearchNode that) {
            if (that == null) throw new NullPointerException();

            return priority() - that.priority();
        }

        private int boardPriority() {
            if (boardPriority == notMemoized)
                boardPriority = board.manhattan();

            return boardPriority;
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        // this.initial = initial;
        priorityQueue = new MinPQ<>();
        priorityQueue.insert(new SearchNode(initial, 0, null));
        aStar();
    }

    private void aStar() {
        while (!priorityQueue.isEmpty()) {
            SearchNode current = priorityQueue.delMin();

            if (current.board.isGoal()) {
                goalNode = current;
                break;
            }

            for (SearchNode neighbor : current.neighbors())
                if (current.previous == null || !neighbor.board.equals(current.previous.board))
                    priorityQueue.insert(neighbor);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board
    public int moves() {
        return goalNode.move;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        ArrayList<Board> boards = new ArrayList<>();

        SearchNode node = goalNode;
        while (node != null) {
            boards.add(node.board);
            node = node.previous;
        }

        return boards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
