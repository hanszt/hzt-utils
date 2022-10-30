package org.hzt.utils.arrays;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting double arrays where a DoubleComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class DoubleTimSort extends PrimitiveTimSort<double[], DoubleComparator> {

    private DoubleTimSort(double[] doubleArray, DoubleComparator doubleComparator) {
        super(doubleArray.length, doubleArray, new double[getInitTempLength(doubleArray.length)], doubleComparator);
    }

    static void sort(double @NotNull [] array, int fromIndex, int toIndex, @NotNull DoubleComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);
        final var nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            new DoubleTimSort(array, comparator)
                    .sort(fromIndex, toIndex, nRemaining);
        }
    }

    @Override
    protected void updateArrayForBinarySort(double[] array, int left, int pivotIndex, int difStartLeft) {
        final var pivot = array[pivotIndex];
        updateArrayForBinarySort(array, left, difStartLeft);
        array[left] = pivot;
    }

    @Override
    protected void swap(double[] array, int index1, int index2) {
        var temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    @Override
    protected void setArrayValue(double[] sourceArray, int sourceIndex, double[] destArray, int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @Override
    protected int arrayLength(double[] array) {
        return array.length;
    }

    @Override
    protected double[] newArray(int length) {
        return new double[length];
    }

    protected int compare(DoubleComparator comparator, double[] array1, int index1, double[] array2, int index2) {
        return comparator.compare(array1[index1], array2[index2]);
    }
}
