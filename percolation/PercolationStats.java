/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 6/03/2019
 *  Description: Use this class for process Monte Carlo simulation of the
 *               Percolation class and API for recive stats, like sample mean,
 *               standard deviation, low/high endpoint of 95% confidence interval.
 *               You can configure grid size (n) and trials count via
 *               PercolationStats constuctor.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int T;
    private double[] percolationThersholds;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        T = trials;
        percolationThersholds = new double[trials];
        int n1 = n + 1;
        double n2 = n * n;

        for (int i = 0; i < T; i++) {
            Percolation percolation = new Percolation(n);
            int openSitesCount = 0;

            do {
                int row, col;
                do {
                    row = StdRandom.uniform(1, n1);
                    col = StdRandom.uniform(1, n1);
                } while (percolation.isOpen(row, col));

                percolation.open(row, col);
                openSitesCount++;
            } while (!percolation.percolates());

            percolationThersholds[i] = openSitesCount / n2;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolationThersholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolationThersholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats percolationStats = new PercolationStats(n, T);

        StdOut.printf(
                "mean                    = %g\n",
                percolationStats.mean()
        );
        StdOut.printf(
                "stddev                  = %g\n",
                percolationStats.stddev()
        );
        StdOut.printf(
                "95%% confidence interval = [%g, %g]\n",
                percolationStats.confidenceLo(),
                percolationStats.confidenceHi()
        );
    }
}
