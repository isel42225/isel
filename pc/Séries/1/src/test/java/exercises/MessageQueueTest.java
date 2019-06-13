package exercises;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.*;

public class MessageQueueTest {

    @Test
    public void test1() throws InterruptedException {
        MessageQueue<Integer> mq = new MessageQueue<>();
        Random rand = new Random();

        ArrayList<Thread> producers = new ArrayList<>();
        ArrayList<Thread> consumers = new ArrayList<>();
        ArrayList<SendStatus> msgs = new ArrayList<>();
        ArrayList<Optional<Integer>> received = new ArrayList<>();

        int nOfProd = 10;
        int nOfConsumers = 5;


        for(int i = 0; i < nOfProd ; ++i){
            Thread th = new Thread(() -> msgs.add(mq.send(rand.nextInt())));
            producers.add(th);
        }

        // start all producer threads
        producers.forEach(Thread::start);

        for(int j = 0; j < nOfConsumers ; ++j){
            Thread th = new Thread(() -> {
                try {
                    received.add(mq.receive(1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            consumers.add(th);
        }

        consumers.forEach(Thread::start);

        for(Thread t : producers){
            t.join();
        }

        for(Thread t : consumers){
            t.join();
        }

        for(Optional<Integer> i : received){
            System.out.println("Message received was : "+ i.get());
        }

        for(SendStatus msg : msgs){
            msg.await(1000);
            System.out.println("Messages was sent ? : " + msg.isSent());
        }

    }

}