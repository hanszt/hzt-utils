package hzt.collections.primitives;

import hzt.collections.MutableListX;
import hzt.iterators.primitives.PrimitiveListIterator;

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
        return asSequence().boxed().toMutableList();
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
