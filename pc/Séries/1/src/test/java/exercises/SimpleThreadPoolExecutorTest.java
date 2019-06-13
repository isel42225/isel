package exercises;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

import static org.junit.Assert.*;

public class SimpleThreadPoolExecutorTest {



    @Test
    public void should_execute_all_tasks() throws InterruptedException {

        final int poolSize = 10;
        final int nOfReps = 20;
        final int keepAliveTime = 1000;
        final int timeout = 100;

        SimpleThreadPoolExecutor tp = new SimpleThreadPoolExecutor(poolSize, keepAliveTime);
        ArrayList<Boolean> res = new ArrayList<>();
        for(int i = 0; i < nOfReps; ++i){
            res.add(
                    tp.execute(() -> {} , timeout)
            );
        }

       tp.awaitTermination(2000);
        for(Boolean b : res){
            assertEquals(true, b);
        }
    }

}