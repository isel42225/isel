import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class OptimizedMessageQueueTest {

    private static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    /**
     * Inspired by suggestion in Java Concurrency in Pratice, chapter 12, page 156 - 158
     */
    @Test
    public void sendAndReceiveTest() {

        final OptimizedMessageQueue<Integer> queue = new OptimizedMessageQueue<>();
        final ExecutorService pool = Executors.newCachedThreadPool();
        final AtomicInteger sendSum = new AtomicInteger(0);
        final AtomicInteger receiveSum = new AtomicInteger(0);
        final int nPairs = 10;
        final int nTrials = 100000;
        final CyclicBarrier barrier = new CyclicBarrier(nPairs * 2 + 1);

        try {
            for (int i = 0; i < nPairs; ++i) {
                pool.execute(() -> {
                    try {
                        int seed = this.hashCode() ^ (int) System.nanoTime();
                        int sum = 0;
                        barrier.await();
                        for (int j = 0; j < nTrials; ++j) {
                            queue.send(seed);
                            sum += seed;
                            seed = xorShift(seed);
                        }
                        sendSum.getAndAdd(sum);
                        barrier.await();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }); // producer (send messages)
                pool.execute(() -> {
                    try {
                        barrier.await();
                        int sum = 0;
                        for (int k = 0; k < nTrials; ++k) {
                            sum += queue.receive(Integer.MAX_VALUE).get();
                        }
                        receiveSum.getAndAdd(sum);
                        barrier.await();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }); // consumer (receive messages)
            }
            barrier.await();    // wait for all threads to be ready
            barrier.await();    // wait for all threads to finish
            assertEquals(sendSum.get(), receiveSum.get());
        }catch (Exception e ){
            throw new RuntimeException(e);
        }
    }

}