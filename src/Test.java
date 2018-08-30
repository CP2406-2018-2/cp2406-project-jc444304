/**
 *
 */
public class Test {

    /**
     *
     */
    public static void main(String[] args) {

        System.out.println("Attempting to start tests...");
        
        Test.doThread();
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
    protected double speed = 1.0;

    /**
     *
     */
    protected double speedRatio = 1 / this.speed;

    /**
     *
     */
    protected double macsPerSec = speedRatio * 1000;

    /**
     *
     */
    protected double nansPerSec = speedRatio * 1000000;

    /**
     *
     */
    protected double nansDifference = nansPerSec % 1000;

    /**
     *
     */
    @Override
    public synchronized void start() {

        System.out.println("Attempting to start thread:");
        System.out.println(this.nansPerSec + " ... " + this.macsPerSec + " ... " + this.nansDifference);

        super.start();
    }

    /**
     *
     */
    public void run() {

        for (; ; ) {

            System.out.println(System.currentTimeMillis() + " ... " + System.nanoTime());

            try {
                Thread.sleep((long) this.macsPerSec, (int) this.nansDifference);
            } catch (InterruptedException exception) {
                return;
            }
        }
    }
}
