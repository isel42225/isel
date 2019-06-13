package util.iterator;

import util.Box;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class TakeWhileIterator<T> implements Iterator<T> {

    private final Predicate<T> p;
    private final Iterator<T> src;
    private Box<T> curr;

    public TakeWhileIterator(Predicate<T> p, Iterator<T> src) {
        this.p = p;
        this.src = src;
        curr = Box.empty();
    }

    @Override
    public boolean hasNext() {
        if(curr.isPresent())return true;
        if(src.hasNext()){
            T item = src.next();
            if(!p.test(item)){
                return false;
            }
            curr = Box.of(item);
            return true;
        }
        return false;
    }


    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        T aux = curr.getItem();
        curr = Box.empty();
        return aux;
    }
}
