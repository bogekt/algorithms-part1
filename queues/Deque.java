/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 16/07/2019
 *  Description: The double-ended queue or deque is a generalization of a stack and a queue.
 *  It supports adding and removing items from either the front or the back of the data structure.
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        Node previous;
        Node next;
        Item value;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        boolean wasEmpty = isEmpty();
        Node newFirst = new Node();
        newFirst.next = first;
        newFirst.value = item;

        if (!wasEmpty) first.previous = newFirst;

        first = newFirst;

        if (wasEmpty) last = first;

        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        boolean wasEmpty = isEmpty();
        Node newLast = new Node();
        newLast.previous = last;
        newLast.value = item;

        if (!wasEmpty) last.next = newLast;

        last = newLast;

        if (wasEmpty) first = last;

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();

        Item value = first.value;
        first = first.next;

        // loitering
        if (first != null) first.previous = null;

        size--;

        return value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();

        Item value = last.value;
        last = last.previous;

        // loitering
        if (last != null) last.next = null;

        size--;

        return value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = current.value;
            current = current.next;

            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque;
        int N = 10;

        // 1 addFirst N times and removeLast N times returns same values
        deque = new Deque<>();

        for (int i = 0; i < N; i++)
            deque.addFirst(i);
        assert deque.size() == N && !deque.isEmpty();

        for (int i = 0; i < N; i++)
            assert deque.removeLast() == i;
        assert deque.size() == 0 && deque.isEmpty();

        // 2 addLast N times and removeFirst N times returns same values
        deque = new Deque<>();

        for (int i = 0; i < N; i++)
            deque.addLast(i);
        assert deque.size() == N && !deque.isEmpty();

        for (int i = 0; i < N; i++)
            assert deque.removeFirst() == i;
        assert deque.size() == 0 && deque.isEmpty();

        // 3 addFirst addLast, removeFirst, removeLast work together
        deque = new Deque<>();
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(3);
        deque.addLast(4);

        assert deque.size() == 4;
        assert deque.removeFirst() == 1 && deque.removeFirst() == 2;
        assert deque.removeLast() == 4 && deque.removeLast() == 3;

        // 4 iterator
        // 4.1 addFirst
        deque = new Deque<>();
        int i;

        for (i = 0; i < N; i++)
            deque.addFirst(i);

        i = 0;
        for (int item : deque)
            assert item == N - i++ - 1;

        // 4.2 addLast
        deque = new Deque<>();

        for (i = 0; i < N; i++)
            deque.addLast(i);

        i = 0;
        for (int item : deque)
            assert item == i++;

        // 4.3 multiple iterators can be used simultaneously
        deque = new Deque<>();
        for (i = 0; i < N; i++)
            deque.addLast(i);

        i = 0;
        for (int a : deque) {
            int j = 0;
            for (int b : deque)
                assert b == j++;
            assert a == i++;
        }
    }
}
