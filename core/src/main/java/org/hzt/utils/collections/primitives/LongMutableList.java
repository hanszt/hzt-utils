package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.jetbrains.annotations.NotNull;

public interface LongMutableList extends LongList, LongMutableCollection,
        PrimitiveMutableList<PrimitiveListIterator.OfLong, LongComparator> {

    static LongMutableList empty() {
        return new LongArrayList();
    }

    static LongMutableList of(Iterable<Long> iterable) {
        return new LongArrayList(iterable);
    }

    static LongMutableList of(LongList longList) {
        return new LongArrayList(longList);
    }

    static LongMutableList of(long... array) {
        return new LongArrayList(array);
    }

    static LongMutableList withInitCapacity(int capacity) {
        return new LongArrayList(capacity);
    }

    @Override
    default LongMutableList plus(@NotNull Iterable<Long> iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default LongMutableList plus(long @NotNull ... array) {
        addAll(array);
        return this;
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
