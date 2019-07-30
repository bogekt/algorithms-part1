/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 18/07/2019
 *  Description: The randomized queue is similar to a stack or queue,
 *  except that the item removed is chosen uniformly at random among items in the data structure.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        items = (Item[]) new Object[2];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (size == items.length)
            items = cloneAndZip(items, size, items.length * 2);

        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int i = randomIndex(items);

        Item item = items[i];
        items[i] = null;
        size--;

        if (size > 0 && size == items.length / 4)
            items = cloneAndZip(items, size, items.length / 2);

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        return items[randomIndex(items)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] randomIndexes = new int[size];
        private int currentIndex = 0;

        public RandomizedQueueIterator() {
            for (int i = 0; i < size; i++) {
                boolean isDuplicateIndex;
                int randomIndex;

                do {
                    isDuplicateIndex = false;
                    randomIndex = randomIndex(items);

                    for (int j = 0; j < i; j++) {
                        isDuplicateIndex = randomIndexes[j] == randomIndex;

                        if (isDuplicateIndex) break;
                    }
                } while (isDuplicateIndex);

                randomIndexes[i] = randomIndex;
            }
        }

        public boolean hasNext() {
            return currentIndex < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            return items[randomIndexes[currentIndex++]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Integer[] array;
        Object[] clone;
        RandomizedQueue<Integer> queue;

        // 1 randomIndex returns all indexes
        array = new Integer[] { 1, 2, 3, 4, 5 };
        queue = new RandomizedQueue<>();
        for (int i = 0; i < array.length; i++) {
            int times = 100;
            while (!array[queue.randomIndex(array)].equals(array[i]))
                times--;
            assert times > 0;
        }

        // 2 cloneAndZip
        // 2.1 deletes null and clones items into bigger array
        array = new Integer[] { 1, null, null, 2, 3, 4, null, 5 };
        queue = new RandomizedQueue<>();
        clone = queue.cloneAndZip(array, 5, 8);
        for (int i = 0; i < 5; i++)
            assert clone[i].equals(i + 1);
        // 2.2 deletes null and clones items into smaller array
        array = new Integer[] { null, 2, null, null, 5, null, null, null, null };
        queue = new RandomizedQueue<>();
        clone = queue.cloneAndZip(array, 2, 4);
        assert clone[0].equals(2);
        assert clone[1].equals(5);

        // 3 RandomizedQueue
        array = new Integer[] { 1, 2, 3, 4, 5 };
        queue = new RandomizedQueue<>();

        // 3.1 size 0 and isEmpty
        assert queue.size() == 0 && queue.isEmpty();

        for (int i = 0; i < array.length; i++)
            queue.enqueue(array[i]);

        // 3.1 size 5 and !isEmpty
        assert queue.size() == array.length && !queue.isEmpty();

        // 3.2 sample returns all enqueued items
        for (int i = 0; i < queue.size(); i++) {
            int times = 100;
            while (!queue.sample().equals(array[i]))
                times--;
            assert times > 0;
        }

        // 3.3 dequeue returns and removes all enqueued items
        int size = queue.size();
        int initialSize = size;
        int dequeueCount = 0;
        while (size-- > 0) {
            int item = queue.dequeue();
            int i = 0;
            while (!array[i++].equals(item)) continue;
            dequeueCount++;
        }
        assert dequeueCount == initialSize;

        // 3.4 size 0 and isEmpty after dequeued all items
        assert queue.size() == 0 && queue.isEmpty();

        // 4 iterator
        // 4.1 simple has elements after enqueue
        array = new Integer[] { 0, 1, 2, 3, 4, 5, 7, 8, 9, 10 };
        queue = new RandomizedQueue<>();

        for (int i = 0; i < array.length; i++)
            queue.enqueue(array[i]);

        for (int item : queue) {
            boolean hasItem = false;

            for (int i = 0; i < array.length; i++) {
                hasItem = array[i] == item;

                if (hasItem) break;
            }

            assert hasItem;
        }

        // 4.2 simple has elements after enqueue and dequeue
        Object[] queueItems = queue.items;
        int itemsCount = queueItems.length;
        int queueSize = queue.size();
        // 1/4 of items size for shrinked array
        int dequeuedLength = queueSize - itemsCount / 4;
        int[] dequeuedItems = new int[dequeuedLength];

        for (int i = 0; i < dequeuedLength; i++)
            dequeuedItems[i] = queue.dequeue();

        for (int item : queue) {
            boolean hasItem = false;
            boolean isDequeued = false;

            for (int i = 0; i < array.length; i++) {
                hasItem = array[i] == item;

                if (hasItem) break;
            }

            for (int i = 0; i < dequeuedItems.length; i++) {
                isDequeued = dequeuedItems[i] == item;

                if (isDequeued) break;
            }

            assert hasItem && !isDequeued;
        }

        // 4.3 multiple iterators can be used simultaneously
        array = new Integer[] { 0, 1, 2, 3, 4, 5, 7, 8, 9, 10 };
        queue = new RandomizedQueue<>();
        for (int i = 0; i < array.length; i++)
            queue.enqueue(array[i]);

        int i = 0;
        for (int item1 : queue) {
            int j = 0;
            boolean hasItem1 = false;

            for (int item2 : queue) {
                boolean hasItem2 = false;

                for (int k = 0; k < array.length; k++) {
                    hasItem2 = array[k] == item2;

                    if (hasItem2) break;
                }

                j++;

                assert hasItem2;
            }

            assert j == array.length && j == queue.size();

            for (int k = 0; k < array.length; k++) {
                hasItem1 = array[k] == item1;

                if (hasItem1) break;
            }

            i++;

            assert hasItem1;
        }

        assert i == array.length && i == queue.size();
    }

    private Item[] cloneAndZip(Item[] array, int length, int cloneLength) {
        Item[] newArray = (Item[]) new Object[cloneLength];

        int copyItems = length;
        int i = 0;
        int j = 0;

        while (copyItems-- > 0) {
            while (array[i] == null)
                i++;
            newArray[j++] = array[i++];
        }

        return newArray;
    }

    private int randomIndex(Item[] array) {
        int i;

        do {
            i = StdRandom.uniform(array.length);
        } while (array[i] == null);

        return i;
    }
}