package serie2.Pt1;

import stack.Stack;

public class LinkedStack<E> implements Stack<E> {
    private static class Node<E> {
        E key;
        Node<E> link;
    }

    Node<E> head=null;
    int count;

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public E push(E t) {
        Node<E> n = new Node<>();
        n.key = t;
        n.link = head;
        head = n;
        ++count;
        return null;
    }

    @Override
    public E pop() {
        E result = head.key;
        head = head.link;
        --count;
        return result;
    }

    @Override
    public E peek() {
        return head.key;
    }
}
