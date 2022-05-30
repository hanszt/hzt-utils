package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;

class PrimitiveTimSort {

    static final int MIN_MERGE = 32;
    static final int MIN_GALLOP = 7;
    static final int INITIAL_TMP_STORAGE_LENGTH = 256;

    int minGallop = MIN_GALLOP;
    int tmpBase;
    int tmpLen;
    int stackSize = 0;

    PrimitiveTimSort(int length) {
        this.tmpBase = 0;
        this.tmpLen = length < 512 ? (length >>> 1) : INITIAL_TMP_STORAGE_LENGTH;
    }

    static int getStackLength(int len) {
        final var i1 = (len < 119_151) ? 24 : 49;
        final var i = len < 1_542 ? 10 : i1;
        return (len < 120) ? 5 : i;
    }

    static int minRunLength(int n) {
        PreConditions.require(n >= 0);
        int r = 0;
        while (n >= MIN_MERGE) {
            r |= n & 1;
            n >>= 1;
        }
        return n + r;
    }

    static int calculateNewSize(int minCapacity, int length) {
        int newSize = minCapacity | (minCapacity >> 1);
        newSize |= newSize >> 2;
        newSize |= newSize >> 4;
        newSize |= newSize >> 8;
        newSize |= newSize >> 16;
        ++newSize;
        return newSize < 0 ? minCapacity : Math.min(newSize, length >>> 1);
    }
}
