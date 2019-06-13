package exercises;

import utils.Timeouts;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Optional.empty;
import static java.util.Optional.of;


public class KeyedExchanger<T> {

    private class Exchange{
        public T data;
        public final Condition cond ;

        public Exchange(T data, Condition cond) {
            this.data = data;
            this.cond = cond;
        }
    }

    private final Map<Integer, Exchange> map = new HashMap<>();
    private final Lock mon = new ReentrantLock();

    public Optional<T> exchange(int ky, T mydata, int timeout) throws InterruptedException{
        try {
            mon.lock();

            Exchange myExchange =
                    map.computeIfAbsent(ky,(key)-> new Exchange(mydata, mon.newCondition()));

            //TODO: fast path ?
            if(!myExchange.data.equals(mydata)){
                Optional<T> res = of(myExchange.data);
                myExchange.data = mydata;
                myExchange.cond.signal();
                return res;
            }

            if (Timeouts.noWait(timeout))
                return empty();

            long limit = Timeouts.start(timeout);
            long remainingInMs = Timeouts.remaining(limit);
            while (true) {
                try {
                    myExchange.cond.await(remainingInMs, TimeUnit.MILLISECONDS);
                }catch (InterruptedException e){
                    if(!myExchange.data.equals(mydata)){
                        Thread.currentThread().interrupt();
                        Optional<T> res = of(myExchange.data);
                        map.remove(ky);
                        return res;
                    }

                    // TODO: Maybe needs more ?
                    map.remove(ky);
                    throw e;

                }

                if(!myExchange.data.equals(mydata)){
                    Optional<T> res = of(myExchange.data);
                    map.remove(ky);
                    return res;
                }

                remainingInMs = Timeouts.remaining(limit);
                if (Timeouts.isTimeout(remainingInMs)) {
                    // TODO: signal?
                    //remove myData from map
                    map.remove(ky);
                    return empty();
                }
            }
        }
        finally {
            mon.unlock();
        }
    }
}
