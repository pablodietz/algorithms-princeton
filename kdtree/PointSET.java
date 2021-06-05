/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> bst;

    // construct an empty set of points
    public PointSET() {
        bst = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return bst.isEmpty();
    }

    // number of points in the set
    public int size() {
        return bst.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument is null");
        bst.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument is null");
        return bst.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : bst)
            p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("argument is null");
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : bst)
            if (rect.distanceTo(p) == 0) q.enqueue(p);
        return q;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument is null");
        Point2D nearest = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (Point2D pi : bst)
            if (p.distanceSquaredTo(pi) < nearestDistance) {
                nearest = pi;
                nearestDistance = p.distanceSquaredTo(nearest);
            }
        return nearest;
    }

    // unit testing of the methods
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        PointSET ps = new PointSET();
        RectHV rect = new RectHV(0.25, 0.25, 0.75, 0.75);
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            ps.insert(new Point2D(x, y));
        }
        ps.draw();
        rect.draw();
        for (Point2D p : ps.range(rect))
            StdOut.printf("%8.6f %8.6f\n", p.x(), p.y());
        Point2D nearest = ps.nearest(new Point2D(0.5, 0.5));
        StdOut.printf("%8.6f %8.6f\n", nearest.x(), nearest.y());
    }
}
