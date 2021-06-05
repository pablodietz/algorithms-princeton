public class Event implements Comparable<Event> {
    private Particle a, b;      // particles involved in event
    private double time;        // time of event
    private int countA, countB; // collision counts for a and b

    public Event(double time, Particle a, Particle b) {
        this.time = time;
        this.a = a;
        this.b = b;
        if (a != null) this.countA = a.getCount();
        else this.countA = -1;
        if (b != null) this.countB = b.getCount();
        else this.countB = -1;
    }

    // ordered by time
    @Override
    public int compareTo(Event that) {
        return Double.compare(this.time, that.time);
    }

    // is valid if intervening collision
    public boolean isValid() {
        if (a != null && a.getCount() != countA) return false;
        if (b != null && b.getCount() != countB) return false;
        return true;
    }

    // getters
    public Particle getA() {
        return a;
    }

    public Particle getB() {
        return b;
    }

    public double getTime() {
        return time;
    }
}
