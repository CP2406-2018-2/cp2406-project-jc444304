
/**
 *
 */
public class Synchronizer extends Thread {

    /**
     * Points to the dependency automator.
     */
    protected Synchronyzable target;

    /**
     * Specifies the number of fake seconds per real second of the thread.
     * For example, set to 60 so that 1 minute is accelerated to 1 second in real-time.
     */
    protected long speed = 1;

    public long getSpeed() { return this.speed; }

    public void setSpeed(long speed) { this.speed = speed; }

    /**
     * Specifies the expiration of the thread in number of fake seconds.
     */
    protected long limit = 1;

    public long getLimit() { return this.limit; }

    public void setLimit(long limit) { this.limit = limit; }

    /**
     * Specifies the number of loops executed per fake second.
     */
    protected long loopsPerSecond = 1;

    public long getLoopsPerSecond() { return this.loopsPerSecond; }

    public void setLoopsPerSecond(long loopsPerSecond) { this.loopsPerSecond = loopsPerSecond; }

    /**
     * Specifies the nano-time when the automation begins running.
     */
    private long firstNanoTime;

    public long getFirstNanoTime() { return this.firstNanoTime; }

    /**
     * Specifies the next nano-time when the loop should reiterates.
     */
    private long nextNanoTime;

    /**
     * Specifies the total number of thread loops regardless if they were skipped.
     */
    private long loopsAttempted;

    public long getLoopsAttempted() { return this.loopsAttempted; }

    /**
     * Specifies the total number of thread loops that were used to execute code.
     */
    private long loopsSucceeded;

    public long getLoopsSucceeded() { return this.loopsSucceeded; }

    /**
     * Specifies the current thread loop waiting time in nano-seconds.
     */
    private long pauseNanoSeconds;

    public Synchronizer(Synchronyzable target) {

        this.target = target;
    }

    public Synchronizer(Synchronyzable target, long speed, long limit, long loopsPerSecond) {

        this.target = target;

        this.speed = speed;
        this.limit = limit;
        this.loopsPerSecond = loopsPerSecond;
    }

    /**
     *
     */
    @Override
    public synchronized void start() {

        this.firstNanoTime = System.nanoTime();

        super.start();
    }

    /**
     *
     */
    public void run() {

        this.nextNanoTime = this.firstNanoTime;
        this.loopsAttempted = 0;
        this.loopsSucceeded = 0;
        this.pauseNanoSeconds = (long) 1000000000 / this.speed / this.loopsPerSecond;

        while (true) {

            this.loopsAttempted++;

            if (System.nanoTime() - this.nextNanoTime < this.pauseNanoSeconds) continue;

            this.nextNanoTime += this.pauseNanoSeconds;

            this.target.synchronize(this.loopsPerSecond);

            if (++this.loopsSucceeded >= this.limit * this.loopsPerSecond) break;
        }
    }
}

/**
 *
 */
interface Synchronyzable {

    /**
     * @param loopsPerSecond Specifies the refresh rate of the call to run thread.
     */
    void synchronize(long loopsPerSecond);
}
