package org.hzt.utils.markerinterfaces;

import java.util.Comparator;

/**
 * @param <COMPARISON_FUNCTION> The comparison function
 */
public interface BinarySearchable<COMPARISON_FUNCTION> {

    int size();

    default int binarySearchTo(int toIndex, COMPARISON_FUNCTION comparison) {
        return binarySearch(0, toIndex, comparison);
    }

    default int binarySearchFrom(int fromIndex, COMPARISON_FUNCTION comparison) {
        return binarySearch(fromIndex, size(), comparison);
    }

    default int binarySearch(COMPARISON_FUNCTION comparison) {
        return binarySearch(0, size(), comparison);
    }

    /**
     * Searches this list or its range for an element for which the given [comparison] function
     * returns zero using the binary search algorithm.
     * <p>
     * The list is expected to be sorted so that the signs of the [comparison] function's return values ascend on the list elements,
     * i.e. negative values come before zero and zeroes come before positive values.
     * Otherwise, the result is undefined.
     * <p>
     * If the list contains multiple elements for which [comparison] returns zero, there is no guarantee which one will be found.
     *
     * @param comparison function that returns zero when called on the list element being searched.
     *                   On the elements coming before the target element, the function must return negative values;
     *                   on the elements coming after the target element, the function must return positive values.
     * @return the index of the found element, if it is contained in the list within the specified range;
     * otherwise, the inverted insertion point `(-insertion point - 1)`.
     * The insertion point is defined as the index at which the element should be inserted,
     * so that the list (or the specified subrange of list) still remains sorted.
     *
     * @see java.util.Arrays#binarySearch(Object[], Object, Comparator)
     */
    int binarySearch(int fromIndex, int toIndex, COMPARISON_FUNCTION comparison);
}
