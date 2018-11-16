// Author: Yvan Burrie

import SmartHome.*;

/**
 *
 */
public class Test {

    /**
     *
     */
    public static void main(String[] args) {

        System.out.println("Attempting to start tests...");

        System.out.println("Doing the Thread test:");
        if (!Test.doThread()) {
            System.out.println("Error while doing the Thread test!");
        }
    }
    
    /**
     *
     */
    private static boolean doThread() {

        Automator automator = new Automator();

        Synchronizer thread;

        /* run the thread for at normal speed for 5 seconds with 100 executions per second: */
        thread = new Synchronizer(automator, 1, 5, 100);
        thread.start();
        while (thread.isAlive()) continue;
        System.out.println("Total thread loops attempted: " + thread.getLoopsAttempted());
        System.out.println("Total thread loops evaluated: " + thread.getLoopsSucceeded());
        System.out.println("Total thread time in seconds: " + ((System.nanoTime() - thread.getFirstNanoTime()) / 1000000000));

        /* run each minute as a second for 10 minutes with 1 execution per second: */
        thread = new Synchronizer(automator, 60, 60 * 10, 1);
        thread.start();
        while (thread.isAlive()) continue;
        System.out.println("Total thread loops attempted: " + thread.getLoopsAttempted());
        System.out.println("Total thread loops evaluated: " + thread.getLoopsSucceeded());
        System.out.println("Total thread time in seconds: " + ((System.nanoTime() - thread.getFirstNanoTime()) / 1000000000));

        return true;
    }
}

/**
 *
 */
class TestThread extends Thread {

    /**
     * Specifies the number of fake seconds per real second of the thread.
     * For example, set to 60 so that 1 minute is accelerated to 1 second in real-time.
     */
    protected long threadSpeed = 1;

    public long getThreadSpeed() { return threadSpeed; }

    public void setThreadSpeed(long speed) { this.threadSpeed = speed; }

    /**
     * Specifies the expiration of the thread in number of fake seconds.
     */
    protected long threadLimit = 1;

    public long getThreadLimit() { return threadLimit; }

    public void setThreadLimit(long limit) { this.threadLimit = limit; }

    /**
     * Specifies the number of loops executed per fake second.
     */
    protected long threadLoopsPerSecond = 1;

    public long getThreadLoopsPerSecond() { return threadLoopsPerSecond; }

    public void setThreadLoopsPerSecond(long loopsPerSecond) { this.threadLoopsPerSecond = loopsPerSecond; }

    /**
     * Specifies the nano-time when the automation begins running.
     */
    private long threadFirstNanoTime;

    public long getThreadFirstNanoTime() { return threadFirstNanoTime; }

    /**
     * Specifies the next nano-time when the loop should reiterates.
     */
    private long threadNextNanoTime;

    /**
     * Specifies the total number of thread loops regardless if they were skipped.
     */
    private long threadLoopsAttempted;

    public long getThreadLoopsAttempted() { return threadLoopsAttempted; }

    /**
     * Specifies the total number of thread loops that were used to execute code.
     */
    private long threadLoopsSucceeded;

    public long getThreadLoopsSucceeded() { return threadLoopsSucceeded; }

    /**
     * Specifies the current thread loop waiting time in nano-seconds.
     */
    private long threadPauseNanoSeconds;

    public TestThread(long speed, long limit, long loopsPerSecond) {

        this.threadSpeed = speed;
        this.threadLimit = limit;
        this.threadLoopsPerSecond = loopsPerSecond;
    }

    /**
     *
     */
    @Override
    public synchronized void start() {

        this.threadFirstNanoTime = System.nanoTime();

        System.out.println("Attempting to start thread at speed: " + this.threadSpeed);

        super.start();
    }

    /**
     *
     */
    public void run() {

        this.threadNextNanoTime = this.threadFirstNanoTime;
        this.threadLoopsAttempted = 0;
        this.threadLoopsSucceeded = 0;
        this.threadPauseNanoSeconds = (long) 1000000000 / this.threadSpeed / this.threadLoopsPerSecond;

        while (true) {

            this.threadLoopsAttempted++;

            if (System.nanoTime() - this.threadNextNanoTime < this.threadPauseNanoSeconds) continue;

            /* Note that if something slows down the loop so much that it is well past this next expected time, it will eventually catch-up upon each thread loop. */
            this.threadNextNanoTime += this.threadPauseNanoSeconds;

            /* Any code needed for threaded-execution should be placed here. */

            if (++this.threadLoopsSucceeded >= this.threadLimit * this.threadLoopsPerSecond) break;
        }
    }
}
