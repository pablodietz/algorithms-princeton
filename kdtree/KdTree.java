/* *****************************************************************************
 *  Name: Pablo Dietz
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.Color;

public class KdTree {
    private static final double PRADIUS = 0.01;
    private Node root = null;
    private int count = 0;

    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument is null");
        root = insert(root, p, 0, 0, 1, 1, true);
    }

    private Node insert(Node node, Point2D p,
                        double xmin, double ymin, double xmax, double ymax,
                        boolean vsplit) {
        if (node == null) {
            count++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        if (p.equals(node.p)) return node;
        int cmp;
        if (vsplit) {
            cmp = Double.compare(p.x(), node.p.x());
            if (cmp <= 0) xmax = node.p.x();
            else xmin = node.p.x();
        }
        else {
            cmp = Double.compare(p.y(), node.p.y());
            if (cmp <= 0) ymax = node.p.y();
            else ymin = node.p.y();
        }
        if (cmp <= 0) node.lb = insert(node.lb, p, xmin, ymin, xmax, ymax, !vsplit);
        else node.rt = insert(node.rt, p, xmin, ymin, xmax, ymax, !vsplit);
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument is null");
        return contains(root, p, true);
    }

    private boolean contains(Node node, Point2D p, boolean vsplit) {
        if (node == null) return false;
        if (p.equals(node.p)) return true;
        int cmp;
        if (vsplit) cmp = Double.compare(p.x(), node.p.x());
        else cmp = Double.compare(p.y(), node.p.y());
        if (cmp < 0) return contains(node.lb, p, !vsplit);
        else if (cmp > 0) return contains(node.rt, p, !vsplit);
        else if (contains(node.lb, p, !vsplit) || contains(node.rt, p, !vsplit)) return true;
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
        StdDraw.setPenColor(Color.BLACK);
    }

    private void draw(Node node, boolean vsplit) {
        if (node == null) return;
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledCircle(node.p.x(), node.p.y(), PRADIUS);
        if (vsplit) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        draw(node.lb, !vsplit);
        draw(node.rt, !vsplit);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("argument is null");
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q, true);
        return q;
    }

    private void range(Node node, RectHV rect, Queue<Point2D> q, boolean vsplit) {
        if (node == null) return;
        if (rect.contains(node.p)) q.enqueue(node.p);
        int cmp;
        if (vsplit)
            if (rect.xmax() < node.p.x()) cmp = -1;
            else if (rect.xmin() > node.p.x()) cmp = 1;
            else cmp = 0;
        else if (rect.ymax() < node.p.y()) cmp = -1;
        else if (rect.ymin() > node.p.y()) cmp = 1;
        else cmp = 0;
        if (cmp <= 0) range(node.lb, rect, q, !vsplit);
        if (cmp >= 0) range(node.rt, rect, q, !vsplit);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument is null");
        if (root == null) return null;
        Point2D[] nearest = { new Point2D(root.p.x(), root.p.y()) };
        nearest(root, p, nearest, true);
        return nearest[0];
    }

    private void nearest(Node node, Point2D p, Point2D[] best, boolean vsplit) {
        double distToBest = p.distanceSquaredTo(best[0]);
        if (node == null || node.rect.distanceSquaredTo(p) > distToBest) return;
        if (p.distanceSquaredTo(node.p) < distToBest) best[0] = node.p;
        if ((vsplit && p.x() < node.p.x()) || (!vsplit && p.y() < node.p.y())) {
            nearest(node.lb, p, best, !vsplit);
            nearest(node.rt, p, best, !vsplit);
        }
        else {
            nearest(node.rt, p, best, !vsplit);
            nearest(node.lb, p, best, !vsplit);
        }
    }

    // unit testing of the methods
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        KdTree kt = new KdTree();
        RectHV rect = new RectHV(0.24, 0.24, 0.76, 0.76);
        for (int i = 0; i < n - 1; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            kt.insert(new Point2D(x, y));
        }
        kt.insert(new Point2D(0.5, 0.5));
        kt.draw();
        rect.draw();
        StdOut.printf("points into the square:\n");
        for (Point2D p : kt.range(rect))
            StdOut.printf("%8.6f %8.6f\n", p.x(), p.y());
        Point2D nearest = kt.nearest(new Point2D(0.5, 0.5));
        StdOut.printf("nearest to (0.5, 0.5): %8.6f %8.6f\n", nearest.x(), nearest.y());
        StdOut.printf("contain (0.5, 0.5)? " + kt.contains(new Point2D(0.5, 0.5)) + "\n");
        StdOut.printf("contain (0.2, 0.3)? " + kt.contains(new Point2D(0.2, 0.3)) + "\n");
        StdOut.println("size: " + kt.size());
    }
}
