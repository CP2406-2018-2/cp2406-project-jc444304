// Author: Yvan Burrie

package SmartHome;

/**
 *
 */
public class Synchronizer extends Thread {

    /**
     * Points to the whatever class is dependent of the Synchronizable interface.
     */
    protected Synchronizable target;

    /**
     * Specifies the number of nano-seconds in each second.
     */
    final public static long NANO_SECS_PER_SEC = 1000000000;

    /**
     * Specifies the number of fake seconds per real second of the thread.
     * For example, set to 60 so that 1 minute is accelerated to 1 second in real-time.
     */
    protected long speed = 1;

    public long getSpeed() {
        return this.speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    /**
     * Specifies the expiration of the thread in number of fake seconds.
     */
    protected long limit = 60 * 60 * 24 * 365;

    public long getLimit() {
        return this.limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    /**
     * Specifies the number of loops executed per fake second.
     */
    protected long loopsPerSecond = 1;

    public long getLoopsPerSecond() {
        return this.loopsPerSecond;
    }

    public void setLoopsPerSecond(long loopsPerSecond) {
        this.loopsPerSecond = loopsPerSecond;
    }

    /**
     * Specifies the nano-time when the automation begins running.
     */
    private long firstNanoTime;

    public long getFirstNanoTime() {
        return this.firstNanoTime;
    }

    /**
     * Specifies the next nano-time when the loop should reiterates.
     */
    private long nextNanoTime;

    /**
     * Specifies the total number of thread loops regardless if they were skipped.
     */
    private long loopsAttempted;

    public long getLoopsAttempted() {
        return this.loopsAttempted;
    }

    /**
     * Specifies the total number of thread loops that were used to execute code.
     */
    private long loopsSucceeded;

    public long getLoopsSucceeded() {
        return this.loopsSucceeded;
    }

    /**
     * Specifies the current thread loop waiting time in nano-seconds.
     */
    private long pauseNanoSeconds;

    /**
     * Behaves like a clock but in milli-seconds.
     */
    private long clock = 0;

    public long getClock() {
        return this.clock;
    }

    public void setClock(long clock) {
        this.clock = clock;
    }

    public Synchronizer(Synchronizable target) {

        this.target = target;
    }

    public Synchronizer(Synchronizable target, long speed, long limit, long loopsPerSecond) {

        this.target = target;

        this.speed = speed;
        this.limit = limit;
        this.loopsPerSecond = loopsPerSecond;
    }

    /**
     *
     */
    public void run() {

        this.firstNanoTime = System.nanoTime();
        this.nextNanoTime = this.firstNanoTime;
        this.loopsAttempted = 0;
        this.loopsSucceeded = 0;
        this.pauseNanoSeconds = NANO_SECS_PER_SEC / this.speed / this.loopsPerSecond;

        while (true) {

            this.loopsAttempted++;

            if (System.nanoTime() - this.nextNanoTime < this.pauseNanoSeconds) continue;

            this.loopsSucceeded++;

            this.nextNanoTime += this.pauseNanoSeconds;

            this.clock += 1000 / this.loopsPerSecond;

            this.target.synchronize(this.loopsPerSecond);

            if (this.loopsSucceeded >= this.limit * this.loopsPerSecond) break;
        }
    }
}

/**
 *
 */
interface Synchronizable {

    /**
     * @param loopsPerSecond Specifies the refresh rate of the call to run thread.
     */
    void synchronize(long loopsPerSecond);
}
