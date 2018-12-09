// Author: Yvan Burrie

package SmartHome;

import com.sun.istack.internal.NotNull;

/**
 *
 */
public class Synchronizer extends Thread {

    /**
     * Points to the whatever class is dependent of the Synchronizable interface.
     */
    private Synchronizable target;

    /**
     * Specifies the number of nano-seconds in each second.
     */
    final static long NANO_SECS_PER_SEC = 1000000000;

    /**
     * Specifies the number of fake seconds per real second of the thread.
     * For example, set to 60 so that 1 minute is accelerated to 1 second in real-time.
     */
    private long speed = 1;

    public long getSpeed() {
        return this.speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
        update();
    }

    /**
     * Specifies the expiration of the thread in number of fake seconds.
     */
    private long maximumTime = 60 * 60 * 24 * 365;

    public long getMaximumTime() {
        return this.maximumTime;
    }

    public void setMaximumTime(long maximumTime) {
        this.maximumTime = maximumTime;
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
        update();
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
     * Specifies the current fake time in milli-seconds.
     */
    private long currentTime = 0;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    boolean paused = false;

    public Synchronizer(@NotNull Synchronizable target) {

        this.target = target;
    }

    public Synchronizer(@NotNull Synchronizable target, long speed, long loopsPerSecond) {

        this.target = target;
        this.speed = speed;
        this.loopsPerSecond = loopsPerSecond;
    }

    void setDuration(Clock startClock, Clock endClock) {

        maximumTime = endClock.compareTo(startClock);
        if (maximumTime < 0) {
            maximumTime *= -1;
        }
        maximumTime = endClock.getTimeInMillis();
    }

    private void update() {

        pauseNanoSeconds = NANO_SECS_PER_SEC / speed / loopsPerSecond;
    }

    /**
     *
     */
    public void run() {

        firstNanoTime = nextNanoTime = System.nanoTime();
        loopsAttempted = 0;
        loopsSucceeded = 0;
        update();

        while (!isInterrupted()) {

            if (paused) {
                nextNanoTime = System.nanoTime();
                continue;
            }

            loopsAttempted++;

            if (System.nanoTime() - nextNanoTime < pauseNanoSeconds){
                continue;
            }

            loopsSucceeded++;

            nextNanoTime += pauseNanoSeconds;

            currentTime += 1000 / loopsPerSecond;

            target.synchronize(loopsPerSecond);

            System.out.println(currentTime + ": " + pauseNanoSeconds);

            if (currentTime >= maximumTime){
                break;
            }
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
