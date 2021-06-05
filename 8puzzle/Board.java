/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date:
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Board {
    private final int[][] tiles;
    private final int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = new int[n][];
        for (int i = 0; i < n; i++) this.tiles[i] = tiles[i].clone();
    }

    // string representation of this board
    public String toString() {
        String str = "";
        str += tiles.length + "\n";
        for (int[] row : tiles) {
            for (int value : row) str += " " + value;
            str += "\n";
        }
        return str;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++) {
                int value = tiles[row][col];
                if (value == 0) continue;
                if (value != row * n + col + 1) distance++;
            }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++) {
                int value = tiles[row][col];
                if (value == 0) continue;
                distance += Math.abs((value - 1) / n - row) + Math.abs((value - 1) % n - col);
            }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n) return false;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (that.tiles[row][col] != this.tiles[row][col]) return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Neighbors(this.tiles);
    }

    private class Neighbors implements Iterable<Board> {
        private final int[][] tiles;

        public Neighbors(int[][] tiles) {
            this.tiles = tiles;
        }

        public Iterator<Board> iterator() {
            return new NeighborsIterator(tiles);
        }
    }

    private static class NeighborsIterator implements Iterator<Board> {
        private static final int[] INCS = { 0, -1, 0, 1, 0 };
        private final int[][] tiles;
        private final int n;
        private BoardSquare blank;
        private Queue<BoardSquare> neighbors = new Queue<BoardSquare>();

        public NeighborsIterator(int[][] tiles) {
            this.tiles = tiles;
            n = tiles.length;
            for (int row = 0; row < n; row++)
                for (int col = 0; col < n; col++)
                    if (tiles[row][col] == 0) {
                        blank = new BoardSquare(row, col);
                        break;
                    }
            for (int i = 0; i < INCS.length - 1; i++) {
                int row = blank.row + INCS[i + 1];
                int col = blank.col + INCS[i];
                if (row >= 0 && row < n && col >= 0 && col < n)
                    neighbors.enqueue(new BoardSquare(row, col));
            }
        }

        public boolean hasNext() {
            return !neighbors.isEmpty();
        }

        public Board next() {
            int[][] tilesCopy = new int[n][];
            for (int i = 0; i < n; i++) tilesCopy[i] = this.tiles[i].clone();
            exch(tilesCopy, blank, neighbors.dequeue());
            return new Board(tilesCopy);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] tilesCopy = new int[n][];

        BoardSquare[] swaps = { new BoardSquare(0, 0), new BoardSquare(0, 0) };
        for (int i = 0; i < n; i++) tilesCopy[i] = this.tiles[i].clone();
        for (int row = 0, k = 0; row < n && k < 2; row++)
            for (int col = 0; col < n && k < 2; col++)
                if (tiles[row][col] != 0) swaps[k++] = new BoardSquare(row, col);
        exch(tilesCopy, swaps[0], swaps[1]);
        return new Board(tilesCopy);
    }

    private static final class BoardSquare {
        final int row;
        final int col;

        public BoardSquare(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static void exch(int[][] a, BoardSquare bs1, BoardSquare bs2) {
        int swap = a[bs1.row][bs1.col];
        a[bs1.row][bs1.col] = a[bs2.row][bs2.col];
        a[bs2.row][bs2.col] = swap;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles1_3x3 = {
                { 2, 1, 3 },
                { 4, 8, 0 },
                { 7, 6, 5 }
        };

        int[][] tiles2_3x3 = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        };

        int[][] tiles1_4x4 = {
                { 8, 1, 3, 9 },
                { 4, 0, 2, 10 },
                { 7, 6, 5, 11 },
                { 12, 13, 14, 15 }
        };

        Board board = new Board(tiles1_3x3);
        StdOut.println(board);
        StdOut.println("hamming: " + board.hamming());
        StdOut.println("manhattan: " + board.manhattan());
        StdOut.println("is goal?: " + board.isGoal());
        StdOut.println("equals?: " + board.equals(new Board(tiles1_3x3)));
        StdOut.println("equals?: " + board.equals(new Board(tiles2_3x3)));
        StdOut.println("equals?: " + board.equals(new Board(tiles1_4x4)));
        for (Board neighbor : board.neighbors()) StdOut.println(neighbor.toString());
        StdOut.println(board.twin());
    }

}
