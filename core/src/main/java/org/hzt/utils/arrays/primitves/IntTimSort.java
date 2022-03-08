package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

/**
 * A Tim sort implementation for sorting int arrays where an IntComparator is used for the comparisons
 *
 * Credits for this implementation go to
 * <a href="https://github.com/mintern-java/primitive">Java Primitive by Mintern</a>
 */
public final class IntTimSort {

    private static final int MIN_MERGE = 32;
    private static final int MIN_GALLOP = 7;
    private static final int INITIAL_TMP_STORAGE_LENGTH = 256;

    private final int[] array;
    private final IntComparator comparator;
    private int minGallop = MIN_GALLOP;
    private int[] temporaryArray;
    private int tmpBase;
    private int tmpLen;
    private int stackSize = 0;
    private final int[] runBase;
    private final int[] runLen;

    private IntTimSort(int[] array, IntComparator comparator) {
        this.array = array;
        this.comparator = comparator;
        int length = array.length;
        int tempLength = length < 512 ? (length >>> 1) : INITIAL_TMP_STORAGE_LENGTH;
        this.temporaryArray = new int[tempLength];
        this.tmpBase = 0;
        this.tmpLen = tempLength;

        int stackLen = getStackLength(length);
        this.runBase = new int[stackLen];
        this.runLen = new int[stackLen];
    }

    private static int getStackLength(int len) {
        final var i1 = (len < 119_151) ? 24 : 49;
        final var i = len < 1_542 ? 10 : i1;
        return (len < 120) ? 5 : i;
    }

    static void sort(int @NotNull [] array, int fromIndex, int toIndex, @NotNull IntComparator comparator) {
        PreConditions.require(fromIndex >= 0 && fromIndex <= toIndex && toIndex <= array.length);

        int nRemaining = toIndex - fromIndex;
        if (nRemaining >= 2) {
            if (nRemaining < MIN_MERGE) {
                int initRunLen = countRunAndMakeAscending(array, fromIndex, toIndex, comparator);
                binarySort(array, fromIndex, toIndex, fromIndex + initRunLen, comparator);
            } else {
                IntTimSort ts = new IntTimSort(array, comparator);
                int minRun = minRunLength(nRemaining);

                fromIndex = getLo(array, fromIndex, toIndex, comparator, nRemaining, ts, minRun);

                PreConditions.require(fromIndex == toIndex);

                ts.mergeForceCollapse();

                PreConditions.require(ts.stackSize == 1);
            }
        }
    }

    private static int getLo(int[] a, int lo, int hi, IntComparator c, int nRemaining, IntTimSort ts, int minRun) {
        do {
            int runLen = countRunAndMakeAscending(a, lo, hi, c);
            if (runLen < minRun) {
                int force = Math.min(nRemaining, minRun);
                binarySort(a, lo, lo + force, lo + runLen, c);
                runLen = force;
            }

            ts.pushRun(lo, runLen);
            ts.mergeCollapse();
            lo += runLen;
            nRemaining -= runLen;
        } while (nRemaining != 0);
        return lo;
    }

    private static void binarySort(int[] array, int lo, int hi, int start, IntComparator c) {
        PreConditions.require(lo <= start && start <= hi);
        if (start == lo) {
            ++start;
        }
        while (start < hi) {
            int pivot = array[start];
            int left = lo;
            int right = start;

            PreConditions.require(lo <= start);
            int n;
            while (left < right) {
                n = (left + right) >>> 1;
                if (c.compareInt(pivot, array[n]) < 0) {
                    right = n;
                } else {
                    left = n + 1;
                }
            }

            PreConditions.require(left == right);

            n = start - left;
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

    private static int countRunAndMakeAscending(int[] a, int lo, int hi, IntComparator c) {
        PreConditions.require(lo < hi);

        int runHi = lo + 1;
        if (runHi == hi) {
            return 1;
        } else {
            if (c.compareInt(a[runHi++], a[lo]) >= 0) {
                while (runHi < hi && c.compareInt(a[runHi], a[runHi - 1]) >= 0) {
                    ++runHi;
                }
            } else {
                while (runHi < hi && c.compareInt(a[runHi], a[runHi - 1]) < 0) {
                    ++runHi;
                }
                reverseRange(a, lo, runHi);
            }

            return runHi - lo;
        }
    }

    private static void reverseRange(int[] a, int lo, int hi) {
        --hi;

        while (lo < hi) {
            int t = a[lo];
            a[lo++] = a[hi];
            a[hi--] = t;
        }

    }

    private static int minRunLength(int n) {
        PreConditions.require(n >= 0);

        int r;
        for (r = 0; n >= MIN_MERGE; n >>= 1) {
            r |= n & 1;
        }

        return n + r;
    }

    private void pushRun(int runBase, int runLen) {
        this.runBase[this.stackSize] = runBase;
        this.runLen[this.stackSize] = runLen;
        ++this.stackSize;
    }

    private void mergeCollapse() {
        while (true) {
            if (this.stackSize <= 1) {
                return;
            }
            int n = this.stackSize - 2;
            if (n > 0 && this.runLen[n - 1] <= this.runLen[n] + this.runLen[n + 1]) {
                if (this.runLen[n - 1] < this.runLen[n + 1]) {
                    --n;
                }
                this.mergeAt(n);
                continue;
            }

            if (this.runLen[n] <= this.runLen[n + 1]) {
                this.mergeAt(n);
                continue;
            }
            return;
        }
    }

    private void mergeForceCollapse() {
        int n;
        while (this.stackSize > 1) {
            n = this.stackSize - 2;
            if (n > 0 && this.runLen[n - 1] < this.runLen[n + 1]) {
                --n;
            }
            this.mergeAt(n);
        }

    }

    private void mergeAt(int i) {
        PreConditions.require(this.stackSize >= 2);
        PreConditions.require(i >= 0);
        PreConditions.require(i == this.stackSize - 2 || i == this.stackSize - 3);

        int base1 = this.runBase[i];
        int len1 = this.runLen[i];
        int base2 = this.runBase[i + 1];
        int len2 = this.runLen[i + 1];

        PreConditions.require(len1 > 0 && len2 > 0);
        PreConditions.require(base1 + len1 == base2);

        this.runLen[i] = len1 + len2;
        if (i == this.stackSize - 3) {
            this.runBase[i + 1] = this.runBase[i + 2];
            this.runLen[i + 1] = this.runLen[i + 2];
        }

        --this.stackSize;
        int k = gallopRight(this.array[base2], this.array, base1, len1, 0, this.comparator);

        PreConditions.require(k >= 0);

        base1 += k;
        len1 -= k;
        if (len1 != 0) {
            len2 = gallopLeft(this.array[base1 + len1 - 1], this.array, base2, len2, len2 - 1, this.comparator);

            PreConditions.require(len2 >= 0);

            if (len2 != 0) {
                if (len1 <= len2) {
                    this.mergeLo(base1, len1, base2, len2);
                } else {
                    this.mergeHi(base1, len1, base2, len2);
                }

            }
        }
    }

    private static int gallopLeft(int key, int[] a, int base, int len, int hint, IntComparator c) {
        PreConditions.require(hint >= 0 && hint < len);

        int lastOfs = 0;
        int ofs = 1;
        int m;
        if (c.compareInt(key, a[base + hint]) > 0) {
            m = len - hint;

            while (ofs < m && c.compareInt(key, a[base + hint + ofs]) > 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                if (ofs <= 0) {
                    ofs = m;
                }
            }

            if (ofs > m) {
                ofs = m;
            }

            lastOfs += hint;
            ofs += hint;
        } else {
            m = hint + 1;

            while (ofs < m && c.compareInt(key, a[base + hint - ofs]) <= 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                if (ofs <= 0) {
                    ofs = m;
                }
            }

            if (ofs > m) {
                ofs = m;
            }

            int tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        }

        PreConditions.require(-1 <= lastOfs && lastOfs < ofs && ofs <= len);

        ++lastOfs;

        while (lastOfs < ofs) {
            m = lastOfs + ((ofs - lastOfs) >>> 1);
            if (c.compareInt(key, a[base + m]) > 0) {
                lastOfs = m + 1;
            } else {
                ofs = m;
            }
        }

        PreConditions.require(lastOfs == ofs);

        return ofs;
    }

    private static int gallopRight(int key, int[] a, int base, int len, int hint, IntComparator c) {
        PreConditions.require(hint >= 0 && hint < len);
        int ofs = 1;
        int lastOfs = 0;
        int m;
        if (c.compareInt(key, a[base + hint]) < 0) {
            m = hint + 1;

            while (ofs < m && c.compareInt(key, a[base + hint - ofs]) < 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                if (ofs <= 0) {
                    ofs = m;
                }
            }

            if (ofs > m) {
                ofs = m;
            }

            int tmp = lastOfs;
            lastOfs = hint - ofs;
            ofs = hint - tmp;
        } else {
            m = len - hint;

            while (ofs < m && c.compareInt(key, a[base + hint + ofs]) >= 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                if (ofs <= 0) {
                    ofs = m;
                }
            }

            if (ofs > m) {
                ofs = m;
            }

            lastOfs += hint;
            ofs += hint;
        }

        PreConditions.require(-1 <= lastOfs && lastOfs < ofs && ofs <= len);

        ++lastOfs;

        while (lastOfs < ofs) {
            m = lastOfs + (ofs - lastOfs >>> 1);
            if (c.compareInt(key, a[base + m]) < 0) {
                ofs = m;
            } else {
                lastOfs = m + 1;
            }
        }
        PreConditions.require(lastOfs == ofs);

        return ofs;
    }

    private void mergeLo(int base1, int len1, int base2, int len2) {
        PreConditions.require(len1 > 0 && len2 > 0 && base1 + len1 == base2);

        int[] array = this.array;
        int[] tmp = this.ensureCapacity(len1);

        int cursor1 = this.tmpBase;
        System.arraycopy(array, base1, tmp, cursor1, len1);
        int dest = base1 + 1;
        int cursor2 = base2 + 1;
        array[base1] = array[base2];
        --len2;
        if (len2 == 0) {
            System.arraycopy(tmp, cursor1, array, dest, len1);
        } else if (len1 == 1) {
            System.arraycopy(array, cursor2, array, dest, len2);
            array[dest + len2] = tmp[cursor1];
        } else {
            IntComparator c = this.comparator;
            int minGallop = this.minGallop;

            label131:
            while (true) {
                int count1 = 0;
                int count2 = 0;

                while (len1 > 1 && len2 > 0) {
                    label145:
                    {
                        if (c.compareInt(array[cursor2], tmp[cursor1]) < 0) {
                            array[dest++] = array[cursor2++];
                            ++count2;
                            count1 = 0;
                            --len2;
                            if (len2 == 0) {
                                break label145;
                            }
                        } else {
                            array[dest++] = tmp[cursor1++];
                            ++count1;
                            count2 = 0;
                            --len1;
                            if (len1 == 1) {
                                break label145;
                            }
                        }

                        if ((count1 | count2) < minGallop) {
                            continue;
                        }

                        while (len1 > 1 && len2 > 0) {
                            count1 = gallopRight(array[cursor2], tmp, cursor1, len1, 0, c);
                            if (count1 != 0) {
                                System.arraycopy(tmp, cursor1, array, dest, count1);
                                dest += count1;
                                cursor1 += count1;
                                len1 -= count1;
                                if (len1 <= 1) {
                                    break label145;
                                }
                            }

                            array[dest++] = array[cursor2++];
                            --len2;
                            if (len2 == 0) {
                                break label145;
                            }

                            count2 = gallopLeft(tmp[cursor1], array, cursor2, len2, 0, c);
                            if (count2 != 0) {
                                System.arraycopy(array, cursor2, array, dest, count2);
                                dest += count2;
                                cursor2 += count2;
                                len2 -= count2;
                                if (len2 == 0) {
                                    break label145;
                                }
                            }

                            array[dest++] = tmp[cursor1++];
                            --len1;
                            if (len1 == 1) {
                                break label145;
                            }

                            --minGallop;
                            if (!(count1 >= MIN_GALLOP || count2 >= MIN_GALLOP)) {
                                if (minGallop < 0) {
                                    minGallop = 0;
                                }

                                minGallop += 2;
                                continue label131;
                            }
                        }

                        throw new AssertionError();
                    }

                    this.minGallop = Math.max(minGallop, 1);
                    if (len1 == 1) {
                        PreConditions.require(len2 > 0);

                        System.arraycopy(array, cursor2, array, dest, len2);
                        array[dest + len2] = tmp[cursor1];
                    } else {
                        if (len1 == 0) {
                            throw new IllegalArgumentException("Comparison method violates its general contract!");
                        }
                        PreConditions.require(len2 == 0);

                        System.arraycopy(tmp, cursor1, array, dest, len1);
                    }
                    return;
                }

                throw new AssertionError();
            }
        }
    }

    private void mergeHi(int base1, int len1, int base2, int len2) {
        PreConditions.require(len1 > 0 && len2 > 0 && base1 + len1 == base2);

        int[] array = this.array;
        int[] tmp = this.ensureCapacity(len2);
        int tmpBase = this.tmpBase;
        System.arraycopy(array, base2, tmp, tmpBase, len2);

        int cursor1 = base1 + len1 - 1;
        int cursor2 = tmpBase + len2 - 1;
        int dest = base2 + len2 - 1;
        array[dest--] = array[cursor1--];
        --len1;
        if (len1 == 0) {
            System.arraycopy(tmp, tmpBase, array, dest - (len2 - 1), len2);
        } else if (len2 == 1) {
            dest -= len1;
            cursor1 -= len1;
            System.arraycopy(array, cursor1 + 1, array, dest + 1, len1);
            array[dest] = tmp[cursor2];
        } else {
            IntComparator c = this.comparator;
            int minGallop = this.minGallop;

            label131:
            while (true) {
                int count1 = 0;
                int count2 = 0;

                while (len1 > 0 && len2 > 1) {
                    label145:
                    {
                        if (c.compareInt(tmp[cursor2], array[cursor1]) < 0) {
                            array[dest--] = array[cursor1--];
                            ++count1;
                            count2 = 0;
                            --len1;
                            if (len1 == 0) {
                                break label145;
                            }
                        } else {
                            array[dest--] = tmp[cursor2--];
                            ++count2;
                            count1 = 0;
                            --len2;
                            if (len2 == 1) {
                                break label145;
                            }
                        }

                        if ((count1 | count2) < minGallop) {
                            continue;
                        }

                        while (len1 > 0 && len2 > 1) {
                            count1 = len1 - gallopRight(tmp[cursor2], array, base1, len1, len1 - 1, c);
                            if (count1 != 0) {
                                dest -= count1;
                                cursor1 -= count1;
                                len1 -= count1;
                                System.arraycopy(array, cursor1 + 1, array, dest + 1, count1);
                                if (len1 == 0) {
                                    break label145;
                                }
                            }

                            array[dest--] = tmp[cursor2--];
                            --len2;
                            if (len2 == 1) {
                                break label145;
                            }

                            count2 = len2 - gallopLeft(array[cursor1], tmp, tmpBase, len2, len2 - 1, c);
                            if (count2 != 0) {
                                dest -= count2;
                                cursor2 -= count2;
                                len2 -= count2;
                                System.arraycopy(tmp, cursor2 + 1, array, dest + 1, count2);
                                if (len2 <= 1) {
                                    break label145;
                                }
                            }

                            array[dest--] = array[cursor1--];
                            --len1;
                            if (len1 == 0) {
                                break label145;
                            }

                            --minGallop;
                            if (!(count1 >= MIN_GALLOP || count2 >= MIN_GALLOP)) {
                                if (minGallop < 0) {
                                    minGallop = 0;
                                }

                                minGallop += 2;
                                continue label131;
                            }
                        }

                        throw new AssertionError();
                    }

                    this.minGallop = Math.max(minGallop, 1);
                    if (len2 == 1) {
                        PreConditions.require(len1 > 0);

                        dest -= len1;
                        cursor1 -= len1;
                        System.arraycopy(array, cursor1 + 1, array, dest + 1, len1);
                        array[dest] = tmp[cursor2];
                    } else {
                        if (len2 == 0) {
                            throw new IllegalArgumentException("Comparison method violates its general contract!");
                        }
                        PreConditions.require(len1 == 0);

                        System.arraycopy(tmp, tmpBase, array, dest - (len2 - 1), len2);
                    }
                    return;
                }

                throw new AssertionError();
            }
        }
    }

    private int[] ensureCapacity(int minCapacity) {
        if (this.tmpLen < minCapacity) {
            int newSize = minCapacity | (minCapacity >> 1);
            newSize |= newSize >> 2;
            newSize |= newSize >> 4;
            newSize |= newSize >> 8;
            newSize |= newSize >> 16;
            ++newSize;
            if (newSize < 0) {
                newSize = minCapacity;
            } else {
                newSize = Math.min(newSize, this.array.length >>> 1);
            }

            this.temporaryArray = new int[newSize];
            this.tmpLen = newSize;
            this.tmpBase = 0;
        }

        return this.temporaryArray;
    }
}
