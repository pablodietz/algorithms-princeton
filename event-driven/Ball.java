import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

public class Ball {
    private double rx, ry;          // position
    private double vx, vy;          // velocity
    private final double radius;    // radius

    // initialize position, velocity and radius
    public Ball(double radius) {
        this.rx = radius + (1 - 2 * radius) * StdRandom.uniform();
        this.ry = radius + (1 - 2 * radius) * StdRandom.uniform();
        this.vx = 0.1 * (0.5 - StdRandom.uniform());
        this.vy = 0.1 * (0.5 - StdRandom.uniform());
        this.radius = radius;
    }

    public Ball(double rx, double ry, double vx, double vy, double radius) {
        this.rx = rx;
        this.ry = ry;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
    }

    public void move(double dt) {
        // check for collision with walls
        if ((rx + vx * dt < radius) || (rx + vx * dt > 1.0 - radius)) vx = -vx;
        if ((ry + vy * dt < radius) || (ry + vy * dt > 1.0 - radius)) vy = -vy;
        // update position
        rx = rx + vx * dt;
        ry = ry + vy * dt;
    }

    public void draw() {
        StdDraw.filledCircle(rx, ry, radius);
    }
}
