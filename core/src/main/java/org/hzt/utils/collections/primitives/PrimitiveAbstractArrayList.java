package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;

import java.util.PrimitiveIterator;

/**
 * @param <T> The boxed type
 * @param <A> The primitive array type
 * @param <T_CONS> The primitive consumer
 * @param <I> the primitive iterator
 */
public abstract class PrimitiveAbstractArrayList<T, T_CONS, A, I extends PrimitiveIterator<T, T_CONS>>
extends PrimitiveAbstractCollection<T, T_CONS, A, I> implements PrimitiveCollection<T, T_CONS, A> {

    private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;
    protected static final int DEFAULT_CAPACITY = 10;
    protected A elementData;

    protected PrimitiveAbstractArrayList(final int initSize, final A elementData) {
        super(initSize);
        this.elementData = elementData;
    }

    protected abstract A copyElementData(int newLength);

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
    protected A growArray(final int oldCapacity, final boolean isInitEmptyArray) {
        final int minCapacity = oldCapacity + 1;
        if (isInitEmptyArray) {
            return newArray(minCapacity);
        }
        final int minGrowth = minCapacity - oldCapacity;
        final int prefGrowth = oldCapacity >> 1;
        final int newCapacity = newLength(oldCapacity, minGrowth, prefGrowth);
        return copyElementData(newCapacity);
    }

    private static int newLength(final int oldLength, final int minGrowth, final int prefGrowth) {
        PreConditions.require(oldLength >= 0);
        PreConditions.require(minGrowth > 0);
        // might overflow
        final int prefLength = oldLength + Math.max(minGrowth, prefGrowth);
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        }
        return hugeLength(oldLength, minGrowth);
    }

    private static int hugeLength(final int oldLength, final int minGrowth) {
        final int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError("Required array length " + oldLength + " + " + minGrowth + " is too large");
        }
        return Math.max(minLength, SOFT_MAX_ARRAY_LENGTH);
    }

    protected void rangeCheckForAdd(final int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(final int index) {
        return "Index: " + index + ", Size: " + size;
    }
}
