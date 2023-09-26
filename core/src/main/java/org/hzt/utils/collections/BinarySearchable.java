package org.hzt.utils.collections;

import org.hzt.utils.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.function.IntUnaryOperator;

/**
 * @param <COMPARISON_FUNCTION> The comparison function
 */
public interface BinarySearchable<COMPARISON_FUNCTION> {

    int size();

    default int binarySearchTo(final int toIndex, final COMPARISON_FUNCTION comparison) {
        return binarySearch(0, toIndex, comparison);
    }

    default int binarySearchFrom(final int fromIndex, final COMPARISON_FUNCTION comparison) {
        return binarySearch(fromIndex, size(), comparison);
    }

    default int binarySearch(final COMPARISON_FUNCTION comparison) {
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
     * @see java.util.Arrays#binarySearch(Object[], Object, Comparator)
     */
    int binarySearch(int fromIndex, int toIndex, COMPARISON_FUNCTION comparison);

    /**
     * This function only returns a valid value if the indexed collection to search in is sorted
     *
     * @param size               the size of the indexed collection to search in
     * @param fromIndex          the index to search from
     * @param toIndex            the index to search until
     * @param comparisonFunction the function used to determine if the element is found
     * @return the index of the element to be found, if not found: Returns the inverted insertion point.
     * (The index it should have been as negative value)
     */
    static int binarySearch(final int size,
                            final int fromIndex,
                            final int toIndex,
                            @NotNull final IntUnaryOperator comparisonFunction) {
        PreConditions.rangeCheck(size, fromIndex, toIndex);

        var low = fromIndex;
        var high = toIndex - 1;

        while (low <= high) {
            final var mid = (low + high) >>> 1;
            final var comparison = comparisonFunction.applyAsInt(mid);

            if (comparison < 0) {
                low = mid + 1;
            } else if (comparison > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }
}
