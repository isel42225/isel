package exercises;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class EventBus {

    private static class Subscriber<T>{
        public final Condition cond;
        private final int maxPending;
        private LinkedList<T> msgList;
        private int pending ;

        private Subscriber(Condition cond, int maxPending) {
            this.cond = cond;
            msgList = new LinkedList<>();
            this.maxPending = maxPending;
            pending = 0;
        }

        public boolean hasMessage(){
            return !msgList.isEmpty();
        }

        public void putMessageAndSignal(T message){
            msgList.add(message);
            ++pending;
            cond.signal();
        }

        public T getMessage() {
            T res = msgList.remove();
            --pending;
            return res;
        }

        public boolean isNotFull() {
            return pending <= maxPending;
        }

        public boolean isEmpty(){
            return pending == 0;
        }
    }

    private final static String SHUTDOWN_MSG = "Bus already closed!";


    private final int maxPending;
    private boolean isShutdown;
    private final Lock mon = new ReentrantLock();
    private final Condition shutdown = mon.newCondition();
    private final Map<Class, List<Subscriber>> subscribers = new HashMap<>();



    public EventBus(int maxPending){
        this.maxPending = maxPending;
    }


    // T é o tipo de evento
    public <T> void  subscribeEvent(Consumer<T> handler, Class eventType){
        Subscriber<T> sub = startSubscription(eventType);
        while(true){
            try {
                T event = getEvent(sub);
                if(event != null)
                    handler.accept(event);
                if(stopSubscription(sub,eventType))return;
            } catch (InterruptedException e) {
                // TODO : more ?
                subscribers.get(eventType).remove(sub);
                return;
            }
        }
    }

    private <T> T getEvent(Subscriber<T> sub) throws InterruptedException {
        try{
            mon.lock();

            // fast-path
            if(sub.hasMessage()){
                return sub.getMessage();
            }

            while(true){
                try {
                    sub.cond.await();
                }catch (InterruptedException e){
                    // TODO
                    if(sub.hasMessage()){
                        Thread.currentThread().interrupt();
                        return sub.getMessage();
                    }

                    // TODO : more ?
                    throw e;
                }

                if(sub.hasMessage()){
                    return sub.getMessage();
                }

                if(isShutdown){
                    return null;
                }

            }
        }finally {
            mon.unlock();
        }
    }

    private <T> Subscriber<T> startSubscription(Class eventType) {
        try{
            mon.lock();
            List<Subscriber> list =
                    subscribers.computeIfAbsent(eventType, k -> new ArrayList<>());
            Subscriber<T> subscriber = new Subscriber<>(mon.newCondition(),maxPending);
            list.add(subscriber);
            return subscriber;
        }finally {
            mon.unlock();
        }
    }

    private <T> boolean stopSubscription(Subscriber<T> sub, Class eventType){
        try{
            mon.lock();
            if(isShutdown && sub.isEmpty()){
                List<Subscriber> list = subscribers.get(eventType);
                list.remove(sub);
                if(list.isEmpty()) subscribers.remove(eventType);

                // signal shutdown
                if(subscribers.isEmpty()) {
                    shutdown.signal();
                    isShutdown = false;
                }

                return true;
            }
            return false;
        }finally {
            mon.unlock();
        }
    }

    // E é o tipo de evento
    public <E> void publishEvent(E message){
        try {
            mon.lock();
            //TODO : check for shutdown
            if(isShutdown) throw new IllegalStateException(SHUTDOWN_MSG);
            Class evenType = message.getClass();
            List<Subscriber> subs = subscribers.get(evenType);
            if(subs == null) return;
            for(Subscriber s : subs){
                if(s.isNotFull())
                    s.putMessageAndSignal(message);
            }
        }finally {
            mon.unlock();
        }
    }

    public void shutdown(){
        try{
            mon.lock();
            isShutdown = true;

            // fast-path, no pending work ?
            if(subscribers.isEmpty()) return;


            // wake up every subscriber
            subscribers.values().forEach(l -> l.forEach(s -> s.cond.signal()));
            while(true){
                try {
                    shutdown.await();
                }
                catch (InterruptedException e){
                    //TODO :
                    if(!isShutdown){
                        Thread.currentThread().interrupt();
                        return;
                    }

                }

                if(!isShutdown){
                    return;
                }

            }


        } finally {
            mon.unlock();
        }
    }


}
