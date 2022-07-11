package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting long arrays where a LongComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class LongTimSort extends PrimitiveTimSort<long[], LongComparator> {

    @SuppressWarnings("squid:S2384")
    private LongTimSort(long[] longArray, LongComparator longComparator) {
        super(longArray.length, longArray, new long[getInitTempLength(longArray.length)], longComparator);
    }

    static void sort(long @NotNull [] array, int fromIndex, int toIndex, @NotNull LongComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);

        final int nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            if (nRemaining < MIN_MERGE) {
                final int initRunLen = PrimitiveArrays.countRunAndMakeAscending(array, fromIndex, toIndex, comparator);
                PrimitiveArrays.binarySort(array, fromIndex, toIndex, fromIndex + initRunLen, comparator);
            } else {
                LongTimSort timSort = new LongTimSort(array, comparator);
                final int minRun = minRunLength(nRemaining);

                final int loIndex = timSort.getLo(array, fromIndex, toIndex, comparator, nRemaining, minRun);

                PreConditions.require(loIndex == toIndex);

                timSort.mergeForceCollapse();

                PreConditions.require(timSort.stackSize == 1);
            }
        }
    }

    protected int getRunLen(long[] array, int lo, int hi, LongComparator comparator, int nRemaining, int minRun) {
        int runLen = PrimitiveArrays.countRunAndMakeAscending(array, lo, hi, comparator);
        if (runLen < minRun) {
            final int force = Math.min(nRemaining, minRun);
            PrimitiveArrays.binarySort(array, lo, lo + force, lo + runLen, comparator);
            runLen = force;
        }
        return runLen;
    }

    @Override
    void setInDestinationArray(long[] sourceArray, int sourceIndex, long[] destArray, int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @SuppressWarnings("squid:S2384")
    long[] ensureCapacity(int minCapacity) {
        if (tempArray.length < minCapacity) {
            tempArray = new long[calculateNewLength(minCapacity, array.length)];
        }
        return tempArray;
    }

    int compare(LongComparator comparator, long[] array1, int index1, long[] array2, int index2) {
        return comparator.compareLong(array1[index1], array2[index2]);
    }
}
