/* *****************************************************************************
 *  Name: Eugene Borys
 *  Date: 6/03/2019
 *  Description: Use this class for modeling sites grid of n by n size
 *               It contains next API:
 *                  - check system is percolates
 *                  - check site is open
 *                  - check site is full
 *                  - open sites
 *                  - number of all open sites
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int virtualBottom = 0;
    private static final int virtualTop = 1;

    @FunctionalInterface
    private interface BiConsumer<T, U> {
        void accept(T t, U u);
    }

    @FunctionalInterface
    private interface BiFunction<T, U, R> {
        R apply(T t, U u);
    }

    @FunctionalInterface
    private interface BiPredicate<T, U> {
        boolean test(T t, U u);
    }

    @FunctionalInterface
    private interface Function<T, R> {
        R apply(T t);
    }

    @FunctionalInterface
    private interface Predicate<T> {
        boolean test(T t);
    }

    private enum SiteStates {
        Closed,
        Open,
        Full,
    }

    private enum SiteSides {
        Left,
        Right,
        Top,
        Bottom,
    }

    private class SiteContext {
        private final int row;
        private final int col;
        private final int leftCol;
        private final int rightCol;
        private final int topRow;
        private final int bottomRow;
        private final int center;
        private final int left;
        private final int right;
        private final int top;
        private final int bottom;
        Predicate<Integer> isGreaterOfFirstRow;
        Predicate<Integer> isLessOfLastRow;

        SiteContext(int row, int col) {
            this(row, col, xyTo1D.apply(col, row));
        }

        SiteContext(int row, int col, int center) {
            this.row = row;
            this.col = col;
            this.center = center;

            leftCol = col - 1;
            rightCol = col + 1;
            topRow = row - 1;
            bottomRow = row + 1;

            left = xyTo1D.apply(leftCol, row);
            right = xyTo1D.apply(rightCol, row);
            top = xyTo1D.apply(col, topRow);
            bottom = xyTo1D.apply(col, bottomRow);

            int rowFirst = xyTo1D.apply(1, row);
            isGreaterOfFirstRow = j -> j >= rowFirst;

            int rowLast = xyTo1D.apply(in, row);
            isLessOfLastRow = j -> j <= rowLast;
        }

        Integer getNeighborIndex(SiteSides side) {
            switch (side) {
                case Left:
                    return left;
                case Right:
                    return right;
                case Top:
                    return top;
                case Bottom:
                    return bottom;
                default:
                    return null;
            }
        }

        SiteContext getNeighbor(SiteSides side) {
            switch (side) {
                case Left:
                    return new SiteContext(row, leftCol);
                case Right:
                    return new SiteContext(row, rightCol);
                case Top:
                    return new SiteContext(topRow, col);
                case Bottom:
                    return new SiteContext(bottomRow, col);
                default:
                    return null;
            }
        }
    }

    private final int in;
    private final int n;
    private final int n2;
    private final SiteStates[] stateArray;
    private final WeightedQuickUnionUF unionFind;
    private boolean isOneSite;
    private boolean isPercolated;
    private int openSites;
    private Predicate<Integer> isValidBound;
    private Predicate<Integer> canBeFull;
    private Predicate<SiteContext> needBeFull;
    private Predicate<Integer> isOpen;
    private Predicate<Integer> isFull;
    private BiFunction<Integer, Integer, Integer> xyTo1D;
    private Predicate<Integer> isTop;
    private Predicate<Integer> isGreaterOfFirstTop;
    private Predicate<Integer> isLessOfLastBottom;
    private BiPredicate<Integer, SiteContext>[] tryConnectNeighbors;
    private Function<SiteContext, SiteContext>[] tryMakeFullNeighbors;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) throw new java.lang.IllegalArgumentException(String.valueOf(n));
        if (n == 1) isOneSite = true;

        in = n;
        this.n = n + 1;
        n2 = this.n * this.n;
        stateArray = new SiteStates[n2];
        unionFind = new WeightedQuickUnionUF(n2);

        openSites = 0;
        isPercolated = false;
        isValidBound = i -> i >= 1 && i <= in;
        xyTo1D = (x, y) -> x + y * this.n;
        isOpen = i -> stateArray[i] != null && stateArray[i].compareTo(SiteStates.Closed) > 0;

        int topFirst = xyTo1D.apply(1, 1);
        int topLast = xyTo1D.apply(in, 1);
        isTop = i -> i >= topFirst && i <= topLast;
        isGreaterOfFirstTop = j -> j >= topFirst;

        int bottomFirst = xyTo1D.apply(1, in);
        int bottomLast = xyTo1D.apply(in, in);
        isLessOfLastBottom = j -> j <= bottomLast;

        for (int i = topFirst; i <= topLast; ++i)
            unionFind.union(virtualTop, i);
        for (int i = bottomFirst; i <= bottomLast; ++i)
            unionFind.union(virtualBottom, i);

        isFull = i -> stateArray[i] != null && stateArray[i].compareTo(SiteStates.Open) > 0;
        canBeFull = j -> !isFull.test(j) && isOpen.test(j);

        BiPredicate[] validationPredicates = new BiPredicate[] {
                (BiPredicate<Integer, SiteContext>) (i, c) -> c.isGreaterOfFirstRow.test(i),
                (BiPredicate<Integer, SiteContext>) (i, c) -> c.isLessOfLastRow.test(i),
                (BiPredicate<Integer, SiteContext>) (i, c) -> isGreaterOfFirstTop.test(i),
                (BiPredicate<Integer, SiteContext>) (i, c) -> isLessOfLastBottom.test(i)
        };

        tryConnectNeighbors = new BiPredicate[4];
        tryMakeFullNeighbors = new Function[4];
        needBeFull = c -> isTop.test(c.center);

        for (int i = 0; i < validationPredicates.length; i++) {
            final SiteSides side = SiteSides.values()[i];
            BiPredicate<Integer, SiteContext> validate = validationPredicates[i];

            final Predicate<SiteContext> chainNeedBeFull = needBeFull;
            needBeFull = c -> {
                int sideIndex = c.getNeighborIndex(side);
                return chainNeedBeFull.test(c) ||
                        (validate.test(sideIndex, c) && isFull.test(sideIndex));
            };
            tryConnectNeighbors[i] = (j, c) -> tryConnect(
                    j, c.getNeighborIndex(side), k -> validate.test(k, c) && isOpen.test(k));
            tryMakeFullNeighbors[i] = (c) -> tryMakeFull(
                    c.getNeighborIndex(side), j -> validate.test(j, c) && canBeFull.test(j)
            ) ? c.getNeighbor(side) : null;
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        int i = xyTo1D.apply(col, row);

        if (isOpen.test(i)) return;

        // make open
        stateArray[i] = SiteStates.Open;
        openSites++;

        if (isOneSite) {
            isPercolated = true;

            return;
        }

        SiteContext siteContext = new SiteContext(row, col);

        // connect adjacent
        for (BiPredicate<Integer, SiteContext> tryConnect : tryConnectNeighbors)
            if (tryConnect.test(i, siteContext)) isPercolated = checkPercolates();

        // make full
        if (!needBeFull.test(siteContext)) return;

        stateArray[i] = SiteStates.Full;
        full(siteContext);
    }

    private void full(SiteContext siteContext) {
        SiteContext fullNeighborContext;

        for (Function<SiteContext, SiteContext> tryMakeFullNeighbor : tryMakeFullNeighbors)
            if ((fullNeighborContext = tryMakeFullNeighbor.apply(siteContext)) != null)
                full(fullNeighborContext);
    }

    // is site (row, col) open?s
    public boolean isOpen(int row, int col) {
        validate(row, col);

        return isOpen.test(xyTo1D.apply(col, row));
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);

        return isFull.test(xyTo1D.apply(col, row));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isPercolated;
    }

    // test client (optional)
    public static void main(String[] args) {
        // todo output passed/failed

        // numberOfOpenSites
        int n = 3;
        final Percolation percolation = new Percolation(n);
        assert percolation.numberOfOpenSites() == 0;

        // exceptions
        try {
            new Percolation(-1);
            assert false;
        }
        catch (java.lang.IllegalArgumentException ex) {
        }
        BiConsumer<Integer, Integer> openTestIndexOutOfBoundsException = (row, col) -> {
            try {
                percolation.open(row, col);
                assert false;
            }
            catch (java.lang.IndexOutOfBoundsException ex) {
            }
        };
        openTestIndexOutOfBoundsException.accept(n + 1, 1);
        openTestIndexOutOfBoundsException.accept(0, 1);

        // open, isOpen
        assert !percolation.isOpen(2, 2);
        assert percolation.openSites == 0;
        percolation.open(2, 2);
        assert percolation.isOpen(2, 2);
        assert percolation.openSites == 1;

        // percolates
        assert !percolation.percolates();
        percolation.open(1, 2);
        percolation.open(n, 2);
        assert percolation.percolates();

        // is full
        for (int i = 1; i <= n; i++)
            assert !percolation.isFull(i, 1) && percolation.isFull(i, 2);
    }

    private boolean checkPercolates() {
        return isPercolated || unionFind.connected(virtualTop, virtualBottom);
    }

    private boolean tryConnect(int p, int q, Predicate<Integer> isConnectable) {
        if (!isConnectable.test(q)) return false;

        unionFind.union(p, q);

        return true;
    }

    private boolean tryMakeFull(int i, Predicate<Integer> isFullable) {
        if (!isFullable.test(i)) return false;

        stateArray[i] = SiteStates.Full;

        return true;
    }

    private void validate(int row, int col) {
        validate(row, "row");
        validate(col, "col");
    }

    private void validate(int i, String prefix) {
        if (!isValidBound.test(i))
            throw new IndexOutOfBoundsException(prefix + " index out of bounds");
    }
}