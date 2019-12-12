/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

public class Solver {
    int moves = 0;
    Board previous = null;
    MinPQ<Board> priorityQueue = null;
    Comparator<Board> HammingComparator = new Comparator<Board>() {
        public int compare(Board a, Board b) {
            return a.hamming() - b.hamming();
        }
    };

    public Solver(Board initial) {
        priorityQueue = new MinPQ<>(HammingComparator);
        priorityQueue.insert(initial);
        AStrar();
    }

    private boolean AStrar() {
        while (!priorityQueue.isEmpty()) {
            Board current = priorityQueue.delMin();

            if (current.isGoal()) return true;

            for (Board neighbor : current.neighbors()) {
                if (neighbor.equals(previous)) continue;

                priorityQueue.insert(neighbor);
            }
        }

        return false;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {

    }
}
