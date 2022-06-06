package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;

/**
 * @param <A> the primitive array type
 * @param <C> the primitive comparator type
 */
public abstract class PrimitiveTimSort<A, C> {

    static final int MIN_MERGE = 32;
    static final int MIN_GALLOP = 7;
    static final int INITIAL_TMP_STORAGE_LENGTH = 256;

    final int[] runBase;
    final int[] runLength;
    int minGallop = MIN_GALLOP;
    int stackSize = 0;

    protected PrimitiveTimSort(int length) {
        int stackLen = getStackLength(length);
        this.runBase = new int[stackLen];
        this.runLength = new int[stackLen];
    }

    protected static int getInitTempLength(int length) {
        return length < 512 ? (length >>> 1) : INITIAL_TMP_STORAGE_LENGTH;
    }

    @SuppressWarnings("squid:S3358")
    protected static int getStackLength(int len) {
        return (len < 120) ? 5 :
                (len < 1_542 ? 10 :
                        ((len < 119_151) ? 24 : 49));
    }

    protected int getLo(A array, int lo, int hi, C comparator, int nRemaining, int minRun) {
        do {
            int runLen = getRunLen(array, lo, hi, comparator, nRemaining, minRun);

            pushRun(lo, runLen);
            mergeCollapse();
            lo += runLen;
            nRemaining -= runLen;
        } while (nRemaining != 0);
        return lo;
    }

    protected abstract int getRunLen(A array, int lo, int hi, C comparator, int nRemaining, int minRun);

    private void mergeCollapse() {
        while (stackSize > 1) {
            int n = stackSize - 2;
            if (runLength[n] > runLength[n + 1]) {
                return; // Invariant is established
            }
            final var b1 = n > 0 && runLength[n - 1] <= runLength[n] + runLength[n + 1];
            final var b2 = n > 1 && runLength[n - 2] <= runLength[n - 1] + runLength[n];
            if ((b1 || b2)) {
                final var b3 = runLength[n - 1] < runLength[n + 1];
                if (b3) {
                    n--;
                }
            }
            mergeAt(n);
        }
    }

    void mergeForceCollapse() {
        while (stackSize > 1) {
            int n = stackSize - 2;
            if (n > 0 && runLength[n - 1] < runLength[n + 1]) {
                --n;
            }
            mergeAt(n);
        }
    }

    protected abstract void mergeAt(int n);

    private void pushRun(int runBase, int runLen) {
        this.runBase[stackSize] = runBase;
        this.runLength[stackSize] = runLen;
        ++stackSize;
    }

    protected static int minRunLength(int n) {
        PreConditions.require(n >= 0);
        int r = 0;
        while (n >= MIN_MERGE) {
            r |= n & 1;
            n >>= 1;
        }
        return n + r;
    }

    protected void decideMergeStrategy(int base1, int len1, int base2, int len2) {
        PreConditions.require(len2 >= 0);
        if (len2 != 0) {
            if (len1 <= len2) {
                mergeLo(base1, len1, base2, len2);
            } else {
                mergeHi(base1, len1, base2, len2);
            }
        }
    }

    protected abstract void mergeLo(int base1, int len1, int base2, int len2);

    protected abstract void mergeHi(int base1, int len1, int base2, int len2);

    protected static int calculateNewLength(int minCapacity, int length) {
        int newLength = minCapacity | (minCapacity >> 1);
        newLength |= newLength >> 2;
        newLength |= newLength >> 4;
        newLength |= newLength >> 8;
        newLength |= newLength >> 16;
        ++newLength;
        return newLength < 0 ? minCapacity : Math.min(newLength, length >>> 1);
    }
}
