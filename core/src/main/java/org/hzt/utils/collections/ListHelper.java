package org.hzt.utils.collections;

import org.hzt.utils.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntUnaryOperator;

public final class ListHelper {

    private ListHelper() {
    }

    public static int binarySearch(final int size,
                                   final int fromIndex,
                                   final int toIndex,
                                   @NotNull final IntUnaryOperator midValComparison) {
        PreConditions.rangeCheck(size, fromIndex, toIndex);

        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            final int mid = (low + high) >>> 1;
            final int cmp = midValComparison.applyAsInt(mid);

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
