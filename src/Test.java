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
     * Specifies the number of seconds for each second of the thread.
     * For example, set to 60 so that 1 minute is accelerated to 1 second.
     */
    protected double speed = 0.5;

    /**
     *
     */
    protected double speedRatio = 1 / this.speed;

    /**
     *
     */
    protected double micsPerSec = this.speedRatio * 1000;

    /**
     *
     */
    protected double nansPerSec = this.micsPerSec * 1000;

    /**
     *
     */
    protected double nansPerSecRemainder = this.nansPerSec % this.micsPerSec;

    /**
     *
     */
    @Override
    public synchronized void start() {

        System.out.println("Attempting to start thread with the following time attributes:");
        System.out.println(" - Speed: " + this.speed);
        System.out.println(" - Speed Ratio: " + this.speedRatio);
        System.out.println(" - Microseconds or Nanoseconds per second: " + this.micsPerSec + ", " + this.nansPerSec);
        System.out.println(" - Nanoseconds remainder from microseconds: " + this.nansPerSecRemainder);

        super.start();
    }

    /**
     *
     */
    public void run() {

        for (; ; ) {

            System.out.println("Current time in milliseconds and nanoseconds (respectively): " + System.currentTimeMillis() + ", " + System.nanoTime());

            try {
                Thread.sleep((long) this.micsPerSec, (int) this.nansPerSecRemainder);
            } catch (InterruptedException exception) {
                return;
            }
        }
    }
}
