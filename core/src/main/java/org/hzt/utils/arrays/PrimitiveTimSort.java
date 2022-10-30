package org.hzt.utils.arrays;

import org.hzt.utils.PreConditions;

/**
 * A Tim sort implementation for sorting primitive arrays where a primitive comparator is used for the comparisons.
 * Based on the generic java implementation of TimSort
 *
 * @see java.util.TimSort;
 *
 * @param <A> the primitive array type
 * @param <C> the primitive comparator type
 */
@SuppressWarnings("JavadocReference")
public abstract class PrimitiveTimSort<A, C> {

    private static final int MIN_MERGE = 32;
    private static final int MIN_GALLOP = 7;
    private static final int INITIAL_TMP_STORAGE_LENGTH = 256;

    private final int[] runBase;
    private final int[] runLength;

    private final A array;
    private final C comparator;
    private A tempArray;
    private int minGallop = MIN_GALLOP;
    private int stackSize = 0;

    protected PrimitiveTimSort(final int length, final A array, final A tempArray, final C comparator) {
        final var stackLen = getStackLength(length);
        this.array = array;
        this.tempArray = tempArray;
        this.comparator = comparator;
        this.runBase = new int[stackLen];
        this.runLength = new int[stackLen];
    }

    protected void sort(final int fromIndex, final int toIndex, final int nRemaining) {
        if (nRemaining < MIN_MERGE) {
            final var initRunLen = countRunAndMakeAscending(array, fromIndex, toIndex, comparator);
            binarySort(array, fromIndex, toIndex, fromIndex + initRunLen, comparator);
        } else {
            final var minRun = minRunLength(nRemaining);
            final var loIndex = getLo(array, fromIndex, toIndex, comparator, nRemaining, minRun);

            PreConditions.require(loIndex == toIndex);
            mergeForceCollapse();
            PreConditions.require(stackSize == 1);
        }
    }

    protected static int getInitTempLength(final int length) {
        return length < 512 ? (length >>> 1) : INITIAL_TMP_STORAGE_LENGTH;
    }

    @SuppressWarnings("squid:S3358")
    private static int getStackLength(final int len) {
        return (len < 120) ? 5 :
                (len < 1_542 ? 10 :
                        ((len < 119_151) ? 24 : 49));
    }

    private int getLo(final A array, final int initLo, final int hi, final C comparator,
                      final int initNRemaining, final int minRun) {
        var nRemaining = initNRemaining;
        var lo = initLo;
        do {
            final var runLen = getRunLen(array, lo, hi, comparator, nRemaining, minRun);
            pushRun(lo, runLen);
            mergeCollapse();
            lo += runLen;
            nRemaining -= runLen;
        } while (nRemaining != 0);
        return lo;
    }

    private void binarySort(final A array, final int lo, final int hi, final int initStart, final C comparator) {
        PreConditions.require(lo <= initStart && initStart <= hi);
        var start = initStart;
        if (start == lo) {
            ++start;
        }
        while (start < hi) {
            PreConditions.require(lo <= start);
            var left = lo;
            var right = start;

            final var pivotIndex = start;
            while (left < right) {
                final var mid = (left + right) >>> 1;
                if (compare(comparator, array, pivotIndex, mid) < 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            PreConditions.require(left == right);
            final var difStartLeft = start - left;
            updateArrayForBinarySort(array, left, pivotIndex, difStartLeft);
            ++start;
        }
    }

    protected abstract void updateArrayForBinarySort(final A array, int left, int pivotIndex, int difStartLeft);

    protected void updateArrayForBinarySort(final A array, final int left, final int difStartLeft) {
        if (difStartLeft == 2) {
            setArrayValue(array, left + 1, left + 2);
            setArrayValue(array, left, left + 1);
        } else if (difStartLeft == 1) {
            setArrayValue(array, left, left + 1);
        } else {
            //noinspection SuspiciousSystemArraycopy
            System.arraycopy(array, left, array, left + 1, difStartLeft);
        }
    }

    protected int getRunLen(final A array, final int lo, final int hi, final C comparator,
                            final int nRemaining, final int minRun) {
        var runLen = countRunAndMakeAscending(array, lo, hi, comparator);
        if (runLen < minRun) {
            final var force = Math.min(nRemaining, minRun);
            binarySort(array, lo, lo + force, lo + runLen, comparator);
            runLen = force;
        }
        return runLen;
    }

    private int countRunAndMakeAscending(final A array, final int lo, final int hi, final C comparator) {
        PreConditions.require(lo < hi);
        var runHi = lo + 1;
        if (runHi == hi) {
            return 1;
        }
        if (compare(comparator, array, runHi++, lo) >= 0) {
            while (runHi < hi && compare(comparator, array, runHi, runHi - 1) >= 0) {
                ++runHi;
            }
        } else {
            while (runHi < hi && compare(comparator, array, runHi, runHi - 1) < 0) {
                ++runHi;
            }
            reverseRange(array, lo, runHi);
        }
        return runHi - lo;
    }

    private void reverseRange(final A array, final int initLo, final int initHi) {
        var hi = initHi - 1;
        var lo = initLo;
        while (lo < hi) {
            swap(array, lo++, hi--);
        }
    }

    private void mergeCollapse() {
        while (stackSize > 1) {
            var n = stackSize - 2;
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

    private void mergeForceCollapse() {
        while (stackSize > 1) {
            var n = stackSize - 2;
            if (n > 0 && runLength[n - 1] < runLength[n + 1]) {
                --n;
            }
            mergeAt(n);
        }
    }

    private void mergeAt(final int i) {
        PreConditions.require(stackSize >= 2);
        PreConditions.require(i >= 0);
        PreConditions.require(i == stackSize - 2 || i == stackSize - 3);

        var base1 = runBase[i];
        var len1 = runLength[i];
        final var base2 = runBase[i + 1];
        final var len2 = runLength[i + 1];

        PreConditions.require(len1 > 0 && len2 > 0);
        PreConditions.require(base1 + len1 == base2);

        runLength[i] = len1 + len2;
        if (i == stackSize - 3) {
            runBase[i + 1] = runBase[i + 2];
            runLength[i + 1] = runLength[i + 2];
        }

        --stackSize;
        final var ofsRight = gallopRight(array, base2, array, base1, len1, 0, comparator);

        PreConditions.require(ofsRight >= 0);

        base1 += ofsRight;
        len1 -= ofsRight;
        if (len1 == 0) {
            return;
        }
        final var ofsLeft = gallopLeft(array, base1 + len1 - 1, array, base2, len2, len2 - 1, comparator);

        merge(base1, len1, base2, ofsLeft);
    }

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important

    @SuppressWarnings({"SuspiciousSystemArraycopy", "squid:S134", "squid:S135", "squid:S138", "squid:S1119", "squid:S1541", "squid:S3776"})
    private void mergeLo(final int base1, final int initLen1, final int base2, final int initLen2) {
        PreConditions.require(initLen1 > 0 && initLen2 > 0 && base1 + initLen1 == base2);
        // Copy first run into temp array
        final var localArray = this.array; // For performance
        final var tmpArray = ensureCapacity(initLen1);
        var cursor1 = 0; // Indexes into tmp array
        var cursor2 = base2;   // Indexes int a
        var dest = base1;      // Indexes int a
        System.arraycopy(localArray, base1, tmpArray, cursor1, initLen1);
        // Move first element of second run and deal with degenerate cases
        setArrayValue(localArray, cursor2++, dest++);
        var len2 = initLen2;
        if (--len2 == 0) {
            System.arraycopy(tmpArray, cursor1, localArray, dest, initLen1);
            return;
        }
        if (initLen1 == 1) {
            System.arraycopy(localArray, cursor2, localArray, dest, len2);
            setArrayValue(localArray, cursor1, dest + len2);
            return;
        }
        final var localComparator = this.comparator;  // Use local variable for performance
        var localMinGallop = this.minGallop;    //  "    "       "     "      "
        var len1 = initLen1;
        outer:
        while (true) {
            var count1 = 0; // Number of times in a row that first run won
            var count2 = 0; // Number of times in a row that second run won
            // Do the straightforward thing until (if ever) one run starts winning consistently.
            do {
                PreConditions.require(len1 > 1 && len2 > 0);
                if (compare(localComparator, localArray, cursor2, tmpArray, cursor1) < 0) {
                    setArrayValue(localArray, cursor2++, dest++);
                    count2++;
                    count1 = 0;
                    if (--len2 == 0) {
                        break outer;
                    }
                } else {
                    setArrayValue(tmpArray, cursor1++, localArray, dest++);
                    count1++;
                    count2 = 0;
                    if (--len1 == 1) {
                        break outer;
                    }
                }
            } while ((count1 | count2) < localMinGallop);
            /*
             * One run is winning so consistently that galloping may be a
             * huge win. So try that, and continue galloping until (if ever)
             * neither run appears to be winning consistently anymore.
             */
            do {
                PreConditions.require(len1 > 1 && len2 > 0);
                count1 = gallopRight(localArray, cursor2, tmpArray, cursor1, len1, 0, localComparator);
                if (count1 != 0) {
                    System.arraycopy(tmpArray, cursor1, localArray, dest, count1);
                    dest += count1;
                    cursor1 += count1;
                    len1 -= count1;
                    if (len1 <= 1) {
                        break outer;
                    }
                }
                setArrayValue(localArray, cursor2++, dest++);
                if (--len2 == 0) {
                    break outer;
                }

                count2 = gallopLeft(tmpArray, cursor1, localArray, cursor2, len2, 0, localComparator);
                if (count2 != 0) {
                    System.arraycopy(localArray, cursor2, localArray, dest, count2);
                    dest += count2;
                    cursor2 += count2;
                    len2 -= count2;
                    if (len2 == 0) {
                        break outer;
                    }
                }
                setArrayValue(tmpArray, cursor1++, localArray, dest++);
                if (--len1 == 1) {
                    break outer;
                }
                localMinGallop--;
            } while (count1 >= MIN_GALLOP || count2 >= MIN_GALLOP);
            localMinGallop = Math.max(localMinGallop, 0);
            localMinGallop += 2;  // Penalize for leaving gallop mode
        }  // End of "outer" loop
        this.minGallop = Math.max(localMinGallop, 1);  // Write back to field

        if (len1 == 1) {
            PreConditions.require(len2 > 0);
            System.arraycopy(localArray, cursor2, localArray, dest, len2);
            //  Last elt of run 1 to end of merge
            setArrayValue(tmpArray, cursor1, localArray, dest + len2);
        } else if (len1 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        } else {
            PreConditions.require(len2 == 0);
            System.arraycopy(tmpArray, cursor1, localArray, dest, len1);
        }
    }
    @SuppressWarnings({"SuspiciousSystemArraycopy", "squid:S134", "squid:S135", "squid:S138", "squid:S1119", "squid:S1541", "squid:S3776"})
    private void mergeHi(final int base1, final int initLen1, final int base2, final int initLen2) {
        PreConditions.require(initLen1 > 0 && initLen2 > 0 && base1 + initLen1 == base2);
        // Copy second run into temp array
        final var localArray = this.array; // For performance
        final var tmpArray = ensureCapacity(initLen2);
        System.arraycopy(localArray, base2, tmpArray, 0, initLen2);

        var cursor1 = base1 + initLen1 - 1;  // Indexes into a
        var dest = base2 + initLen2 - 1;     // Indexes into a
        // Move last element of first run and deal with degenerate cases
        setArrayValue(localArray, cursor1--, dest--);
        var len1 = initLen1;
        if (--len1 == 0) {
            System.arraycopy(tmpArray, 0, localArray, dest - (initLen2 - 1), initLen2);
            return;
        }
        var cursor2 = initLen2 - 1; // Indexes into tmp array
        if (initLen2 == 1) {
            dest -= len1;
            cursor1 -= len1;
            System.arraycopy(localArray, cursor1 + 1, localArray, dest + 1, len1);
            setArrayValue(tmpArray, cursor2, localArray, dest);
            return;
        }
        final var localComparator = this.comparator;  // Use local variable for performance
        var localMinGallop = this.minGallop;    //  "    "       "     "      "
        var len2 = initLen2;
        outer:
        while (true) {
            var count1 = 0; // Number of times in a row that first run won
            var count2 = 0; // Number of times in a row that second run won
            /*
             * Do the straightforward thing until (if ever) one run
             * appears to win consistently.
             */
            do {
                PreConditions.require(len1 > 0 && len2 > 1);
                if (compare(localComparator, tmpArray, cursor2, localArray, cursor1) < 0) {
                    setArrayValue(localArray, cursor1--, dest--);
                    count1++;
                    count2 = 0;
                    if (--len1 == 0) {
                        break outer;
                    }
                } else {
                    setArrayValue(tmpArray, cursor2, localArray, dest);
                    dest--;
                    cursor2--;
                    count2++;
                    count1 = 0;
                    if (--len2 == 1) {
                        break outer;
                    }
                }
            } while ((count1 | count2) < localMinGallop);

            /*
             * One run is winning so consistently that galloping may be a
             * huge win. So try that, and continue galloping until (if ever)
             * neither run appears to be winning consistently anymore.
             */
            do {
                PreConditions.require(len1 > 0 && len2 > 1);
                count1 = len1 - gallopRight(tmpArray, cursor2, localArray, base1, len1, len1 - 1, localComparator);
                if (count1 != 0) {
                    dest -= count1;
                    cursor1 -= count1;
                    len1 -= count1;
                    System.arraycopy(localArray, cursor1 + 1, localArray, dest + 1, count1);
                    if (len1 == 0) {
                        break outer;
                    }
                }
                setArrayValue(tmpArray, cursor2--, localArray, dest--);
                if (--len2 == 1) {
                    break outer;
                }
                count2 = len2 - gallopLeft(localArray, cursor1, tmpArray, 0, len2, len2 - 1, localComparator);
                if (count2 != 0) {
                    dest -= count2;
                    cursor2 -= count2;
                    len2 -= count2;
                    System.arraycopy(tmpArray, cursor2 + 1, localArray, dest + 1, count2);
                    if (len2 <= 1) {
                        break outer;
                    }
                }
                setArrayValue(localArray, cursor1--, dest--);
                if (--len1 == 0) {
                    break outer;
                }
                localMinGallop--;
            } while (count1 >= MIN_GALLOP || count2 >= MIN_GALLOP);
            localMinGallop = Math.max(localMinGallop, 0);
            localMinGallop += 2;  // Penalize for leaving gallop mode
        }  // End of "outer" loop
        this.minGallop = Math.max(localMinGallop, 1);  // Write back to field

        if (len2 == 1) {
            PreConditions.require(len1 > 0);
            dest -= len1;
            cursor1 -= len1;
            System.arraycopy(localArray, cursor1 + 1, localArray, dest + 1, len1);
            // Move first elt of run2 to front of merge
            setArrayValue(tmpArray, cursor2, localArray, dest);
        } else if (len2 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        } else {
            PreConditions.require(len1 == 0);
            System.arraycopy(tmpArray, 0, localArray, dest - (len2 - 1), len2);
        }
    }

    private void setArrayValue(final A array, final int sourceIndex, final int destIndex) {
        setArrayValue(array, sourceIndex, array, destIndex);
    }

    protected abstract void swap(A array, int index1, int index2);
    protected abstract void setArrayValue(A sourceArray, int sourceIndex, A destArray, int destIndex);

    private int compare(final C comparator, final A array, final int index1, final int index2) {
        return compare(comparator, array, index1, array, index2);
    }

    protected abstract int compare(C comparator, A array1, int index1, A array2, int index2);
    protected abstract int arrayLength(A array);
    protected abstract A newArray(int length);

    private A ensureCapacity(final int minCapacity) {
        if (arrayLength(tempArray) < minCapacity) {
            tempArray = newArray(calculateNewLength(minCapacity, arrayLength(array)));
        }
        return tempArray;
    }

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important
    @SuppressWarnings({"squid:S1541", "squid:S3776"})
    private int gallopRight(final A array1, final int cursor, final A array2,
                            final int base, final int len, final int hint, final C comparator) {
        PreConditions.require(hint >= 0 && hint < len);
        var ofs = 1;
        var lastOfs = 0;
        if (compare(comparator, array1, cursor, array2, base + hint) < 0) {
            final var maxOfs = hint + 1;

            while (ofs < maxOfs && compare(comparator, array1, cursor, array2, base + hint - ofs) < 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            final var tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        } else {
            final var maxOfs = len - hint;

            while (ofs < maxOfs && compare(comparator, array1, cursor, array2, base + hint + ofs) >= 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            lastOfs += hint;
            ofs += hint;
        }
        PreConditions.require(-1 <= lastOfs && lastOfs < ofs && ofs <= len);

        ++lastOfs;

        while (lastOfs < ofs) {
            final var maxOfs = lastOfs + ((ofs - lastOfs) >>> 1);
            if (compare(comparator, array1, cursor, array2, base + maxOfs) < 0) {
                ofs = maxOfs;
            } else {
                lastOfs = maxOfs + 1;
            }
        }
        PreConditions.require(lastOfs == ofs);
        return ofs;
    }

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important
    @SuppressWarnings({"squid:S1541", "squid:S3776"})
    private int gallopLeft(final A array1, final int cursor, final A array2,
                           final int base, final int len, final int hint, final C comparator) {
        PreConditions.require(hint >= 0 && hint < len);
        var lastOfs = 0;
        var ofs = 1;
        if (compare(comparator, array1, cursor, array2, base + hint) > 0) {
            final var maxOfs = len - hint;

            while (ofs < maxOfs && compare(comparator, array1, cursor, array2, base + hint + ofs) > 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            lastOfs += hint;
            ofs += hint;
        } else {
            final var maxOfs = hint + 1;

            while (ofs < maxOfs && compare(comparator, array1, cursor, array2, base + hint - ofs) <= 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            final var tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        }
        PreConditions.require(-1 <= lastOfs && lastOfs < ofs && ofs <= len);

        ++lastOfs;

        while (lastOfs < ofs) {
            final var maxOfs = lastOfs + ((ofs - lastOfs) >>> 1);
            if (compare(comparator, array1, cursor, array2, base + maxOfs) > 0) {
                lastOfs = maxOfs + 1;
            } else {
                ofs = maxOfs;
            }
        }
        PreConditions.require(lastOfs == ofs);
        return ofs;
    }

    private void pushRun(final int runBase, final int runLen) {
        this.runBase[stackSize] = runBase;
        this.runLength[stackSize] = runLen;
        ++stackSize;
    }

    private static int minRunLength(final int initN) {
        PreConditions.require(initN >= 0);
        var r = 0;
        var n = initN;
        while (n >= MIN_MERGE) {
            r |= n & 1;
            n >>= 1;
        }
        return n + r;
    }

    private void merge(final int base1, final int len1, final int base2, final int len2) {
        PreConditions.require(len2 >= 0);
        if (len2 != 0) {
            if (len1 <= len2) {
                mergeLo(base1, len1, base2, len2);
            } else {
                mergeHi(base1, len1, base2, len2);
            }
        }
    }

    private static int calculateNewLength(final int minCapacity, final int length) {
        var newLength = minCapacity | (minCapacity >> 1);
        newLength |= newLength >> 2;
        newLength |= newLength >> 4;
        newLength |= newLength >> 8;
        newLength |= newLength >> 16;
        ++newLength;
        return newLength < 0 ? minCapacity : Math.min(newLength, length >>> 1);
    }
}
