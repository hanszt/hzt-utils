package org.hzt.utils.arrays.primitves;

import org.hzt.utils.PreConditions;

/**
 * @param <A> the primitive array type
 * @param <C> the primitive comparator type
 *
 * @see java.util.TimSort;
 */
@SuppressWarnings("JavadocReference")
public abstract class PrimitiveTimSort<A, C> {

    static final int MIN_MERGE = 32;
    static final int MIN_GALLOP = 7;
    static final int INITIAL_TMP_STORAGE_LENGTH = 256;

    final int[] runBase;
    final int[] runLength;

    final A array;
    final C comparator;
    A tempArray;
    int minGallop = MIN_GALLOP;
    int stackSize = 0;

    protected PrimitiveTimSort(int length, A array, A tempArray, C comparator) {
        int stackLen = getStackLength(length);
        this.array = array;
        this.tempArray = tempArray;
        this.comparator = comparator;
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
        final int ofsRight = gallopRight(array, base2, array, base1, len1, 0, comparator);

        PreConditions.require(ofsRight >= 0);

        base1 += ofsRight;
        len1 -= ofsRight;
        if (len1 == 0) {
            return;
        }
        final int ofsLeft = gallopLeft(array, base1 + len1 - 1, array, base2, len2, len2 - 1, comparator);

        decideMergeStrategy(base1, len1, base2, ofsLeft);
    }

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important
    @SuppressWarnings({"SuspiciousSystemArraycopy", "squid:S134", "squid:S135", "squid:S138", "squid:S1119", "squid:S1541", "squid:S3776"})
    protected void mergeLo(int base1, int len1, int base2, int len2) {
        PreConditions.require(len1 > 0 && len2 > 0 && base1 + len1 == base2);
        // Copy first run into temp array
        A localArray = this.array; // For performance
        A tmpArray = ensureCapacity(len1);
        int cursor1 = 0; // Indexes into tmp array
        int cursor2 = base2;   // Indexes int a
        int dest = base1;      // Indexes int a
        System.arraycopy(localArray, base1, tmpArray, cursor1, len1);
        // Move first element of second run and deal with degenerate cases
        setInDestinationArray(localArray, cursor2, dest);
        dest++;
        cursor2++;
        if (--len2 == 0) {
            System.arraycopy(tmpArray, cursor1, localArray, dest, len1);
            return;
        }
        if (len1 == 1) {
            System.arraycopy(localArray, cursor2, localArray, dest, len2);
            setInDestinationArray(localArray, cursor1, dest + len2);
            return;
        }
        C localComparator = this.comparator;  // Use local variable for performance
        int localMinGallop = this.minGallop;    //  "    "       "     "      "
        outer:
        while (true) {
            int count1 = 0; // Number of times in a row that first run won
            int count2 = 0; // Number of times in a row that second run won
            // Do the straightforward thing until (if ever) one run starts winning consistently.
            do {
                PreConditions.require(len1 > 1 && len2 > 0);
                if (compare(localComparator, localArray, cursor2, tmpArray, cursor1) < 0) {
                    setInDestinationArray(localArray, cursor2, dest);
                    dest++;
                    cursor2++;
                    count2++;
                    count1 = 0;
                    if (--len2 == 0) {
                        break outer;
                    }
                } else {
                    setInDestinationArray(tmpArray, cursor1, localArray, dest);
                    dest++;
                    cursor1++;
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
                setInDestinationArray(localArray, cursor2, dest);
                dest++;
                cursor2++;
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
                setInDestinationArray(tmpArray, cursor1, localArray, dest);
                dest++;
                cursor1++;
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
            setInDestinationArray(tmpArray, cursor1, localArray, dest + len2);
        } else if (len1 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        } else {
            PreConditions.require(len2 == 0);
            System.arraycopy(tmpArray, cursor1, localArray, dest, len1);
        }
    }

    @SuppressWarnings({"SuspiciousSystemArraycopy", "squid:S134", "squid:S135", "squid:S138", "squid:S1119", "squid:S1541", "squid:S3776"})
    protected void mergeHi(int base1, int len1, int base2, int len2) {
        PreConditions.require(len1 > 0 && len2 > 0 && base1 + len1 == base2);
        // Copy second run into temp array
        A localArray = this.array; // For performance
        A tmpArray = ensureCapacity(len2);
        System.arraycopy(localArray, base2, tmpArray, 0, len2);

        int cursor1 = base1 + len1 - 1;  // Indexes into a
        int dest = base2 + len2 - 1;     // Indexes into a
        // Move last element of first run and deal with degenerate cases
        setInDestinationArray(localArray, cursor1, dest);
        cursor1--;
        dest--;
        if (--len1 == 0) {
            System.arraycopy(tmpArray, 0, localArray, dest - (len2 - 1), len2);
            return;
        }
        int cursor2 = len2 - 1; // Indexes into tmp array
        if (len2 == 1) {
            dest -= len1;
            cursor1 -= len1;
            System.arraycopy(localArray, cursor1 + 1, localArray, dest + 1, len1);
            setInDestinationArray(tmpArray, cursor2, localArray, dest);
            return;
        }
        C localComparator = this.comparator;  // Use local variable for performance
        int localMinGallop = this.minGallop;    //  "    "       "     "      "
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
                if (compare(localComparator, tmpArray, cursor2, localArray, cursor1) < 0) {
                    setInDestinationArray(localArray, cursor1, dest);
                    cursor1--;
                    dest--;
                    count1++;
                    count2 = 0;
                    if (--len1 == 0) {
                        break outer;
                    }
                } else {
                    setInDestinationArray(tmpArray, cursor2, localArray, dest);
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
                setInDestinationArray(tmpArray, cursor2, localArray, dest);
                dest--;
                cursor2--;
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
                setInDestinationArray(localArray, cursor1, dest);
                dest--;
                cursor1--;
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
            setInDestinationArray(tmpArray, cursor2, localArray, dest);
        } else if (len2 == 0) {
            throw new IllegalArgumentException("Comparison method violates its general contract!");
        } else {
            PreConditions.require(len1 == 0);
            System.arraycopy(tmpArray, 0, localArray, dest - (len2 - 1), len2);
        }
    }

    private void setInDestinationArray(A array, int sourceIndex, int destIndex) {
        setInDestinationArray(array, sourceIndex, array, destIndex);
    }

    abstract void setInDestinationArray(A sourceArray, int sourceIndex, A destArray, int destIndex);

    abstract A ensureCapacity(int minCapacity);

    abstract int compare(C comparator, A array1, int index1, A array2, int index2);

    //Suppress the warnings that have to do with to long and complex methods. In this case performance is more important
    @SuppressWarnings({"squid:S1541", "squid:S3776"})
    int gallopRight(A array1, int cursor, A array, int base, int len, int hint, C comparator) {
        PreConditions.require(hint >= 0 && hint < len);
        int ofs = 1;
        int lastOfs = 0;
        if (compare(comparator, array1, cursor, array, base + hint) < 0) {
            final int maxOfs = hint + 1;

            while (ofs < maxOfs && compare(comparator, array1, cursor, array, base + hint - ofs) < 0) {
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

            while (ofs < maxOfs && compare(comparator, array1, cursor, array, base + hint + ofs) >= 0) {
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
            if (compare(comparator, array1, cursor, array, base + maxOfs) < 0) {
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
    int gallopLeft(A array1, int cursor, A array, int base, int len, int hint, C comparator) {
        PreConditions.require(hint >= 0 && hint < len);

        int lastOfs = 0;
        int ofs = 1;
        if (compare(comparator, array1, cursor, array, base + hint) > 0) {
            final int maxOfs = len - hint;

            while (ofs < maxOfs && compare(comparator, array1, cursor, array, base + hint + ofs) > 0) {
                lastOfs = ofs;
                ofs = (ofs << 1) + 1;
                ofs = (ofs <= 0) ? maxOfs : ofs;
            }
            ofs = Math.min(ofs, maxOfs);

            lastOfs += hint;
            ofs += hint;
        } else {
            final int maxOfs = hint + 1;

            while (ofs < maxOfs && compare(comparator, array1, cursor, array, base + hint - ofs) <= 0) {
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
            if (compare(comparator, array1, cursor, array, base + maxOfs) > 0) {
                lastOfs = maxOfs + 1;
            } else {
                ofs = maxOfs;
            }
        }
        PreConditions.require(lastOfs == ofs);
        return ofs;
    }

    private void pushRun(int runBase, int runLen) {
        this.runBase[stackSize] = runBase;
        this.runLength[stackSize] = runLen;
        ++stackSize;
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
