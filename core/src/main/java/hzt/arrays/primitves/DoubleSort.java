package hzt.arrays.primitves;

import hzt.utils.primitive_comparators.DoubleComparator;
import org.jetbrains.annotations.NotNull;

public final class DoubleSort {

    private DoubleSort() {
    }

    public static void sort(double @NotNull [] array, @NotNull DoubleComparator comparator) {
        sort(array, 0, array.length, comparator);
    }

    public static void sort(double @NotNull [] array, int fromIndex, int toIndex, @NotNull DoubleComparator comparator) {
        checkBounds(array.length, fromIndex, toIndex);
        DoubleTimSort.sort(array, fromIndex, toIndex, comparator);
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
