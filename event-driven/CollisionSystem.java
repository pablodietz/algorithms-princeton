import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;

import java.awt.*;

public class CollisionSystem {
    private MinPQ<Event> pq;        // the priority queue
    private double t = 0.0;         // simulation clock time
    private Particle[] particles;   // the array of particles
    private int N;

    public CollisionSystem(Particle[] particles) {
        this.particles = particles.clone();
        this.N = particles.length;
    }

    private void predict(Particle a) {
        if (a == null) return;
        for (int i = 0; i < N; i++) {
            double dt = a.timeToHit(particles[i]);
            pq.insert(new Event(this.t + dt, a, particles[i]));
        }
        pq.insert(new Event(this.t + a.timeToHitVerticalWall(), a, null));
        pq.insert(new Event(this.t + a.timeToHitHorizontalWall(), null, a));
    }

    private void redraw() {
        StdDraw.clear();
        for (int i = 0; i < N; i++) particles[i].draw();
        StdDraw.show();
        StdDraw.pause(20);
        pq.insert(new Event(t + 1.0 / 1.0, null, null));
    }

    public void simulate() {
        pq = new MinPQ<Event>();
        // initialize PQ with collision events and redraw event
        for (int i = 0; i < N; i++) predict(particles[i]);
        pq.insert(new Event(0, null, null));

        while (!pq.isEmpty()) {
            // get next event
            Event event = pq.delMin();
            if (!event.isValid()) continue;
            Particle a = event.a, b = event.b;
            // update positions and time
            for (int i = 0; i < N; i++) particles[i].move(event.time - t);
            t = event.time;
            // process event
            // neither particle null -> particle-particle collision
            // one particle null -> particle-wall collision
            // both particles null -> redraw event
            if (a != null && b != null) a.bounceOff(b);
            if (a != null && b == null) a.bounceOffVerticalWall();
            if (a == null && b != null) b.bounceOffHorizontalWall();
            if (a == null && b == null) redraw();
            // predict new events based on changes
            predict(a);
            predict(b);
        }
    }

    private static class Event implements Comparable<Event> {
        private final Particle a, b;      // particles involved in event
        private final double time;        // time of event
        private final int countA, countB; // collision counts for a and b

        public Event(double time, Particle a, Particle b) {
            this.time = time;
            this.a = a;
            this.b = b;
            if (a != null) this.countA = a.count();
            else this.countA = -1;
            if (b != null) this.countB = b.count();
            else this.countB = -1;
        }

        // ordered by time
        @Override
        public int compareTo(Event that) {
            return Double.compare(this.time, that.time);
        }

        // is valid if intervening collision
        public boolean isValid() {
            if (a != null && a.count() != countA) return false;
            if (b != null && b.count() != countB) return false;
            return true;
        }
    }

    public static void main(String[] args) {
        // StdDraw.setCanvasSize(600, 600);

        // enable double buffering
        StdDraw.enableDoubleBuffering();

        // the array of particles
        Particle[] particles;

        // create n random particles
        if (args.length == 1) {
            int n = Integer.parseInt(args[0]);
            particles = new Particle[n];
            for (int i = 0; i < n; i++)
                particles[i] = new Particle();
        }

        // or read from standard input
        else {
            int n = StdIn.readInt();
            particles = new Particle[n];
            for (int i = 0; i < n; i++) {
                double rx = StdIn.readDouble();
                double ry = StdIn.readDouble();
                double vx = StdIn.readDouble();
                double vy = StdIn.readDouble();
                double radius = StdIn.readDouble();
                double mass = StdIn.readDouble();
                int r = StdIn.readInt();
                int g = StdIn.readInt();
                int b = StdIn.readInt();
                Color color = new Color(r, g, b);
                particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
            }
        }

        // create collision system and simulate
        CollisionSystem system = new CollisionSystem(particles);
        system.simulate();
    }
}
