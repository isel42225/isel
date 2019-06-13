import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class UnsafeMessageBoxTest {

    @Test
    public void shouldNotConsumeMoreThanInitialLives() throws InterruptedException {

        final int nOfLives = 10;
        final int nOfThreads = 20;
        final int expectedFailed = nOfThreads - nOfLives;
        final String msg = "Hello World";

        final UnsafeMessageBox<String> mb = new UnsafeMessageBox<>();

        mb.publish(msg, nOfLives);

        final List<Thread> ths = new ArrayList<>();
        final List<String> res = Collections.synchronizedList(new ArrayList<>());   // ??

        for(int i = 0 ; i < nOfThreads ; ++i){
            Thread t = new Thread(() -> res.add(mb.tryConsume()));
            t.start();
            ths.add(t);
        }

        for(Thread t : ths){
            t.join();
        }

        int consumed = 0;
        int failed = 0;
        for(String s : res){
            if(s != null){
                consumed++;
            }
            else {
                failed++;
            }
        }

        assertEquals(nOfLives,consumed);
        assertEquals(expectedFailed,failed);
    }

}