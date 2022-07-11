package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting double arrays where a DoubleComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 */
public final class DoubleTimSort extends PrimitiveTimSort<double[], DoubleComparator> {

    @SuppressWarnings("squid:S2384")
    private DoubleTimSort(double[] doubleArray, DoubleComparator doubleComparator) {
        super(doubleArray.length, doubleArray, new double[getInitTempLength(doubleArray.length)], doubleComparator);
    }

    static void sort(double @NotNull [] array, int fromIndex, int toIndex, @NotNull DoubleComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);

        final int nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            if (nRemaining < MIN_MERGE) {
                final int initRunLen = PrimitiveArrays.countRunAndMakeAscending(array, fromIndex, toIndex, comparator);
                PrimitiveArrays.binarySort(array, fromIndex, toIndex, fromIndex + initRunLen, comparator);
            } else {
                DoubleTimSort timSort = new DoubleTimSort(array, comparator);
                final int minRun = minRunLength(nRemaining);

                final int loIndex = timSort.getLo(array, fromIndex, toIndex, comparator, nRemaining, minRun);

                PreConditions.require(loIndex == toIndex);

                timSort.mergeForceCollapse();

                PreConditions.require(timSort.stackSize == 1);
            }
        }
    }

    protected int getRunLen(double[] array, int lo, int hi, DoubleComparator comparator, int nRemaining, int minRun) {
        int runLen = PrimitiveArrays.countRunAndMakeAscending(array, lo, hi, comparator);
        if (runLen < minRun) {
            final int force = Math.min(nRemaining, minRun);
            PrimitiveArrays.binarySort(array, lo, lo + force, lo + runLen, comparator);
            runLen = force;
        }
        return runLen;
    }

    @Override
    void setInDestinationArray(double[] sourceArray, int sourceIndex, double[] destArray, int destIndex) {
        destArray[destIndex] = sourceArray[sourceIndex];
    }

    @SuppressWarnings("squid:S2384")
    double[] ensureCapacity(int minCapacity) {
        if (tempArray.length < minCapacity) {
            tempArray = new double[calculateNewLength(minCapacity, array.length)];
        }
        return tempArray;
    }

    int compare(DoubleComparator comparator, double[] array1, int index1, double[] array2, int index2) {
        return comparator.compareDouble(array1[index1], array2[index2]);
    }
}
