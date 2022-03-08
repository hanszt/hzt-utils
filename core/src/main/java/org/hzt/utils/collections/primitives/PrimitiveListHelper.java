package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

final class PrimitiveListHelper {

    private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;
    static final int DEFAULT_CAPACITY = 10;

    private PrimitiveListHelper() {
    }

    /**
     * @param array the primitive array to grow
     * @param oldCapacity the current array size
     * @param isInitEmptyArray check if array empty
     * @param <T> the primitive array type
     * @return the grown array
     */
    static <T> T growArray(T array, int oldCapacity, boolean isInitEmptyArray) {
        int minCapacity = oldCapacity + 1;
        if (isInitEmptyArray) {
            return newArray(minCapacity, array);
        }
        final int minGrowth = minCapacity - oldCapacity;
        final int prefGrowth = oldCapacity >> 1;
        int newCapacity = newLength(oldCapacity, minGrowth, prefGrowth);
        return copyElementData(newCapacity, array);
    }

    @NotNull
    private static <T> T newArray(int minCapacity, T original) {
        final int capacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        if (original instanceof int[]) {
            return (T) new int[capacity];
        } else if (original instanceof long[]) {
            return (T) new long[capacity];
        } else if (original instanceof double[]) {
            return (T) new double[capacity];
        } else {
            throw new IllegalArgumentException(original.getClass().getSimpleName() + " not a supported type");
        }
    }

    @NotNull
    private static <T> T copyElementData(int newCapacity, T primitiveArray) {
        if (primitiveArray instanceof int[]) {
            return (T) Arrays.copyOf((int[]) primitiveArray, newCapacity);
        } else if (primitiveArray instanceof long[]) {
            return (T) Arrays.copyOf((long[]) primitiveArray, newCapacity);
        } else if (primitiveArray instanceof double[]) {
            return (T) Arrays.copyOf((double[]) primitiveArray, newCapacity);
        } else {
            throw new IllegalArgumentException(primitiveArray.getClass().getSimpleName() + " not a supported type");
        }
    }

    static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        PreConditions.require(oldLength >= 0);
        PreConditions.require(minGrowth > 0);
        // might overflow
        int prefLength = oldLength + Math.max(minGrowth, prefGrowth);
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        }
        return hugeLength(oldLength, minGrowth);
    }

    private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError("Required array length " + oldLength + " + " + minGrowth + " is too large");
        }
        return Math.max(minLength, SOFT_MAX_ARRAY_LENGTH);
    }

    static int fastRemoveInt(int[] array, int size, int index) {
        final int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        array[newSize] = 0;
        return newSize;
    }

    static int fastRemoveLong(long[] array, int size, int index) {
        final int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        array[newSize] = 0L;
        return newSize;
    }

    static int fastRemoveDouble(double[] array, int size, int index) {
        final int newSize = size - 1;
        if (newSize > index) {
            System.arraycopy(array, index + 1, array, index, newSize - index);
        }
        array[newSize] = 0.0;
        return newSize;
    }

    static int checkIndex(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index out of range: " + index);
        }
        return index;
    }
}
