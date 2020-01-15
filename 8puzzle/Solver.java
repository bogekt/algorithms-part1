/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 14/01/2020
 *  Description: Solver of 8 puzzle game using A* algorithm and Board api
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    private static final boolean TRACE = false;
    private static final SearchNodePriorityComparator SEARCH_NODE_PRIORITY_COMPARATOR
            = new SearchNodePriorityComparator();

    private SearchNode goalNode;
    private SearchNode twinGoalNode;

    private static final class SearchNodePriorityComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode o1, SearchNode o2) {
            if (o1 == null || o2 == null) throw new NullPointerException();

            return o1.priority() - o2.priority();
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private static final int NOT_MEMOIZED = -1;
        private static final boolean MANHATTAN_PRIORITY = true;

        private final Board board;
        private final int move;
        private final SearchNode previous;

        private int boardPriority = NOT_MEMOIZED;

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
            return SEARCH_NODE_PRIORITY_COMPARATOR.compare(this, that);
        }

        public String toString() {
            return "priority = "
                    + priority()
                    + System.lineSeparator()
                    + "move = "
                    + move
                    + System.lineSeparator()
                    + boardPriorityName()
                    + " = "
                    + boardPriority()
                    + System.lineSeparator()
                    + board;
        }

        private String boardPriorityName() {
            return MANHATTAN_PRIORITY ? "manhattan" : "hamming";
        }

        private int boardPriority() {
            if (boardPriority == NOT_MEMOIZED)
                boardPriority = MANHATTAN_PRIORITY
                                ? board.manhattan()
                                : board.hamming();

            return boardPriority;
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        if (TRACE) StdOut.println("TRACE");

        final MinPQ<SearchNode> queue = new MinPQ<>();
        final MinPQ<SearchNode> twinQueue = new MinPQ<>();
        queue.insert(new SearchNode(initial, 0, null));
        twinQueue.insert(new SearchNode(initial.twin(), 0, null));

        int step = 0;

        while (!queue.isEmpty() && goalNode == null && twinGoalNode == null) {
            if (TRACE) {
                StdOut.println("Main queue");
                trace(step, queue);

                StdOut.println("Twin queue");
                trace(step, twinQueue);
            }

            goalNode = aStarIteration(queue);
            twinGoalNode = aStarIteration(twinQueue);

            step++;
        }
    }

    private SearchNode aStarIteration(MinPQ<SearchNode> queue) {
        SearchNode current = queue.delMin();

        if (current.board.isGoal()) return current;

        for (SearchNode neighbor : current.neighbors())
            if (current.previous == null || !neighbor.board.equals(current.previous.board))
                queue.insert(neighbor);

        return null;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return goalNode != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        return goalNode.move;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Stack<Board> boards = new Stack<>();

        SearchNode node = goalNode;
        while (node != null) {
            boards.push(node.board);
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


    private static void trace(int step, MinPQ<SearchNode> queue) {
        StringBuilder sb = new StringBuilder().append("Step ").append(step).append(":\t");
        ArrayList<SearchNode> list = new ArrayList<>();
        queue.iterator().forEachRemaining(list::add);
        list.sort(SEARCH_NODE_PRIORITY_COMPARATOR);

        int n = list.get(0).board
                .toString()
                .split(System.lineSeparator())[1]
                .split(" ")
                .length;

        String textTabs = "\t";

        for (SearchNode node : list)
            sb.append("priority = ").append(node.priority()).append(textTabs);
        sb.append(System.lineSeparator()).append("\t\t");

        for (SearchNode node : list)
            sb.append("move = ").append(node.move).append(textTabs + "\t");
        sb.append(System.lineSeparator()).append("\t\t");

        for (SearchNode node : list)
            sb.append(node.boardPriorityName() + " = ").append(node.boardPriority())
              .append(textTabs);
        sb.append(System.lineSeparator()).append("\t\t");

        for (int i = 0; i < n; i++) {
            for (SearchNode node : list) {
                sb.append(
                        node.board
                                .toString()
                                .split(System.lineSeparator())[i]
                ).append(textTabs);

                if (i == 0)
                    sb.append("\t\t\t");
                else
                    sb.append("\t\t");
            }
            sb.append(System.lineSeparator()).append("\t\t");
        }

        StdOut.println(sb);
    }
}
