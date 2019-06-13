package util.iterator;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TakeWhileSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private final Spliterator<T> src;
    private final Predicate<T> pred;
    private boolean [] end = {false};

    public TakeWhileSpliterator(Spliterator<T> src , Predicate<T> pred){
        super(src.estimateSize(), src.characteristics());
        this.src = src;
        this.pred = pred;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        //Return
        if(end [0]) return false;
        return src.tryAdvance(item -> {
            if(pred.test(item)) action.accept(item);
            else { end[0] = true;}  //can't take anymore
        } ) ;

    }
}
