package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

public interface LongMutableListX extends LongListX, LongMutableCollection,
        PrimitiveMutableList<PrimitiveListIterator.OfLong> {

    static LongMutableListX empty() {
        return new LongArrayList();
    }

    static LongMutableListX of(Iterable<Long> iterable) {
        return new LongArrayList(iterable);
    }

    static LongMutableListX of(LongListX longListX) {
        return new LongArrayList(longListX);
    }

    static LongMutableListX of(long... array) {
        return new LongArrayList(array);
    }

    static LongMutableListX withInitCapacity(int capacity) {
        return new LongArrayList(capacity);
    }

    @Override
    default LongMutableListX plus(@NotNull Iterable<Long> iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default LongMutableListX plus(long @NotNull ... array) {
        addAll(array);
        return this;
    }

    default PrimitiveIterator.OfLong iterator() {
        return listIterator();
    }

    long set(int index, long value);

    @Override
    default boolean remove(long l) {
        final var index = indexOf(l);
        if (index >= 0) {
            removeAt(index);
            return true;
        }
        return false;
    }

    @Override
    default MutableListX<Long> boxed() {
        return MutableListX.of(this);
    }

    long removeAt(int index);

    default long removeFirst() {
        return removeAt(0);
    }

    default long removeLast() {
        return removeAt(size() - 1);
    }

    void clear();
}
