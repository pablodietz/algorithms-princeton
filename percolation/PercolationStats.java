/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date: 08/04/2020
 *  Compilation:  javac PercolationStats.java
 *  Execution:    java PercolationStats n T
 *  Dependencies: Percolation.java StdOut.java StdRandom.java StdStats.java
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_FACTOR = 1.96;
    private final int trials;
    private final double pMean;
    private final double pStddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException(
                    "n and trials must be greater than or equal to zero");
        this.trials = trials;
        int[] coords;
        int[] numberOfOpenSitesArray = new int[trials];
        for (int trial = 0; trial < trials; trial++) {
            Percolation perc = new Percolation(n);
            for (int k : StdRandom.permutation(n * n)) {
                coords = getSiteCoords(k + 1, n);
                perc.open(coords[0], coords[1]);
                if (perc.percolates())
                    break;
            }
            numberOfOpenSitesArray[trial] = perc.numberOfOpenSites();
        }
        pMean = StdStats.mean(numberOfOpenSitesArray) / n / n;
        pStddev = StdStats.stddev(numberOfOpenSitesArray) / n / n;
    }

    // get site (row, col) coords from id
    private int[] getSiteCoords(int id, int n) {
        if (id < 1 || id > n * n)
            throw new IllegalArgumentException("id must be an integer between 1 and n*n");
        int[] coords = new int[2];
        coords[0] = (id - 1) / n + 1;
        coords[1] = id - n * (coords[0] - 1);
        return coords;
    }

    // sample mean of percolation threshold
    public double mean() {
        return pMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return pStddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return pMean - CONFIDENCE_FACTOR * pStddev / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return pMean + CONFIDENCE_FACTOR * pStddev / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        // n-by-n percolation system (read from command-line, default = 10)
        int n = 10;
        int trials = 30;
        if (args.length >= 1) n = Integer.parseInt(args[0]);
        if (args.length >= 2) trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.printf("mean\t\t\t= %f\n", percStats.mean());
        StdOut.printf("stddev\t\t\t= %f\n", percStats.stddev());
        StdOut.printf("95%% confidence interval\t= [%f; %f]\n", percStats.confidenceLo(),
                      percStats.confidenceHi());
    }
}
