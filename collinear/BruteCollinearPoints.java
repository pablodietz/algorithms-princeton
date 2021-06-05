/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date:
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private LineSegment[] segments = new LineSegment[0];
    private int nOfSegs = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // Throw an IllegalArgumentException if the argument to the constructor is null.
        if (points == null)
            throw new IllegalArgumentException("the argument to the constructor is null");
        // Throw an IllegalArgumentException if any point in the array is null.
        for (Point point : points)
            if (point == null)
                throw new IllegalArgumentException("any point in the array is null");

        Point[] pointsCopy = points.clone();
        Arrays.sort(pointsCopy);
        int n = pointsCopy.length;

        // Throw an IllegalArgumentException if array contains a repeated points.
        for (int i = 0; i < n - 1; i++)
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0)
                throw new IllegalArgumentException("array contains a repeated points");

        Point[] fourPoints = new Point[4];
        for (int ip = 0; ip < n - 3; ip++) {
            fourPoints[0] = pointsCopy[ip];
            for (int iq = ip + 1; iq < n - 2; iq++) {
                fourPoints[1] = pointsCopy[iq];
                for (int ir = iq + 1; ir < n - 1; ir++) {
                    fourPoints[2] = pointsCopy[ir];
                    for (int is = ir + 1; is < n; is++) {
                        fourPoints[3] = pointsCopy[is];
                        if (areCollinear(fourPoints)) {
                            addSegment(fourPoints[0], fourPoints[3]);
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return nOfSegs;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    private static boolean areCollinear(Point[] a) {
        if (a.length < 3) return false;
        Comparator<Point> c = a[0].slopeOrder();
        boolean result = true;
        for (int i = 1; i < a.length - 1; i++)
            result &= (c.compare(a[i], a[i + 1]) == 0);
        return result;
    }

    // resize array
    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < nOfSegs; i++) copy[i] = segments[i];
        segments = copy;
    }

    // add segment at the end
    private void addSegment(Point p0, Point p1) {
        resize(nOfSegs + 1);
        segments[nOfSegs++] = new LineSegment(p0, p1);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
