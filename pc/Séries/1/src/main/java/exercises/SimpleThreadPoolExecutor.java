package exercises;

import utils.NodeLinkedList;
import utils.Timeouts;

import java.util.LinkedList;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleThreadPoolExecutor {

    private class Worker {
        private long timeout;
        public Thread thread;
        private Runnable task;
        private Condition cond;

        //....


        public Worker(Condition cond ,int timeout) {
            this.cond = cond;
            this.timeout = Timeouts.start(timeout);
            task = null;
        }

        public void putTaskAndSignal(Runnable task){
            this.task = task;
            cond.signal();
            timeout = Timeouts.start(timeout);

        }

        public boolean hasWork(){
            return task != null;
        }

        public boolean hasExpired(){
            long remaining = Timeouts.remaining(timeout);
            return Timeouts.isTimeout(remaining);
        }

    }

    private class Work{
        public Runnable work;
        public final Condition cond;

        public Work(Runnable work, Condition cond) {
            this.work = work;
            this.cond = cond;
        }


        public Runnable getWorkAndSignal(){
            Runnable res = work;
            work = null;
            cond.signal();
            return res;
        }

        public boolean wasDelivered(){
            return work == null;
        }
    }

    private final int maxPoolSize;
    private final int keepAliveTime;
    private int nOfThreads;
    private boolean isShutdown;

    private final Lock mon = new ReentrantLock();
    private final Condition shutdown = mon.newCondition();

    private final NodeLinkedList<Worker> available = new NodeLinkedList<>();
    private final NodeLinkedList<Work> work = new NodeLinkedList<>();



    public SimpleThreadPoolExecutor(int maxPoolSize, int keepAliveTime) {
        this.maxPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        nOfThreads = 0;
    }


    public boolean execute(Runnable command, int timeout)
            throws InterruptedException {

        try {
            mon.lock();


            if(isShutdown){
                throw new RejectedExecutionException();
            }

            if(available.isNotEmpty()) {
                Worker w = available.pull().value;
                w.putTaskAndSignal(command);
                return true;
            }

            if(Timeouts.noWait(timeout)) {
                return false;
            }

            if(nOfThreads < maxPoolSize) {
                Worker w = new Worker(mon.newCondition(), keepAliveTime);
                w.thread = new Thread(() -> threadFunc(command));
                nOfThreads += 1;
                w.thread.start();
                return true;
            }

            NodeLinkedList.Node<Work> node = work.push(new Work(command, mon.newCondition()));

            long limit = Timeouts.start(timeout);
            long remaining = Timeouts.remaining(limit);

            while(true) {
                try{
                    node.value.cond.await(remaining, TimeUnit.MILLISECONDS);
                }catch (InterruptedException e) {
                    // TODO
                    if(node.value.wasDelivered()) {
                        Thread.currentThread().interrupt();
                        return true;
                    }
                    work.remove(node);
                    throw e;
                }

                if(node.value.wasDelivered()) {
                    return true;
                }

                remaining = Timeouts.remaining(limit);

                if(Timeouts.isTimeout(remaining)){
                    work.remove(node);
                    return false;
                }

            }

        }finally {
            mon.unlock();
        }

    }

    private void threadFunc(Runnable firstTask){
        // run first task
        firstTask.run();

        while(true){
            Runnable work = getWork();
            if(work == null){
                return;
            }
            work.run();
            if(isShuttingDown()){
                return;
            }

        }
    }

    private boolean isShuttingDown() {
        try{
            mon.lock();
            if(work.isEmpty()){
                nOfThreads -= 1;
                if(nOfThreads == 0){
                    shutdown.signal();
                }
                return true;
            }
            return false;
        }finally {
            mon.unlock();
        }
    }


    private Runnable getWork() {
        try {
            mon.lock();

            if(work.isNotEmpty()){
               Work w = work.pull().value;
               return w.getWorkAndSignal();
            }

            NodeLinkedList.Node<Worker> node = available.push(new Worker(mon.newCondition(), keepAliveTime));

            while(true) {
                try {
                    node.value.cond.await(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

                } catch (InterruptedException e) {
                    // TODO
                    if(work.isNotEmpty()){
                       Thread.currentThread().interrupt();
                        Work w = work.pull().value;
                        return w.getWorkAndSignal();
                    }

                    // TODO
                }

                if (node.value.hasWork()) {
                    return node.value.task;

                }

                if (node.value.hasExpired()) {
                    nOfThreads -= 1;
                    return null;
                }
            }
        }finally {
            mon.unlock();
        }
    }

    public void shutdown(){
        try{
            mon.lock();
            isShutdown = true;
        }finally {
            mon.unlock();
        }

    }

    public boolean awaitTermination(int timeout) throws InterruptedException{
        try{
            mon.lock();

            if(Timeouts.noWait(timeout)){
                return false;
            }

            long limit = Timeouts.start(timeout);
            long remaining = Timeouts.remaining(limit);
            while(true){
                try{
                    shutdown.await(remaining, TimeUnit.MILLISECONDS);
                }catch (InterruptedException e){

                }

                remaining = Timeouts.remaining(limit);
                if(Timeouts.isTimeout(remaining)){
                    return false;
                }
            }
        }finally {
            mon.unlock();
        }
    }
}

