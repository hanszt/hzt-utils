package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;

class PrimitiveTimSort {

    static final int MIN_MERGE = 32;
    static final int MIN_GALLOP = 7;
    static final int INITIAL_TMP_STORAGE_LENGTH = 256;

    int minGallop = MIN_GALLOP;
    int tempBase = 0;
    int stackSize = 0;
    int tempLength;

    PrimitiveTimSort(int length) {
        this.tempLength = length < 512 ? (length >>> 1) : INITIAL_TMP_STORAGE_LENGTH;
    }

    @SuppressWarnings("squid:S3358")
    static int getStackLength(int len) {
        return (len < 120) ? 5 :
                (len < 1_542 ? 10 :
                        ((len < 119_151) ? 24 : 49));
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

    static int calculateNewLength(int minCapacity, int length) {
        int newLength = minCapacity | (minCapacity >> 1);
        newLength |= newLength >> 2;
        newLength |= newLength >> 4;
        newLength |= newLength >> 8;
        newLength |= newLength >> 16;
        ++newLength;
        return newLength < 0 ? minCapacity : Math.min(newLength, length >>> 1);
    }
}
