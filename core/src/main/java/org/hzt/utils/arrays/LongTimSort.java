package org.hzt.utils.arrays;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.LongComparator;

/**
 * A Tim sort implementation for sorting long arrays where a LongComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class LongTimSort extends PrimitiveTimSort<long[], LongComparator> {

    private LongTimSort(final long[] longArray, final LongComparator longComparator) {
        super(longArray.length, longArray, new long[getInitTempLength(longArray.length)], longComparator);
    }

    static void sort(final long[] array, final int fromIndex, final int toIndex, final LongComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);
        final var nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            new LongTimSort(array, comparator)
                    .sort(fromIndex, toIndex, nRemaining);
        }
    }

    @Override
    protected void updateArrayForBinarySort(final long[] array, final int left, final int pivotIndex, final int difStartLeft) {
        final var pivot = array[pivotIndex];
        updateArrayForBinarySort(array, left, difStartLeft);
        array[left] = pivot;
    }

    @Override
    protected void swap(final long[] array, final int index1, final int index2) {
        final var temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    @Override
    protected void setArrayValue(final long[] sourceArray, final int sourceIndex, final long[] destArray, final int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @Override
    protected int arrayLength(final long[] array) {
        return array.length;
    }

    @Override
    protected long[] newArray(final int length) {
        return new long[length];
    }

    protected int compare(final LongComparator comparator, final long[] array1, final int index1, final long[] array2, final int index2) {
        return comparator.compare(array1[index1], array2[index2]);
    }
}
