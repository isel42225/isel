package exercises;

import org.junit.Test;

import java.beans.EventHandler;

import static org.junit.Assert.*;

public class EventBusTest {

    @Test
    public void single_subscribe_and_publish() throws InterruptedException {
        EventBus eb = new EventBus(10);
        Thread t1 = new Thread(() ->
            eb.<Integer>subscribeEvent(System.out::println, Integer.class));

        Thread t2 = new Thread(() ->{
                    try {
                        // sleep to give time to subscribe
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    eb.publishEvent(20);
        });

        Thread t3 = new Thread(() -> {
            try{
                Thread.sleep(200);
                eb.shutdown();
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }

        });


        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();



    }
}