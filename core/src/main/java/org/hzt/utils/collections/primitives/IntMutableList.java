package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.IntComparator;

public interface IntMutableList extends IntList, IntMutableCollection,
        PrimitiveMutableList<PrimitiveListIterator.OfInt, IntComparator> {

    static IntMutableList empty() {
        return new IntArrayList();
    }

    static IntMutableList of(Iterable<Integer> iterable) {
        return new IntArrayList(iterable);
    }

    static IntMutableList of(IntList intList) {
        return new IntArrayList(intList);
    }

    static IntMutableList of(int... array) {
        return new IntArrayList(array);
    }

    static IntMutableList withInitCapacity(int capacity) {
        return new IntArrayList(capacity);
    }

    @Override
    default IntMutableList plus(Iterable<Integer> iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default IntMutableList plus(int... array) {
        addAll(array);
        return this;
    }

    @Override
    default MutableListX<Integer> boxed() {
        return MutableListX.of(this);
    }

    int set(int index, int value);

    boolean add(int index, int value);

    boolean addAll(int index, PrimitiveIterable.OfInt iterable);

    @Override
    default boolean remove(int i) {
        final int index = indexOf(i);
        if (index >= 0) {
            removeAt(index);
            return true;
        }
        return false;
    }

    int removeAt(int index);

    default int removeFirst() {
        return removeAt(0);
    }

    default int removeLast() {
        return removeAt(size() - 1);
    }

    void clear();
}
