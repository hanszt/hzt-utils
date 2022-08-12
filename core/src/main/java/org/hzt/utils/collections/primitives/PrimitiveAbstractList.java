package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;

import java.util.PrimitiveIterator;

/**
 * @param <T> The boxed type
 * @param <A> The primitive array type
 * @param <T_CONS> The primitive consumer
 * @param <I> the primitive iterator
 */
abstract class PrimitiveAbstractList<T, A, T_CONS, I extends PrimitiveIterator<T, T_CONS>>
extends PrimitiveAbstractCollection<T, A, T_CONS, I> implements PrimitiveCollection<T, T_CONS, A> {

    private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;
    static final int DEFAULT_CAPACITY = 10;
    A elementData;

    PrimitiveAbstractList(int size, A elementData) {
        super(size);
        this.elementData = elementData;
    }

    abstract A copyElementData(int newLength);

    public A toArray() {
        return copyElementData(size);
    }

    public void clear() {
        size = 0;
        elementData = newArray(DEFAULT_CAPACITY);
    }

    /**
     * @param oldCapacity the current array size
     * @param isInitEmptyArray check if array empty
     * @return the grown array
     */
    A growArray(int oldCapacity, boolean isInitEmptyArray) {
        int minCapacity = oldCapacity + 1;
        if (isInitEmptyArray) {
            return newArray(minCapacity);
        }
        final var minGrowth = minCapacity - oldCapacity;
        final var prefGrowth = oldCapacity >> 1;
        int newCapacity = newLength(oldCapacity, minGrowth, prefGrowth);
        return copyElementData(newCapacity);
    }

    private static int newLength(int oldLength, int minGrowth, int prefGrowth) {
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
}