package org.hzt.utils.collections;

import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.DoubleMutableList;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.collections.primitives.LongMutableList;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.primitive_comparators.LongComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Lists {

    private Lists() {
    }

    /**
     * See <a href="https://en.wikipedia.org/wiki/Quickselect">Quick select</a>
     *
     * @param list a list of Comparable objects
     * @param n    the position of the desired object, using the ordering defined on the list elements
     * @return the nth smallest object
     */
    public static <T> T nthSmallest(int n, Comparator<T> comp, List<T> list) {
        final var underPivot = new ArrayList<T>();
        final var overPivot = new ArrayList<T>();
        final var equalPivot = new ArrayList<T>();
        // choosing a pivot is a whole topic in itself.
        // this implementation uses the simple strategy of grabbing something from the middle of the ArrayList.
        final var pivot = list.get(n / 2);
        // split coll into 3 lists based on comparison with the pivot
        for (final var item : list) {
            final var order = comp.compare(item, pivot);
            if (order < 0) {
                underPivot.add(item);
            } else if (order > 0) {
                overPivot.add(item);
            } else {
                equalPivot.add(item);
            }
        }
        // recurse on the appropriate list
        if (n < underPivot.size()) {
            return nthSmallest(n, comp, underPivot);
            // equal to pivot; just return it
        } else if (n < underPivot.size() + equalPivot.size()) {
            return pivot;
            // everything in underPivot and equalPivot is too small.  Adjust n accordingly in the recursion.
        } else {
            return nthSmallest(n - underPivot.size() - equalPivot.size(), comp, overPivot);
        }
    }

    public static <T> T nthSmallest(int n, Comparator<T> comp, ListX<T> list) {
        return nthSmallest(n, comp, list.toList());
    }

    public static int nthSmallest(int n, IntComparator comp, IntList list) {
        final var underPivot = IntMutableList.empty();
        final var overPivot = IntMutableList.empty();
        final var equalPivot = IntMutableList.empty();
        // choosing a pivot is a whole topic in itself.
        // this implementation uses the simple strategy of grabbing something from the middle of the ArrayList.
        final var pivot = list.get(n / 2);
        // split coll into 3 lists based on comparison with the pivot
        for (final var iterator = list.iterator(); iterator.hasNext(); ) {
            final var item = iterator.nextInt();
            final var order = comp.compare(item, pivot);
            if (order < 0) {
                underPivot.add(item);
            } else if (order > 0) {
                overPivot.add(item);
            } else {
                equalPivot.add(item);
            }
        }
        // recurse on the appropriate list
        if (n < underPivot.size()) {
            return nthSmallest(n, comp, underPivot);
            // equal to pivot; just return it
        } else if (n < underPivot.size() + equalPivot.size()) {
            return pivot;
            // everything in underPivot and equalPivot is too small.  Adjust n accordingly in the recursion.
        } else {
            return nthSmallest(n - underPivot.size() - equalPivot.size(), comp, overPivot);
        }
    }

    public static long nthSmallest(int n, LongComparator comp, LongList list) {
        final var underPivot = LongMutableList.empty();
        final var overPivot = LongMutableList.empty();
        final var equalPivot = LongMutableList.empty();
        // choosing a pivot is a whole topic in itself.
        // this implementation uses the simple strategy of grabbing something from the middle of the ArrayList.
        final var pivot = list.get(n / 2);
        // split coll into 3 lists based on comparison with the pivot
        for (final var iterator = list.iterator(); iterator.hasNext(); ) {
            final var item = iterator.nextLong();
            final var order = comp.compare(item, pivot);
            if (order < 0) {
                underPivot.add(item);
            } else if (order > 0) {
                overPivot.add(item);
            } else {
                equalPivot.add(item);
            }
        }
        // recurse on the appropriate list
        if (n < underPivot.size()) {
            return nthSmallest(n, comp, underPivot);
            // equal to pivot; just return it
        } else if (n < underPivot.size() + equalPivot.size()) {
            return pivot;
            // everything in underPivot and equalPivot is too small.  Adjust n accordingly in the recursion.
        } else {
            return nthSmallest(n - underPivot.size() - equalPivot.size(), comp, overPivot);
        }
    }

    public static double nthSmallest(int n, DoubleComparator comp, DoubleList list) {
        final var underPivot = DoubleMutableList.empty();
        final var overPivot = DoubleMutableList.empty();
        final var equalPivot = DoubleMutableList.empty();
        // choosing a pivot is a whole topic in itself.
        // this implementation uses the simple strategy of grabbing something from the middle of the ArrayList.
        final var pivot = list.get(n / 2);
        // split coll into 3 lists based on comparison with the pivot
        for (final var iterator = list.iterator(); iterator.hasNext(); ) {
            final var item = iterator.nextDouble();
            final var order = comp.compare(item, pivot);
            if (order < 0) {
                underPivot.add(item);
            } else if (order > 0) {
                overPivot.add(item);
            } else {
                equalPivot.add(item);
            }
        }
        // recurse on the appropriate list
        if (n < underPivot.size()) {
            return nthSmallest(n, comp, underPivot);
            // equal to pivot; just return it
        } else if (n < underPivot.size() + equalPivot.size()) {
            return pivot;
            // everything in underPivot and equalPivot is too small.  Adjust n accordingly in the recursion.
        } else {
            return nthSmallest(n - underPivot.size() - equalPivot.size(), comp, overPivot);
        }
    }
}
