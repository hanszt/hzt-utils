package org.hzt.utils.arrays;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting double arrays where a DoubleComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class DoubleTimSort extends PrimitiveTimSort<double[], DoubleComparator> {

    private DoubleTimSort(final double[] doubleArray, final DoubleComparator doubleComparator) {
        super(doubleArray.length, doubleArray, new double[getInitTempLength(doubleArray.length)], doubleComparator);
    }

    static void sort(final double @NotNull [] array, final int fromIndex, final int toIndex, @NotNull final DoubleComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);
        final var nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            new DoubleTimSort(array, comparator)
                    .sort(fromIndex, toIndex, nRemaining);
        }
    }

    @Override
    protected void updateArrayForBinarySort(final double[] array, final int left, final int pivotIndex, final int difStartLeft) {
        final var pivot = array[pivotIndex];
        updateArrayForBinarySort(array, left, difStartLeft);
        array[left] = pivot;
    }

    @Override
    protected void swap(final double[] array, final int index1, final int index2) {
        final var temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    @Override
    protected void setArrayValue(final double[] sourceArray, final int sourceIndex, final double[] destArray, final int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @Override
    protected int arrayLength(final double[] array) {
        return array.length;
    }

    @Override
    protected double[] newArray(final int length) {
        return new double[length];
    }

    protected int compare(final DoubleComparator comparator, final double[] array1, final int index1, final double[] array2, final int index2) {
        return comparator.compare(array1[index1], array2[index2]);
    }
}
