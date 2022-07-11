package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.jetbrains.annotations.NotNull;

public interface IntMutableListX extends IntListX, IntMutableCollection,
        PrimitiveMutableList<PrimitiveListIterator.OfInt, IntComparator> {

    static IntMutableListX empty() {
        return new IntArrayList();
    }

    static IntMutableListX of(Iterable<Integer> iterable) {
        return new IntArrayList(iterable);
    }

    static IntMutableListX of(IntListX intListX) {
        return new IntArrayList(intListX);
    }

    static IntMutableListX of(int... array) {
        return new IntArrayList(array);
    }

    static IntMutableListX withInitCapacity(int capacity) {
        return new IntArrayList(capacity);
    }

    @Override
    default IntMutableListX plus(@NotNull Iterable<Integer> iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default IntMutableListX plus(int @NotNull ... array) {
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
