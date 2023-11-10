package org.hzt.utils.arrays;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.LongComparator;

/**
 * A Tim sort implementation for sorting long arrays where a LongComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class LongTimSort extends PrimitiveTimSort<long[], LongComparator> {

    private LongTimSort(long[] longArray, LongComparator longComparator) {
        super(longArray.length, longArray, new long[getInitTempLength(longArray.length)], longComparator);
    }

    static void sort(long[] array, int fromIndex, int toIndex, LongComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);
        final int nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            new LongTimSort(array, comparator)
                    .sort(fromIndex, toIndex, nRemaining);
        }
    }

    @Override
    protected void updateArrayForBinarySort(long[] array, int left, int pivotIndex, int difStartLeft) {
        final long pivot = array[pivotIndex];
        updateArrayForBinarySort(array, left, difStartLeft);
        array[left] = pivot;
    }

    @Override
    protected void swap(long[] array, int index1, int index2) {
        long temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    @Override
    protected void setArrayValue(long[] sourceArray, int sourceIndex, long[] destArray, int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @Override
    protected int arrayLength(long[] array) {
        return array.length;
    }

    @Override
    protected long[] newArray(int length) {
        return new long[length];
    }

    protected int compare(LongComparator comparator, long[] array1, int index1, long[] array2, int index2) {
        return comparator.compare(array1[index1], array2[index2]);
    }
}
