package org.hzt.utils.collections;

import org.hzt.utils.PreConditions;

import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

final class ListHelper {

    private ListHelper() {
    }

    static <T> int binarySearch(
            int size, IntFunction<T> midValExtractor, int fromIndex, int toIndex, ToIntFunction<T> comparison) {
        PreConditions.rangeCheck(size, fromIndex, toIndex);

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
}
