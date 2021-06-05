/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date: 08/04/2020
 *  Compilation:  javac Percolation.java
 *  Dependencies: WeightedQuickUnionUF.java
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private final int sourceId;
    private final int sinkId;
    private final WeightedQuickUnionUF ufSourceSink;
    private final WeightedQuickUnionUF ufSource;
    private int openSitesCounter = 0;
    private boolean[][] grid;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be greater than or equal to zero");
        this.n = n;
        sourceId = 0;
        sinkId = n * n + 1;
        grid = new boolean[n][n];
        ufSourceSink = new WeightedQuickUnionUF(n * n + 2);
        ufSource = new WeightedQuickUnionUF(n * n + 1);
    }

    // check site (row, col) values
    private void checkRowCol(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException(
                    "row and column indices must be integers between 1 and n");
    }

    // get id from site (row, col)
    private int getSiteId(int row, int col) {
        checkRowCol(row, col);
        return col + n * (row - 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRowCol(row, col);
        if (!isOpen(row, col)) {
            int newSiteId = getSiteId(row, col);
            grid[row - 1][col - 1] = true;
            openSitesCounter++;
            if (row == 1) {
                ufSourceSink.union(newSiteId, sourceId);
                ufSource.union(newSiteId, sourceId);
            }
            else if (isOpen(row - 1, col)) {
                ufSourceSink.union(newSiteId, getSiteId(row - 1, col));
                ufSource.union(newSiteId, getSiteId(row - 1, col));
            }
            if (row == n) ufSourceSink.union(newSiteId, sinkId);
            else if (isOpen(row + 1, col)) {
                ufSourceSink.union(newSiteId, getSiteId(row + 1, col));
                ufSource.union(newSiteId, getSiteId(row + 1, col));
            }
            if (col != 1 && isOpen(row, col - 1)) {
                ufSourceSink.union(newSiteId, getSiteId(row, col - 1));
                ufSource.union(newSiteId, getSiteId(row, col - 1));
            }
            if (col != n && isOpen(row, col + 1)) {
                ufSourceSink.union(newSiteId, getSiteId(row, col + 1));
                ufSource.union(newSiteId, getSiteId(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowCol(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRowCol(row, col);
        return ufSource.find(sourceId) == ufSource.find(getSiteId(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCounter;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufSourceSink.find(sourceId) == ufSourceSink.find(sinkId);
    }
}
