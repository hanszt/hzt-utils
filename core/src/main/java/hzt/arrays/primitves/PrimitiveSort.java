package hzt.arrays.primitves;

import hzt.utils.primitive_comparators.DoubleComparator;
import hzt.utils.primitive_comparators.IntComparator;
import hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

public final class PrimitiveSort {

    private PrimitiveSort() {
    }

    public static void sort(int @NotNull [] array, @NotNull IntComparator comparator) {
        sort(array, 0, array.length, comparator);
    }

    public static void sort(int @NotNull [] array, int fromIndex, int toIndex, @NotNull IntComparator comparator) {
        checkBounds(array.length, fromIndex, toIndex);
        IntTimSort.sort(array, fromIndex, toIndex, comparator);
    }

    public static void sort(long @NotNull [] array, @NotNull LongComparator comparator) {
        sort(array, 0, array.length, comparator);
    }

    public static void sort(long @NotNull [] array, int fromIndex, int toIndex, @NotNull LongComparator comparator) {
        checkBounds(array.length, fromIndex, toIndex);
        LongTimSort.sort(array, fromIndex, toIndex, comparator);
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
