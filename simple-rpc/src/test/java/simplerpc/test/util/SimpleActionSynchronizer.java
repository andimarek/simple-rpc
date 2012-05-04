package simplerpc.test.util;

import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Synchronizes two Threads, so that main test thread can test, if an asynchronous call was made.
 */
public class SimpleActionSynchronizer{

    private final CountDownLatch countDownLatch = new CountDownLatch( 1 );
    private final long timeout;

    public SimpleActionSynchronizer( long timeout ){
        this.timeout = timeout;
    }

    /**
     * The main thread invokes this to wait for a specified time.
     * 
     * @param failureMessage
     * @throws InterruptedException
     */
    public void waitForAction( String failureMessage ) throws InterruptedException{
        countDownLatch.await( timeout, TimeUnit.MILLISECONDS );
        if( countDownLatch.getCount() > 0 ) fail( failureMessage );
    }

    /**
     * The other Thread invokes this.
     */
    public void action(){
        countDownLatch.countDown();
    }

}
