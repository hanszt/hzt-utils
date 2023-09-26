package org.hzt.utils.arrays;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting int arrays where an IntComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class IntTimSort extends PrimitiveTimSort<int[], IntComparator> {

    private IntTimSort(final int[] intArray, final IntComparator intComparator) {
        super(intArray.length, intArray, new int[getInitTempLength(intArray.length)], intComparator);
    }

    static void sort(final int @NotNull [] array, final int fromIndex, final int toIndex, @NotNull final IntComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);
        final var nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            new IntTimSort(array, comparator)
                    .sort(fromIndex, toIndex, nRemaining);
        }
    }

    @Override
    protected void updateArrayForBinarySort(final int[] array, final int left, final int pivotIndex, final int difStartLeft) {
        final var pivot = array[pivotIndex];
        updateArrayForBinarySort(array, left, difStartLeft);
        array[left] = pivot;
    }

    @Override
    protected void swap(final int[] array, final int index1, final int index2) {
        final var temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    @Override
    protected void setArrayValue(final int[] sourceArray, final int sourceIndex, final int[] destArray, final int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @Override
    protected int arrayLength(final int[] array) {
        return array.length;
    }

    @Override
    protected int[] newArray(final int length) {
        return new int[length];
    }

    protected int compare(final IntComparator comparator, final int[] array1, final int index1, final int[] array2, final int index2) {
        return comparator.compare(array1[index1], array2[index2]);
    }
}
