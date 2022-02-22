package hzt.arrays.primitves;

import hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

public final class LongSort {

    private LongSort() {
    }

    public static void sort(long @NotNull [] array, @NotNull LongComparator comparator) {
        sort(array, 0, array.length, comparator);
    }

    public static void sort(long @NotNull [] array, int fromIndex, int toIndex, @NotNull LongComparator comparator) {
        checkBounds(array.length, fromIndex, toIndex);
        LongTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    private static void checkBounds(int length, int fromIndex, int toIndex) {
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException("fromIndex < 0");
        }
        if (toIndex > length) {
            throw new ArrayIndexOutOfBoundsException("toIndex > a.length");
        }
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex > toIndex");
        }
    }
}
