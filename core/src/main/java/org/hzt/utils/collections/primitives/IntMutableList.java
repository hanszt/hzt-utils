package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

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
    default IntMutableList plus(@NotNull Iterable<Integer> iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default IntMutableList plus(int @NotNull ... array) {
        addAll(array);
        return this;
    }

    @Override
    default MutableListX<Integer> boxed() {
        return asSequence().boxed().toMutableList();
    }

    int set(int index, int value);

    @Override
    default boolean remove(int i) {
        final var index = indexOf(i);
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