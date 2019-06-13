package sketches;

import pt.isel.pc.examples.utils.Timeouts;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class SimpleSemaphore {

    private int units;

    public SimpleSemaphore(int initial) {
        units = initial;
    }

    // synchronized over this
    synchronized public boolean tryAcquire(long timeoutInMs) throws InterruptedException {

        // fast path
        if (units > 0) {
            units -= 1;
            return true;
        }
        // should wait or not?
        if (Timeouts.noWait(timeoutInMs)) {
            return false;
        }

        // prepare everything for waiting
        long limit = Timeouts.start(timeoutInMs);
        long remainingInMs = Timeouts.remaining(limit);
        while (true) {
            try {
                this.wait(remainingInMs);
            } catch (InterruptedException ex) {
                // So that notifications are not lost
                if (units > 0) {
                    notify();
                }
                throw ex;
            }
            if (units > 0) {
                units -= 1;
                return true;
            }
            remainingInMs = Timeouts.remaining(limit);
            if (Timeouts.isTimeout(remainingInMs)) {
                // no cancellation processing needed
                return false;
            }
        }
    }

    // synchronized over this
    synchronized public void release() {
        units += 1;
        this.notify();
    }
}
