package util.iterator;

import util.Box;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class FlatMapIterator<T , R> implements Iterator<R> {
    private final Function<T , Iterable<R>> mapper;
    private final Iterator<T> src;
    private Box<Iterator<R>> curr;

    public FlatMapIterator(Function<T, Iterable<R>> mapper, Iterator<T> src) {
        this.mapper = mapper;
        this.src = src;
        curr = Box.empty();
    }

    @Override
    public boolean hasNext() {
        if(curr.isPresent())return true;
        if (src.hasNext()){
           curr = Box.of(mapper.apply(src.next()).iterator());
           return true;
        }
        return false;
    }

    @Override
    public R next() {
        if(!hasNext()) throw new NoSuchElementException();
        R aux = curr.getItem().next();
        if(!curr.getItem().hasNext()) curr = Box.empty();
        return aux;
    }
}
