package hzt.collections;

import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;

final class ArrayHelper {

    private ArrayHelper() {
    }

    static <T> int binarySearch(
            int size, IntFunction<T> midValExtractor, int fromIndex, int toIndex, ToIntFunction<T> comparison) {
        rangeCheck(size, fromIndex, toIndex);

        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final T midVal = midValExtractor.apply(mid);
            final int cmp = comparison.applyAsInt(midVal);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    static int binarySearch(
            int size, IntUnaryOperator midValExtractor, int fromIndex, int toIndex, IntUnaryOperator comparison) {
        rangeCheck(size, fromIndex, toIndex);

        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final int midVal = midValExtractor.applyAsInt(mid);
            final int cmp = comparison.applyAsInt(midVal);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    private static void rangeCheck(int size, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex (" + fromIndex + ") is greater than toIndex (" + toIndex + ").");
        }
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex (" + fromIndex + ") is less than zero.");
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex (" + toIndex + ") is greater than size (" + size + ").");
        }
    }
}
