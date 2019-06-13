package util.iterator;

import util.Box;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class TakeFromFirstWhileIterator<T> implements Iterator<T> {
    private final Predicate<T> p;
    private final Iterator<T> src;
    private Box<T> curr;
    private boolean flag;


    public TakeFromFirstWhileIterator(Predicate<T> p, Iterator<T> src) {
        this.p = p;
        this.src = src;
        curr = Box.empty();


        Box<T> aux;
        while(src.hasNext() ){
            aux = Box.of(src.next());
            if(p.test(aux.getItem())){
                curr = aux;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        if(curr.isPresent()) return true;
        while(src.hasNext() && !flag ){
            T item = src.next();
            if(p.test(item)){
                curr = Box.of(item);
                return true;
            }
            flag = true;
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

