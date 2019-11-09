/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class DynamicMedian<T> {
    private T[] a;
    private int n;

    public DynamicMedian(int capacity) {
        // accounts for '0' not being used
        this.a = (T[]) new Object[capacity + 1];
        this.n = 0;
    }

    public void insert(T k) {
        a[++n] = k;
        swim(n);
    }

    public T delMedian() {
        T median = findMedian();
        exch(1, n--);
        sink(1);
        a[n + 1] = null;

        return median;
    }

    public T findMedian() {
        if (even(n) && n > 1) return a[2];
        return a[1];
    }

    private void swim(int k) {
        while (even(k) && k > 1 && less(k / 2, k)) {
            exch(k, k / 2);

            if ((n > k) && less(k + 1, k / 2)) exch(k + 1, k / 2);
            k = k / 2;
        }

        while (!even(k) && (k > 1 && !less(k / 2, k))) {
            exch(k, k / 2);

            if (!less(k - 1, k / 2)) exch(k - 1, k / 2);
            k = k / 2;
        }

    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && less(j, k)) j++;
            if (less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    private boolean even(int i) {
        if ((i % 2) == 0) return true;
        else return false;
    }

    private void exch(int i, int j) {
        T temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private boolean less(int i, int j) {
        if (((Comparable<T>) a[i]).compareTo(a[j]) < 0) return true;
        else return false;
    }


    public static void main(String[] args) {
        DynamicMedian<Integer> medianInsertDelete = new DynamicMedian<Integer>(10);
        medianInsertDelete.insert(3);
        assert medianInsertDelete.findMedian() == Integer.valueOf(3);

        medianInsertDelete.insert(8);
        assert medianInsertDelete.findMedian() == Integer.valueOf(3);

        medianInsertDelete.insert(7);
        assert medianInsertDelete.findMedian() == Integer.valueOf(7);

        medianInsertDelete.insert(6);
        assert medianInsertDelete.findMedian() == Integer.valueOf(6);

        medianInsertDelete.insert(9);
        assert medianInsertDelete.findMedian() == Integer.valueOf(7);

        medianInsertDelete.insert(5);
        assert medianInsertDelete.findMedian() == Integer.valueOf(6);

        medianInsertDelete.insert(2);
        assert medianInsertDelete.findMedian() == Integer.valueOf(6);

        medianInsertDelete.delMedian();
        assert medianInsertDelete.findMedian() == Integer.valueOf(5);
    }
}
