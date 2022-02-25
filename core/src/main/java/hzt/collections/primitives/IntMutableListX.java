package hzt.collections.primitives;

import hzt.collections.MutableListX;
import hzt.iterators.primitives.PrimitiveListIterator;

import java.util.PrimitiveIterator;

public interface IntMutableListX extends IntListX, IntMutableCollection,
        PrimitiveMutableList<PrimitiveListIterator.OfInt> {

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

    @Override
    default MutableListX<Integer> boxed() {
        return asSequence().boxed().toMutableList();
    }

    static IntMutableListX withInitCapacity(int capacity) {
        return new IntArrayList(capacity);
    }

    default PrimitiveIterator.OfInt iterator() {
        return listIterator();
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
