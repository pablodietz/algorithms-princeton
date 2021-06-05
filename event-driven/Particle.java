import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.awt.*;

public class Particle {
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private double rx, ry;          // position
    private double vx, vy;          // velocity
    private final double radius;    // radius
    private final double mass;      // mass
    private int count;              // number of collisions
    private Color color;            // color

    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
        this.vx = vx;
        this.vy = vy;
        this.rx = rx;
        this.ry = ry;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
    }

    public Particle(double radius, double mass) {
        rx = StdRandom.uniform(0.0, 1.0);
        ry = StdRandom.uniform(0.0, 1.0);
        vx = StdRandom.uniform(-0.005, 0.005);
        vy = StdRandom.uniform(-0.005, 0.005);
        color = Color.BLACK;
        this.radius = radius;
        this.mass = mass;
    }

    public Particle() {
        rx = StdRandom.uniform(0.0, 1.0);
        ry = StdRandom.uniform(0.0, 1.0);
        vx = StdRandom.uniform(-0.005, 0.005);
        vy = StdRandom.uniform(-0.005, 0.005);
        radius = 0.02;
        mass = 0.5;
        color = Color.BLACK;
    }

    public void move(double dt) {
        rx = rx + vx * dt;
        ry = ry + vy * dt;
    }

    public void draw() {
        StdDraw.filledCircle(rx, ry, radius);
    }

    // predict collision with particle or wall
    public double timeToHit(Particle that) {
        if (this == that) return INFINITY;  // no collision
        double dx = that.rx - this.rx, dy = that.ry - this.ry;
        double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
        double dvdr = dx * dvx + dy * dvy;
        if (dvdr > 0) return INFINITY;  // no collision
        double dvdv = dvx * dvx + dvy * dvy;
        double drdr = dx * dx + dy * dy;
        double sigma = this.radius + that.radius;
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);
        if (d < 0) return INFINITY;  // no collision
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    public double timeToHitVerticalWall() {
        if (this.vx > 0) return (1.0 - this.radius - this.rx) / this.vx;
        else if (this.vx < 0) return (this.radius - this.rx) / this.vx;
        else return INFINITY;  // no collision
    }

    public double timeToHitHorizontalWall() {
        if (this.vy > 0) return (1.0 - this.radius - this.ry) / this.vy;
        else if (this.vy < 0) return (this.radius - this.ry) / this.vy;
        else return INFINITY;  // no collision
    }

    // resolve collision with particle or wall
    public void bounceOff(Particle that) {
        double dx = that.rx - this.rx, dy = that.ry - this.ry;
        double dvx = that.vx - this.vx, dvy = that.vy - this.vy;
        double dvdr = dx * dvx + dy * dvy;
        double sigma = this.radius + that.radius;
        double J = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * sigma);
        double Jx = J * dx / sigma, Jy = J * dy / sigma;
        this.vx += Jx / this.mass;
        this.vy += Jy / this.mass;
        that.vx -= Jx / that.mass;
        that.vy -= Jy / that.mass;
        this.count++;
        that.count++;
    }

    public void bounceOffVerticalWall() {
        this.vx = -this.vx;
        this.count++;
    }

    public void bounceOffHorizontalWall() {
        this.vy = -this.vy;
        this.count++;
    }

    // getters
    public int count() {
        return count;
    }
}
