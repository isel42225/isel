package serie2.Pt1;


import queue.HeapUtils;
import queue.Queue;

import java.util.Comparator;
import java.util.Iterator;

public class PriorityQueue<T> implements Queue<T>, Iterable<T> {
    T[] heap;
    int sizeHeap;
    Comparator<? super T> comparator;

    public PriorityQueue(int capacity, Comparator<? super T> cmp) {
        heap = (T[]) new Object[ capacity];
        comparator = cmp;
    }
    public PriorityQueue(T[] array, Comparator<? super T> cmp) {
        heap = array;
        HeapUtils.buildHeap(array, cmp );
        sizeHeap = array.length;
        comparator = cmp;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;
            public boolean hasNext() { return i < size(); }
            public T next()          { return heap[i++];  }
        };
    }

    @Override
    public boolean isEmpty() {
        return sizeHeap == 0;
    }

    @Override
    public int size() {
        return sizeHeap;
    }

    @Override
    public boolean offer(T t) { // O(lg n)
        heap[sizeHeap] = t;
        HeapUtils.increase( heap, sizeHeap, comparator);
        ++sizeHeap;
        return false;
    }


    @Override
    public T poll() {  // O(lg n)
        T aux = heap[0];
        heap[0] = heap[--sizeHeap];
        HeapUtils.heapify( heap, sizeHeap, 0, comparator );
        return aux;
    }

    @Override
    public T peek() { // O(1)
        return heap[0];
    }



}
