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

        TestThread thread = new TestThread();
        thread.start();

        return true;
    }
}

/**
 *
 */
class TestThread extends Thread {

    /**
     * Defines the number of nanoseconds in each second.
     */
    final public static double NANOSECONDS_PER_SECOND = 1000000000;

    /**
     * Specifies the number of seconds for each second of the thread.
     * For example, set to 60 so that 1 minute is accelerated to 1 second.
     */
    protected double speed = 0.5;

    /**
     * Specifies the expiration of the thread limited in number of seconds.
     */
    protected double limit = this.speed * 1;

    /**
     *
     */
    @Override
    public synchronized void start() {

        System.out.println("Attempting to start thread at speed: " + this.speed);

        super.start();
    }

    /**
     *
     */
    public void run() {

        long firstTime = System.nanoTime();
        long startTime = firstTime;

        long loopsAttempted = 0;
        long loopsEvaluated = 0;

        for (; ; ) {

            loopsAttempted++;

            long pauseTime = System.nanoTime() - startTime;

            /* Note that at this point, we may wish to change the speed of the thread while running. */

            if (pauseTime >= NANOSECONDS_PER_SECOND / this.speed) {
                startTime += NANOSECONDS_PER_SECOND / this.speed;

                /* Any code needed for threaded-execution should be placed here. */

                if (++loopsEvaluated >= this.limit) break;
            }
        }

        System.out.println("Total thread loops attempted: " + loopsAttempted);
        System.out.println("Total thread loops evaluated: " + loopsEvaluated);
        System.out.println("Total thread time in seconds: " + ((System.nanoTime() - firstTime) / NANOSECONDS_PER_SECOND));
    }
}
