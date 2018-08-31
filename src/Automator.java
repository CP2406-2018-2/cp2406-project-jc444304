/**
 * @author Yvan Burrie
 */
public class Automator extends Thread {

    /**
     * Defines the number of nanoseconds in each second.
     */
    final public static double NANOSECONDS_PER_SECOND = 1000000000;

    /**
     * Specifies the number of seconds for each second of the thread.
     * For example, set to 60 so that 1 minute is accelerated to 1 second.
     */
    protected double speed = 1.0;

    /**
     *
     */
    public double getSpeed() { return speed; }

    /**
     * @param speed Must be greater or less-than zero.
     */
    public void setSpeed(double speed) {
        if (speed == 0) {
            throw new IllegalArgumentException("The automation speed must not be zero.");
        }
        this.speed = speed;
    }

    /**
     * Specifies the expiration of the thread limited in number of seconds.
     */
    protected double limit = this.speed * 1;

    /**
     *
     */
    public double getLimit() { return limit; }

    /**
     *
     */
    public void setLimit(double limit) { this.limit = limit; }

    /**
     *
     */
    public Automator(double speed) {

        this.setSpeed(speed);
    }

    /**
     *
     */
    public void run() {

        long firstTime = System.nanoTime();
        long startTime = firstTime;
        long loopsEvaluated = 0;

        for (; ; ) {

            long pauseTime = System.nanoTime() - startTime;

            if (pauseTime >= NANOSECONDS_PER_SECOND / this.speed) {
                startTime += NANOSECONDS_PER_SECOND / this.speed;

                if (++loopsEvaluated >= this.limit) break;
            }
        }
    }
}
