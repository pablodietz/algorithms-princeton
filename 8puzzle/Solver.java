/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Solver {
    private SearchNode solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solution = new SearchNode(initial, 0, null);
        AStarAlg init = new AStarAlg(solution);
        AStarAlg twin = new AStarAlg(new SearchNode(initial.twin(), 0, null));
        while (init.hasNext() && twin.hasNext()) {
            solution = init.next();
            twin.next();
        }
    }

    private class AStarAlg implements Iterator<SearchNode> {
        private SearchNode previous;
        private MinPQ<SearchNode> pq;

        public AStarAlg(SearchNode searchNode) {
            pq = new MinPQ<SearchNode>();
            previous = searchNode;
        }

        public boolean hasNext() {
            return !previous.board.isGoal();
        }

        public SearchNode next() {
            for (Board neighbor : previous.board.neighbors()) {
                if (previous.moves > 0 && neighbor.equals(previous.previous.board)) continue;
                pq.insert(new SearchNode(neighbor, previous.moves + 1, previous));
            }
            previous = pq.delMin();
            return previous;
        }
    }

    private final class SearchNode implements Comparable<SearchNode> {
        final Board board;
        final int moves;
        final SearchNode previous;
        private final int priority;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
            this.previous = previous;
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution.board.isGoal();
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) return solution.moves;
        else return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (isSolvable()) return new Boards(solution);
        else return null;
    }

    private class Boards implements Iterable<Board> {
        private final SearchNode lastNode;

        public Boards(SearchNode lastNode) {
            this.lastNode = lastNode;
        }

        public Iterator<Board> iterator() {
            return new BoardsIterator(lastNode);
        }
    }

    private class BoardsIterator implements Iterator<Board> {
        private Stack<Board> stack = new Stack<Board>();

        public BoardsIterator(SearchNode lastNode) {
            SearchNode node = lastNode;
            while (node != null) {
                stack.push(node.board);
                node = node.previous;
            }
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public Board next() {
            return stack.pop();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
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
