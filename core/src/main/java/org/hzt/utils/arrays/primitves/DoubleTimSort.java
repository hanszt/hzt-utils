package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting double arrays where a DoubleComparator is used for the comparisons
 * Based on the generic java implementation of TimSort
 *
 * @see java.util.TimSort;
 */
@SuppressWarnings({"DuplicatedCode", "JavadocReference"})
public final class DoubleTimSort extends PrimitiveTimSort {

    private final DoubleComparator doubleComparator;
    private final double[] doubleArray;
    private final int[] runBase;
    private final int[] runLen;
    private double[] tempArray;

    @SuppressWarnings("squid:S2384")
    private DoubleTimSort(double[] doubleArray, DoubleComparator doubleComparator) {
        super(doubleArray.length);
        this.doubleArray = doubleArray;
        this.doubleComparator = doubleComparator;
        this.tempArray = new double[tempLength];

        int stackLen = getStackLength(doubleArray.length);
        this.runBase = new int[stackLen];
        this.runLen = new int[stackLen];
    }

    static void sort(double @NotNull [] array, int fromIndex, int toIndex, @NotNull DoubleComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);

        final int nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            if (nRemaining < MIN_MERGE) {
                int initRunLen = countRunAndMakeAscending(array, fromIndex, toIndex, comparator);
                binarySort(array, fromIndex, toIndex, fromIndex + initRunLen, comparator);
            } else {
                DoubleTimSort timSort = new DoubleTimSort(array, comparator);
                final int minRun = minRunLength(nRemaining);

                fromIndex = getLo(array, fromIndex, toIndex, comparator, nRemaining, timSort, minRun);

                PreConditions.require(fromIndex == toIndex);

                timSort.mergeForceCollapse();

                PreConditions.require(timSort.stackSize == 1);
            }
        }
    }

    private static int getLo(double[] array, int lo, int hi, DoubleComparator c, int nRemaining, DoubleTimSort ts, int minRun) {
        do {
            int runLen = countRunAndMakeAscending(array, lo, hi, c);
            if (runLen < minRun) {
                int force = Math.min(nRemaining, minRun);
                binarySort(array, lo, lo + force, lo + runLen, c);
                runLen = force;
            }

            ts.pushRun(lo, runLen);
            ts.mergeCollapse();
            lo += runLen;
            nRemaining -= runLen;
        } while (nRemaining != 0);
        return lo;
    }

    private static void binarySort(double[] array, int lo, int hi, int start, DoubleComparator comparator) {
        PreConditions.require(lo <= start && start <= hi);
        if (start == lo) {
            ++start;
        }
        while (start < hi) {
            double pivot = array[start];
            int left = lo;
            int right = start;

            PreConditions.require(lo <= start);
            while (left < right) {
                int mid = (left + right) >>> 1;
                if (comparator.compareDouble(pivot, array[mid]) < 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            PreConditions.require(left == right);

            int n = start - left;
            if (n == 2) {
                array[left + 2] = array[left + 1];
                array[left + 1] = array[left];
            } else if (n == 1) {
                array[left + 1] = array[left];
            } else {
                System.arraycopy(array, left, array, left + 1, n);
            }
            array[left] = pivot;
            ++start;
        }
    }

    private static int countRunAndMakeAscending(double[] a, int lo, int hi, DoubleComparator comparator) {
        PreConditions.require(lo < hi);

        int runHi = lo + 1;
        if (runHi == hi) {
            return 1;
        }
        if (comparator.compareDouble(a[runHi++], a[lo]) >= 0) {
            while (runHi < hi && comparator.compareDouble(a[runHi], a[runHi - 1]) >= 0) {
                ++runHi;
            }
        } else {
            while (runHi < hi && comparator.compareDouble(a[runHi], a[runHi - 1]) < 0) {
                ++runHi;
            }
            reverseRange(a, lo, runHi);
        }
        return runHi - lo;
    }

    private static void reverseRange(double[] a, int lo, int hi) {
        --hi;
        while (lo < hi) {
            double t = a[lo];
            a[lo++] = a[hi];
            a[hi--] = t;
        }
    }

    private void pushRun(int runBase, int runLen) {
        this.runBase[stackSize] = runBase;
        this.runLen[stackSize] = runLen;
        ++stackSize;
    }

    private void mergeCollapse() {
        while (stackSize > 1) {
            int n = stackSize - 2;
            if (runLen[n] > runLen[n + 1]) {
                return; // Invariant is established
            }
            final var b1 = n > 0 && runLen[n - 1] <= runLen[n] + runLen[n + 1];
            final var b2 = n > 1 && runLen[n - 2] <= runLen[n - 1] + runLen[n];
            if ((b1 || b2)) {
                final var b3 = runLen[n - 1] < runLen[n + 1];
                if (b3) {
                    n--;
                }
            }
            mergeAt(n);
        }
    }

    private void mergeForceCollapse() {
        while (stackSize > 1) {
            int n = stackSize - 2;
            if (n > 0 && runLen[n - 1] < runLen[n + 1]) {
                --n;
            }
            mergeAt(n);
        }
    }

    private void mergeAt(int i) {
        PreConditions.require(stackSize >= 2);
        PreConditions.require(i >= 0);
        PreConditions.require(i == stackSize - 2 || i == stackSize - 3);

        int base1 = runBase[i];
        int len1 = runLen[i];
        int base2 = runBase[i + 1];
        int len2 = runLen[i + 1];

        PreConditions.require(len1 > 0 && len2 > 0);
        PreConditions.require(base1 + len1 == base2);

        runLen[i] = len1 + len2;
        if (i == stackSize - 3) {
            runBase[i + 1] = runBase[i + 2];
            runLen[i + 1] = runLen[i + 2];
        }

        --stackSize;
        int k = gallopRight(doubleArray[base2], doubleArray, base1, len1, 0, doubleComparator);

        PreConditions.require(k >= 0);

        base1 += k;
        len1 -= k;
        if (len1 == 0) {
            return;
        }
        len2 = gallopLeft(doubleArray[base1 + len1 - 1], doubleArray, base2, len2, len2 - 1, doubleComparator);

        PreConditions.require(len2 >= 0);

        if (len2 != 0) {
            if (len1 <= len2) {
                mergeLo(base1, len1, base2, len2);
            } else {
                mergeHi(base1, len1, base2, len2);
            }
        }
    }

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important
    @SuppressWarnings({"squid:S1541", "squid:S3776"})
    private static int gallopLeft(double key, double[] a, int base, int len, int hint, DoubleComparator comparator) {
        PreConditions.require(hint >= 0 && hint < len);

        int lastOfs = 0;
        int ofs = 1;
        if (comparator.compareDouble(key, a[base + hint]) > 0) {
            final int maxOfs = len - hint;

            while (ofs < maxOfs && comparator.compareDouble(key, a[base + hint + ofs]) > 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            lastOfs += hint;
            ofs += hint;
        } else {
            final int maxOfs = hint + 1;

            while (ofs < maxOfs && comparator.compareDouble(key, a[base + hint - ofs]) <= 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            int tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        }
        PreConditions.require(-1 <= lastOfs && lastOfs < ofs && ofs <= len);

        ++lastOfs;

        while (lastOfs < ofs) {
            final int maxOfs = lastOfs + ((ofs - lastOfs) >>> 1);
            if (comparator.compareDouble(key, a[base + maxOfs]) > 0) {
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
    private static int gallopRight(double key, double[] array, int base, int len, int hint, DoubleComparator comparator) {
        PreConditions.require(hint >= 0 && hint < len);
        int ofs = 1;
        int lastOfs = 0;
        if (comparator.compareDouble(key, array[base + hint]) < 0) {
            final int maxOfs = hint + 1;

            while (ofs < maxOfs && comparator.compareDouble(key, array[base + hint - ofs]) < 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            int tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        } else {
            int maxOfs = len - hint;

            while (ofs < maxOfs && comparator.compareDouble(key, array[base + hint + ofs]) >= 0) {
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
            if (comparator.compareDouble(key, array[base + maxOfs]) < 0) {
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
    private void mergeLo(int base1, int len1, int base2, int len2) {
        PreConditions.require(len1 > 0 && len2 > 0 && base1 + len1 == base2);
        // Copy first run into temp array
        double[] array = doubleArray; // For performance
        double[] tmpArray = ensureCapacity(len1);
        int cursor1 = tempBase; // Indexes into tmp array
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
        DoubleComparator comparator = doubleComparator;  // Use local variable for performance
        int minGallop = this.minGallop;    //  "    "       "     "      "
        outer:
        while (true) {
            int count1 = 0; // Number of times in a row that first run won
            int count2 = 0; // Number of times in a row that second run won
            // Do the straightforward thing until (if ever) one run starts winning consistently.
            do {
                PreConditions.require(len1 > 1 && len2 > 0);
                if (comparator.compareDouble(array[cursor2], tmpArray[cursor1]) < 0) {
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
    private void mergeHi(int base1, int len1, int base2, int len2) {
        PreConditions.require(len1 > 0 && len2 > 0 && base1 + len1 == base2);

        // Copy second run into temp array
        double[] array = doubleArray; // For performance
        double[] tmp = ensureCapacity(len2);
        int tmpBase = this.tempBase;
        System.arraycopy(array, base2, tmp, tmpBase, len2);

        int cursor1 = base1 + len1 - 1;  // Indexes into a
        int dest = base2 + len2 - 1;     // Indexes into a
        // Move last element of first run and deal with degenerate cases
        array[dest--] = array[cursor1--];
        if (--len1 == 0) {
            System.arraycopy(tmp, tmpBase, array, dest - (len2 - 1), len2);
            return;
        }
        int cursor2 = tmpBase + len2 - 1; // Indexes into tmp array
        if (len2 == 1) {
            dest -= len1;
            cursor1 -= len1;
            System.arraycopy(array, cursor1 + 1, array, dest + 1, len1);
            array[dest] = tmp[cursor2];
            return;
        }

        DoubleComparator comparator = doubleComparator;  // Use local variable for performance
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
                if (comparator.compareDouble(tmp[cursor2], array[cursor1]) < 0) {
                    array[dest--] = array[cursor1--];
                    count1++;
                    count2 = 0;
                    if (--len1 == 0) {
                        break outer;
                    }
                } else {
                    array[dest--] = tmp[cursor2--];
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
                count1 = len1 - gallopRight(tmp[cursor2], array, base1, len1, len1 - 1, comparator);
                if (count1 != 0) {
                    dest -= count1;
                    cursor1 -= count1;
                    len1 -= count1;
                    System.arraycopy(array, cursor1 + 1, array, dest + 1, count1);
                    if (len1 == 0) {
                        break outer;
                    }
                }
                array[dest--] = tmp[cursor2--];
                if (--len2 == 1) {
                    break outer;
                }
                count2 = len2 - gallopLeft(array[cursor1], tmp, tmpBase, len2, len2 - 1, comparator);
                if (count2 != 0) {
                    dest -= count2;
                    cursor2 -= count2;
                    len2 -= count2;
                    System.arraycopy(tmp, cursor2 + 1, array, dest + 1, count2);
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
            array[dest] = tmp[cursor2];  // Move first elt of run2 to front of merge
        } else if (len2 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        } else {
            PreConditions.require(len1 == 0);
            System.arraycopy(tmp, tmpBase, array, dest - (len2 - 1), len2);
        }
    }

    @SuppressWarnings("squid:S2384")
    private double[] ensureCapacity(int minCapacity) {
        if (tempLength < minCapacity) {
            int newSize = calculateNewLength(minCapacity, doubleArray.length);
            tempArray = new double[newSize];
            tempLength = newSize;
            tempBase = 0;
        }
        return tempArray;
    }
}
