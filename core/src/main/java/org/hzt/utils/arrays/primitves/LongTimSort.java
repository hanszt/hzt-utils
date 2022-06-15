package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting long arrays where a LongComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 *
 * @see java.util.TimSort;
 */
@SuppressWarnings({"DuplicatedCode", "JavadocReference"})
public final class LongTimSort extends PrimitiveTimSort<long[], LongComparator> {

    private final LongComparator longComparator;
    private final long[] longArray;
    private long[] tempArray;

    @SuppressWarnings("squid:S2384")
    private LongTimSort(long[] longArray, LongComparator longComparator) {
        super(longArray.length);
        this.longArray = longArray;
        this.longComparator = longComparator;
        this.tempArray = new long[getInitTempLength(longArray.length)];
    }

    static void sort(long @NotNull [] array, int fromIndex, int toIndex, @NotNull LongComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);

        final int nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            if (nRemaining < MIN_MERGE) {
                final int initRunLen = PrimitiveSort.countRunAndMakeAscending(array, fromIndex, toIndex, comparator);
                PrimitiveSort.binarySort(array, fromIndex, toIndex, fromIndex + initRunLen, comparator);
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
        int runLen = PrimitiveSort.countRunAndMakeAscending(array, lo, hi, comparator);
        if (runLen < minRun) {
            final int force = Math.min(nRemaining, minRun);
            PrimitiveSort.binarySort(array, lo, lo + force, lo + runLen, comparator);
            runLen = force;
        }
        return runLen;
    }

    protected void mergeAt(int i) {
        PreConditions.require(stackSize >= 2);
        PreConditions.require(i >= 0);
        PreConditions.require(i == stackSize - 2 || i == stackSize - 3);

        int base1 = runBase[i];
        int len1 = runLength[i];
        final int base2 = runBase[i + 1];
        final int len2 = runLength[i + 1];

        PreConditions.require(len1 > 0 && len2 > 0);
        PreConditions.require(base1 + len1 == base2);

        runLength[i] = len1 + len2;
        if (i == stackSize - 3) {
            runBase[i + 1] = runBase[i + 2];
            runLength[i + 1] = runLength[i + 2];
        }

        --stackSize;
        final int ofsRight = gallopRight(longArray[base2], longArray, base1, len1, 0, longComparator);

        PreConditions.require(ofsRight >= 0);

        base1 += ofsRight;
        len1 -= ofsRight;
        if (len1 == 0) {
            return;
        }
        final int ofsLeft = gallopLeft(longArray[base1 + len1 - 1], longArray, base2, len2, len2 - 1, longComparator);

        decideMergeStrategy(base1, len1, base2, ofsLeft);
    }

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important
    @SuppressWarnings({"squid:S1541", "squid:S3776"})
    private static int gallopLeft(long key, long[] array, int base, int len, int hint, LongComparator comparator) {
        PreConditions.require(hint >= 0 && hint < len);

        int lastOfs = 0;
        int ofs = 1;
        if (comparator.compareLong(key, array[base + hint]) > 0) {
            final int maxOfs = len - hint;

            while (ofs < maxOfs && comparator.compareLong(key, array[base + hint + ofs]) > 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            lastOfs += hint;
            ofs += hint;
        } else {
            final int maxOfs = hint + 1;

            while (ofs < maxOfs && comparator.compareLong(key, array[base + hint - ofs]) <= 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            final int tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        }
        PreConditions.require(-1 <= lastOfs && lastOfs < ofs && ofs <= len);

        ++lastOfs;

        while (lastOfs < ofs) {
            final int maxOfs = lastOfs + ((ofs - lastOfs) >>> 1);
            if (comparator.compareLong(key, array[base + maxOfs]) > 0) {
                lastOfs = maxOfs + 1;
            } else {
                ofs = maxOfs;
            }
        }
        PreConditions.require(lastOfs == ofs);
        return ofs;
    }

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important
    @SuppressWarnings({"squid:S1541", "squid:S3776"})
    private static int gallopRight(long key, long[] array, int base, int len, int hint, LongComparator comparator) {
        PreConditions.require(hint >= 0 && hint < len);
        int ofs = 1;
        int lastOfs = 0;
        if (comparator.compareLong(key, array[base + hint]) < 0) {
            final int maxOfs = hint + 1;

            while (ofs < maxOfs && comparator.compareLong(key, array[base + hint - ofs]) < 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            final int tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        } else {
            final int maxOfs = len - hint;

            while (ofs < maxOfs && comparator.compareLong(key, array[base + hint + ofs]) >= 0) {
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
            final int maxOfs = lastOfs + ((ofs - lastOfs) >>> 1);
            if (comparator.compareLong(key, array[base + maxOfs]) < 0) {
                ofs = maxOfs;
            } else {
                lastOfs = maxOfs + 1;
            }
        }
        PreConditions.require(lastOfs == ofs);
        return ofs;
    }

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important
    @SuppressWarnings({"squid:S134", "squid:S135", "squid:S138", "squid:S1119", "squid:S1541", "squid:S3776"})
    protected void mergeLo(int base1, int len1, int base2, int len2) {
        PreConditions.require(len1 > 0 && len2 > 0 && base1 + len1 == base2);
        // Copy first run into temp array
        long[] array = longArray; // For performance
        long[] tmpArray = ensureCapacity(len1);
        int cursor1 = 0; // Indexes into tmp array
        int cursor2 = base2;   // Indexes int a
        int dest = base1;      // Indexes int a
        System.arraycopy(array, base1, tmpArray, cursor1, len1);
        // Move first element of second run and deal with degenerate cases
        array[dest++] = array[cursor2++];
        if (--len2 == 0) {
            System.arraycopy(tmpArray, cursor1, array, dest, len1);
            return;
        }
        if (len1 == 1) {
            System.arraycopy(array, cursor2, array, dest, len2);
            array[dest + len2] = tmpArray[cursor1]; // Last elt of run 1 to end of merge
            return;
        }
        LongComparator comparator = longComparator;  // Use local variable for performance
        int minGallop = this.minGallop;    //  "    "       "     "      "
        outer:
        while (true) {
            int count1 = 0; // Number of times in a row that first run won
            int count2 = 0; // Number of times in a row that second run won
            // Do the straightforward thing until (if ever) one run starts winning consistently.
            do {
                PreConditions.require(len1 > 1 && len2 > 0);
                if (comparator.compareLong(array[cursor2], tmpArray[cursor1]) < 0) {
                    array[dest++] = array[cursor2++];
                    count2++;
                    count1 = 0;
                    if (--len2 == 0) {
                        break outer;
                    }
                } else {
                    array[dest++] = tmpArray[cursor1++];
                    count1++;
                    count2 = 0;
                    if (--len1 == 1) {
                        break outer;
                    }
                }
            } while ((count1 | count2) < minGallop);
            /*
             * One run is winning so consistently that galloping may be a
             * huge win. So try that, and continue galloping until (if ever)
             * neither run appears to be winning consistently anymore.
             */
            do {
                PreConditions.require(len1 > 1 && len2 > 0);
                count1 = gallopRight(array[cursor2], tmpArray, cursor1, len1, 0, comparator);
                if (count1 != 0) {
                    System.arraycopy(tmpArray, cursor1, array, dest, count1);
                    dest += count1;
                    cursor1 += count1;
                    len1 -= count1;
                    if (len1 <= 1) {
                        break outer;
                    }
                }
                array[dest++] = array[cursor2++];
                if (--len2 == 0) {
                    break outer;
                }

                count2 = gallopLeft(tmpArray[cursor1], array, cursor2, len2, 0, comparator);
                if (count2 != 0) {
                    System.arraycopy(array, cursor2, array, dest, count2);
                    dest += count2;
                    cursor2 += count2;
                    len2 -= count2;
                    if (len2 == 0) {
                        break outer;
                    }
                }
                array[dest++] = tmpArray[cursor1++];
                if (--len1 == 1) {
                    break outer;
                }
                minGallop--;
            } while (count1 >= MIN_GALLOP || count2 >= MIN_GALLOP);
            minGallop = Math.max(minGallop, 0);
            minGallop += 2;  // Penalize for leaving gallop mode
        }  // End of "outer" loop
        this.minGallop = Math.max(minGallop, 1);  // Write back to field

        if (len1 == 1) {
            PreConditions.require(len2 > 0);
            System.arraycopy(array, cursor2, array, dest, len2);
            array[dest + len2] = tmpArray[cursor1]; //  Last elt of run 1 to end of merge
        } else if (len1 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        } else {
            PreConditions.require(len2 == 0);
            System.arraycopy(tmpArray, cursor1, array, dest, len1);
        }
    }

    @SuppressWarnings({"squid:S134", "squid:S135", "squid:S138", "squid:S1119", "squid:S1541", "squid:S3776"})
    protected void mergeHi(int base1, int len1, int base2, int len2) {
        PreConditions.require(len1 > 0 && len2 > 0 && base1 + len1 == base2);
        // Copy second run into temp array
        long[] array = longArray; // For performance
        long[] tmpArray = ensureCapacity(len2);
        System.arraycopy(array, base2, tmpArray, 0, len2);

        int cursor1 = base1 + len1 - 1;  // Indexes into a
        int dest = base2 + len2 - 1;     // Indexes into a
        // Move last element of first run and deal with degenerate cases
        array[dest--] = array[cursor1--];
        if (--len1 == 0) {
            System.arraycopy(tmpArray, 0, array, dest - (len2 - 1), len2);
            return;
        }
        int cursor2 = len2 - 1; // Indexes into tmp array
        if (len2 == 1) {
            dest -= len1;
            cursor1 -= len1;
            System.arraycopy(array, cursor1 + 1, array, dest + 1, len1);
            array[dest] = tmpArray[cursor2];
            return;
        }
        LongComparator comparator = longComparator;  // Use local variable for performance
        int minGallop = this.minGallop;    //  "    "       "     "      "
        outer:
        while (true) {
            int count1 = 0; // Number of times in a row that first run won
            int count2 = 0; // Number of times in a row that second run won
            /*
             * Do the straightforward thing until (if ever) one run
             * appears to win consistently.
             */
            do {
                PreConditions.require(len1 > 0 && len2 > 1);
                if (comparator.compareLong(tmpArray[cursor2], array[cursor1]) < 0) {
                    array[dest--] = array[cursor1--];
                    count1++;
                    count2 = 0;
                    if (--len1 == 0) {
                        break outer;
                    }
                } else {
                    array[dest--] = tmpArray[cursor2--];
                    count2++;
                    count1 = 0;
                    if (--len2 == 1) {
                        break outer;
                    }
                }
            } while ((count1 | count2) < minGallop);

            /*
             * One run is winning so consistently that galloping may be a
             * huge win. So try that, and continue galloping until (if ever)
             * neither run appears to be winning consistently anymore.
             */
            do {
                PreConditions.require(len1 > 0 && len2 > 1);
                count1 = len1 - gallopRight(tmpArray[cursor2], array, base1, len1, len1 - 1, comparator);
                if (count1 != 0) {
                    dest -= count1;
                    cursor1 -= count1;
                    len1 -= count1;
                    System.arraycopy(array, cursor1 + 1, array, dest + 1, count1);
                    if (len1 == 0) {
                        break outer;
                    }
                }
                array[dest--] = tmpArray[cursor2--];
                if (--len2 == 1) {
                    break outer;
                }
                count2 = len2 - gallopLeft(array[cursor1], tmpArray, 0, len2, len2 - 1, comparator);
                if (count2 != 0) {
                    dest -= count2;
                    cursor2 -= count2;
                    len2 -= count2;
                    System.arraycopy(tmpArray, cursor2 + 1, array, dest + 1, count2);
                    if (len2 <= 1) {
                        break outer;
                    }
                }
                array[dest--] = array[cursor1--];
                if (--len1 == 0) {
                    break outer;
                }
                minGallop--;
            } while (count1 >= MIN_GALLOP || count2 >= MIN_GALLOP);
            minGallop = Math.max(minGallop, 0);
            minGallop += 2;  // Penalize for leaving gallop mode
        }  // End of "outer" loop
        this.minGallop = Math.max(minGallop, 1);  // Write back to field

        if (len2 == 1) {
            PreConditions.require(len1 > 0);
            dest -= len1;
            cursor1 -= len1;
            System.arraycopy(array, cursor1 + 1, array, dest + 1, len1);
            array[dest] = tmpArray[cursor2];  // Move first elt of run2 to front of merge
        } else if (len2 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        } else {
            PreConditions.require(len1 == 0);
            System.arraycopy(tmpArray, 0, array, dest - (len2 - 1), len2);
        }
    }

    @SuppressWarnings("squid:S2384")
    private long[] ensureCapacity(int minCapacity) {
        if (tempArray.length < minCapacity) {
            final int newLength = calculateNewLength(minCapacity, longArray.length);
            tempArray = new long[newLength];
        }
        return tempArray;
    }
}
