import utils.NodeLinkedList;
import utils.Timeouts;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueue<T> {

    private class Consumer {
        private final Condition cond;
        public Message msg;

        public Consumer(Condition cond) {
            this.cond = cond;
        }
    }

    private class Message implements SendStatus {
        public boolean wasSent;
        public final Condition received;
        public T data;

        public Message(T data, Condition received) {
            this.received = received;
            this.data = data;

        }

        @Override
        public boolean isSent() {
            return wasSent;
        }

        @Override
        public boolean tryCancel() {
            try{
                // TODO : mutual exclusion necessary ?
                mon.lock();
                if(wasSent) return false;
                messages.remove(this);
                return true;
            }finally {
                mon.unlock();
            }

        }

        @Override
        public boolean await(int timeout) throws InterruptedException {

            try{
                // TODO : mutual exclusion necessary ?
                mon.lock();

                if(isSent())return true;

                if(Timeouts.noWait(timeout)){
                    return false;
                }

                long limit = Timeouts.start(timeout);
                long remaining = Timeouts.remaining(limit);
                while(true){
                    try{
                        received.await(remaining, TimeUnit.MILLISECONDS);
                    }catch (InterruptedException e){
                        if(isSent()) {
                            Thread.currentThread().interrupt();
                            return true;
                        }
                        messages.remove(this);
                        throw e;
                    }

                    if(isSent()){
                        return true;
                    }

                    remaining = Timeouts.remaining(limit);
                    if(Timeouts.isTimeout(remaining)){
                        messages.remove(this);
                        return false;
                    }
                }

            }finally {
                mon.unlock();
            }


        }
    }

    private final Lock mon = new ReentrantLock();
    private final NodeLinkedList<Consumer> consumers = new NodeLinkedList<>();
    private final LinkedList<Message> messages = new LinkedList<>();

    public SendStatus send(T sentMsg) {
        try{
            mon.lock();

            //fast-path
            if(consumers.isNotEmpty()){
                Consumer cons = consumers.pull().value;
                cons.msg = new Message(sentMsg, mon.newCondition());
                cons.cond.signal();
                return cons.msg;
            }

            Message message = new Message(sentMsg, mon.newCondition());
            messages.add(message);
            return message;
        }
        finally {
            mon.unlock();
        }

    }

    public Optional<T> receive(int timeout) throws InterruptedException{
        try{
            mon.lock();

            // fast-path
            if(messages.size() > 0){
                Message message = messages.pop();
                message.wasSent = true;
                message.received.signal();
                return Optional.of(message.data);
            }

            if (Timeouts.noWait(timeout)) {
                return Optional.empty();
            }
            long limit = Timeouts.start(timeout);
            long remaining = Timeouts.remaining(limit);
            NodeLinkedList.Node<Consumer> node =
                    consumers.push(new Consumer(mon.newCondition()));

            while(true){
                try{
                    node.value.cond.await(remaining, TimeUnit.MILLISECONDS);
                }catch (InterruptedException e){
                    // TODO
                    if(messages.size() > 0){
                        Message message = messages.pop();
                        message.received.signal();
                        return Optional.of(message.data);
                    }
                    consumers.remove(node);
                    throw e;
                }

                if(messages.size() > 0){
                    Message message = messages.pop();
                    message.received.signal();
                    return Optional.of(message.data);
                }

                remaining = Timeouts.remaining(limit);
                if(Timeouts.isTimeout(remaining)){
                    consumers.remove(node);
                    return Optional.empty();
                }

            }
        }finally {
            mon.unlock();
        }
    }
}
