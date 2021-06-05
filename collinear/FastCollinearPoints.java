/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date:
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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

        // Throw an IllegalArgumentException if array contains a repeated point.
        for (int i = 0; i < n - 1; i++)
            if (pointsCopy[i].compareTo(pointsCopy[i + 1]) == 0)
                throw new IllegalArgumentException("array contains a repeated points");

        for (int i = 0; i < n; i++) {
            Arrays.sort(pointsCopy);
            Point p = pointsCopy[i];
            Comparator<Point> c = p.slopeOrder();
            Arrays.sort(pointsCopy, c);
            for (int j0 = 0, j = j0 + 1; j < n; j0 = j) {
                while (j < n && c.compare(pointsCopy[j0], pointsCopy[j]) == 0) j++;
                if ((j - j0) >= 3 && lessEqual(p, pointsCopy[j0]))
                    segments.add(new LineSegment(p, pointsCopy[j - 1]));
            }
        }
    }

    // helper: check if p0 is less than or equal to p1
    private static boolean lessEqual(Point p0, Point p1) {
        return p0.compareTo(p1) <= 0;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
