package hzt.collections.primitives;

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

    static IntMutableListX withInitCapacity(int capacity) {
        return new IntArrayList(capacity);
    }

    default PrimitiveIterator.OfInt iterator() {
        return listIterator();
    }

    boolean add(int l);

    int removeAt(int index);

    default int removeFirst() {
        return removeAt(0);
    }

    default int removeLast() {
        return removeAt(size() - 1);
    }

    void clear();
}
