package org.hzt.utils.arrays;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting int arrays where an IntComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class IntTimSort extends PrimitiveTimSort<int[], IntComparator> {

    private IntTimSort(int[] intArray, IntComparator intComparator) {
        super(intArray.length, intArray, new int[getInitTempLength(intArray.length)], intComparator);
    }

    static void sort(int @NotNull [] array, int fromIndex, int toIndex, @NotNull IntComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);
        final int nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            new IntTimSort(array, comparator)
                    .sort(fromIndex, toIndex, nRemaining);
        }
    }

    @Override
    protected void updateArrayForBinarySort(int[] array, int left, int pivotIndex, int difStartLeft) {
        final int pivot = array[pivotIndex];
        updateArrayForBinarySort(array, left, difStartLeft);
        array[left] = pivot;
    }

    @Override
    protected void swap(int[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    @Override
    protected void setArrayValue(int[] sourceArray, int sourceIndex, int[] destArray, int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @Override
    protected int arrayLength(int[] array) {
        return array.length;
    }

    @Override
    protected int[] newArray(int length) {
        return new int[length];
    }

    protected int compare(IntComparator comparator, int[] array1, int index1, int[] array2, int index2) {
        return comparator.compare(array1[index1], array2[index2]);
    }
}
