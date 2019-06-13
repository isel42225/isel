import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Invariants :
 *  - receive() must block when no message in queue
 *  - send() must obey FIFO ordering
 * @param <T> type of Message content
 */
public class OptimizedMessageQueue<T> {

    private class Message implements SendStatus {
        public final AtomicBoolean wasSent = new AtomicBoolean(false);
        public final T data;
        public final AtomicInteger waiters = new AtomicInteger(); // ??
        public final Object inner = new Object();

        public Message(T data) {
            this.data = data;
        }

        public Optional<T> receiveAndSignal() {

            int observedWaiters;
                do {
                    observedWaiters = waiters.get();
                    if (observedWaiters != 0) {
                        synchronized (inner) {
                            inner.notifyAll();
                        }
                    }
                } while (!waiters.compareAndSet(observedWaiters, 0));
                wasSent.set(true);
                return Optional.of(data);
        }

        @Override
        public boolean isSent() {
            return wasSent.get();
        }

        @Override
        public boolean await(int timeout) throws InterruptedException {

            synchronized (inner){
                if(isSent())return true;

                if(Timeouts.noWait(timeout)){
                    return false;
                }
                waiters.incrementAndGet();
                long limit = Timeouts.start(timeout);
                long remaining = Timeouts.remaining(limit);
                try {
                    while (true) {
                        try {
                            inner.wait(remaining);
                        } catch (InterruptedException e) {
                            if (isSent()) {
                                Thread.currentThread().interrupt();
                                return true;
                            }
                            throw e;
                        }

                        if (isSent()) {
                            return true;
                        }

                        remaining = Timeouts.remaining(limit);
                        if (Timeouts.isTimeout(remaining)) {
                            return false;
                        }
                    }
                }finally {
                    waiters.decrementAndGet();
                }
            }
        }
    }

    private final Object mon = new ReentrantLock();
    private volatile int waitingConsumers = 0;
    private final LockFreeQueue<Message> messages = new LockFreeQueue<>();

    public SendStatus send(T sentMsg) {
        Message msg = new Message(sentMsg);
        messages.enqueue(msg);

        if(waitingConsumers != 0){
            synchronized (mon) {
                mon.notify();
            }
        }
        return msg;
    }

    private Message tryReceive(){
        if(messages.isEmpty()) return null;
        return messages.dequeue();
    }

    public Optional<T> receive(int timeout) throws InterruptedException {
        Message msg;
        if((msg = tryReceive()) != null){
            return msg.receiveAndSignal();
        }

        if (Timeouts.noWait(timeout)) {
            return Optional.empty();
        }

        synchronized (mon) {

            long limit = Timeouts.start(timeout);
            long remaining = Timeouts.remaining(limit);

            waitingConsumers += 1;
            try {
                while((msg = tryReceive()) == null){
                    try {
                        mon.wait(remaining);
                    } catch (InterruptedException e) {
                        if(messages.isNotEmpty() && waitingConsumers > 1){
                            mon.notify();
                        }
                        throw e;
                    }

                    remaining = Timeouts.remaining(limit);
                    if (Timeouts.isTimeout(remaining)) {
                        return Optional.empty();
                    }
                }
                return msg.receiveAndSignal();
            }finally {
                waitingConsumers -= 1;
            }
        }
    }
}
