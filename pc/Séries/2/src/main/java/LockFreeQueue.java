import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> {

    private static class Node<T> {
        volatile T value;
        final AtomicReference<Node<T>> next = new AtomicReference<>();
        public Node(T value) {
            this.value = value;
        }
    }

    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;

    public LockFreeQueue() {
        Node<T> dummy = new Node<T>(null);
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public void enqueue(T value) {
        if(value == null)
            throw new IllegalArgumentException("Value cannot be null");

        final Node<T> node = new Node<>(value);

        while(true) {
            final Node<T> observedTail = tail.get();
            final Node<T> observedTailNext = observedTail.next.get();
            if (observedTailNext != null) {
                tail.compareAndSet(observedTail, observedTailNext);
                continue;
            }
            if(observedTail.next.compareAndSet(null, node)) {
                tail.compareAndSet(observedTail, node);
                return;
            }
        }
    }

    public T dequeue(){
        while (true) {

            final Node<T> observedHead = head.get();
            final Node <T> observedFirst = observedHead.next.get();  // first elem

            if(observedFirst != null && head.compareAndSet(observedHead, observedFirst)){
                final T val = observedFirst.value;
                observedFirst.value = null;
                return val;
            }

            // is empty
            return null;
        }
    }

    public boolean isEmpty(){
        return head.get() == tail.get();
    }

    public boolean isNotEmpty(){
        return !isEmpty();
    }
}
