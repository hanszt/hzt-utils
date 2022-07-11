package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting int arrays where an IntComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class IntTimSort extends PrimitiveTimSort<int[], IntComparator> {

    @SuppressWarnings("squid:S2384")
    private IntTimSort(int[] intArray, IntComparator intComparator) {
        super(intArray.length, intArray, new int[getInitTempLength(intArray.length)], intComparator);
    }

    static void sort(int @NotNull [] array, int fromIndex, int toIndex, @NotNull IntComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);

        final int nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            if (nRemaining < MIN_MERGE) {
                final int initRunLen = PrimitiveArrays.countRunAndMakeAscending(array, fromIndex, toIndex, comparator);
                PrimitiveArrays.binarySort(array, fromIndex, toIndex, fromIndex + initRunLen, comparator);
            } else {
                IntTimSort timSort = new IntTimSort(array, comparator);
                final int minRun = minRunLength(nRemaining);

                final int loIndex = timSort.getLo(array, fromIndex, toIndex, comparator, nRemaining, minRun);

                PreConditions.require(loIndex == toIndex);

                timSort.mergeForceCollapse();

                PreConditions.require(timSort.stackSize == 1);
            }
        }
    }

    protected int getRunLen(int[] array, int lo, int hi, IntComparator comparator, int nRemaining, int minRun) {
        int runLen = PrimitiveArrays.countRunAndMakeAscending(array, lo, hi, comparator);
        if (runLen < minRun) {
            final int force = Math.min(nRemaining, minRun);
            PrimitiveArrays.binarySort(array, lo, lo + force, lo + runLen, comparator);
            runLen = force;
        }
        return runLen;
    }

    @Override
    void setInDestinationArray(int[] sourceArray, int sourceIndex, int[] destArray, int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @SuppressWarnings("squid:S2384")
    int[] ensureCapacity(int minCapacity) {
        if (tempArray.length < minCapacity) {
            tempArray = new int[calculateNewLength(minCapacity, array.length)];
        }
        return tempArray;
    }

    int compare(IntComparator comparator, int[] array1, int index1, int[] array2, int index2) {
        return comparator.compareInt(array1[index1], array2[index2]);
    }
}
