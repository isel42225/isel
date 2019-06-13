package exercises;

import exercises.KeyedExchanger;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class KeyedExchangerTest {


    // Expected to exit for timeout and returning empty
    @Test
    public void single_thread_exchange() throws InterruptedException{
        ///KeyedExchanger<Object> ke = new KeyedExchanger<>();
        KeyedExchanger<Object> ke = new KeyedExchanger<>();
        Optional [] res = new Optional[1];
        Thread single = new Thread(()-> {
            try {
                    res[0] = ke.exchange(1, new Object(), 1000);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        });

        single.start();
        single.join();

        assertEquals(Optional.empty(), res[0]);
    }

    // Simple exchange with 2 threads
    @Test
    public void single_pair_exchange() throws InterruptedException {
        ///KeyedExchanger<Object> ke = new KeyedExchanger<>();
        KeyedExchanger<Integer> ke = new KeyedExchanger<>();
        int [] vals = new int[2];
        Thread t1 = new Thread(() ->{
            try {
                vals[0] = ke.exchange(1, 10, 1000).get();
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        } );

        Thread t2 = new Thread(() ->{
            try {
                vals[1] = ke.exchange(1, 20, 1000).get();
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        } );

        // start the threads
        t1.start();
        t2.start();

        // wait for thread termination
        t1.join();
        t2.join();

        assertEquals(10, vals[1]);

    }

    @Test
    public void trio_exchange_thread() throws InterruptedException{
        KeyedExchanger<Integer> ke = new KeyedExchanger<>();
        int [] vals = new int[2];
        Optional [] empt = new Optional[1];
        Thread t1 = new Thread(() ->{
            try {
                vals[0] = ke.exchange(1, 10, 100).get();
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        } );

        Thread t2 = new Thread(() ->{
            try {
                vals[1] = ke.exchange(1, 20, 100).get();
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        } );

        // start the threads
        t1.start();
        t2.start();

        // wait for thread termination
        t1.join();
        t2.join();

        assertEquals(10, vals[1]);

        Thread t3 = new Thread(() -> {
            try {
                empt[0] = ke.exchange(1, 50 , 100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t3.start();
        t3.join();
        assertEquals(Optional.empty(),empt[0]);
    }


    @Test
    public void success_exchange_with_timeout() throws InterruptedException{

    }


}
