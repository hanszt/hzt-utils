package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.LongComparator;

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
    default LongMutableList plus(Iterable<Long> iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default LongMutableList plus(long... array) {
        addAll(array);
        return this;
    }

    @Override
    default MutableListX<Long> boxed() {
        return MutableListX.of(this);
    }

    long set(int index, long value);

    boolean add(int index, long value);

    boolean addAll(int index, PrimitiveIterable.OfLong iterable);

    @Override
    default boolean remove(long l) {
        final int index = indexOf(l);
        if (index >= 0) {
            removeAt(index);
            return true;
        }
        return false;
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
