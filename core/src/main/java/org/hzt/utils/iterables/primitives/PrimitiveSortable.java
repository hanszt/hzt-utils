package org.hzt.utils.iterables.primitives;

import org.hzt.utils.primitive_comparators.PrimitiveComparator;

/**
 * @param <C> the primitive comparator
 */
public interface PrimitiveSortable<C extends PrimitiveComparator> {

    PrimitiveSortable<C> sorted();

    PrimitiveSortable<C> sorted(C comparator);

    PrimitiveSortable<C> sortedDescending();

    /**
     * This method offers O(1 - log(n)) time complexity when called against an unsorted sortable
     *
     * If it is sorted, the method has time complexity O(n) (Worst case)
     *
     * Can for example be used before binarySearch which requires a sorted data structure
     *
     * @param comparator the comparator to use for testing weather the Primitive Sortable is sorted
     * @return true if it is sorted else false
     */
    boolean isSorted(C comparator);

    boolean isSorted();
}
