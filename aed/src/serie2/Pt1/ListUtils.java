package serie2.Pt1;

import java.util.Comparator;
import queue.PriorityQueue;

public class ListUtils {

    public static <E> Node<E> occurAtLeastKTimes(Node<E>[] lists, Comparator<E> cmp, int k) {
        PriorityQueue<Node<E>> pq = new PriorityQueue<>(lists.length, (n1, n2) -> cmp.compare(n1.value, n2.value));
        for (Node<E> head : lists)
            if (head != null) pq.offer(head);

        Node<E> sentinel = new Node<>();
        sentinel.next = sentinel.previous = sentinel;

        Node<E> curr;
        int count = 1;
        boolean found;

        while (!pq.isEmpty()) {
            curr = pq.poll();
            found = false;
            if (curr.next != null) {
                pq.offer(curr.next);

                if (curr.next.value == curr.value) {
                    ++count;
                    found = true;
                }

            }
            if (!found && !pq.isEmpty()) {
                count = curr.value != pq.peek().value ? 1 : ++count;

            }
            if (count % k == 0) {
                insertLast(sentinel, curr.value);
                count = 1;
            }
        }

        return sentinel;
    }

    // Adiciona o elemento no fim da lista
    public static <E> void insertLast(Node<E> list, E e) {
        Node<E> x = new Node<>(e);
        x.previous = list.previous;
        list.previous.next = x;
        list.previous = x;
        x.next = list;
    }

    public static <E> void internalReverse(Node<Node<E>> list) {

        do {
            if (list.value != null)
                invertOrder(list.value);

            show(list.value);

        } while ((list = list.next) != null);
    }

    public static <E> void show(Node<E> list) {
        System.out.println("------------");
        Node<E> curr = list;
        while (curr != null) {
            System.out.print(curr.value + " ");
            curr = curr.next;
        }
        System.out.println();
    }

    private static <E> void invertOrder(Node<E> node) {
        Node<E> tail = node;
        while (tail.next != null) {
            tail = tail.next;
        }

        for (Node<E> i = node, j = tail; i != j && j != i.previous; i = i.next, j = j.previous) {
            E aux = i.value;
            i.value = j.value;
            j.value = aux;

        }
    }


}

