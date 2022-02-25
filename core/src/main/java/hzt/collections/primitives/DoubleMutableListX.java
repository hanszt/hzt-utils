package hzt.collections.primitives;

import hzt.collections.MutableListX;
import hzt.iterators.primitives.PrimitiveListIterator;

import java.util.PrimitiveIterator;

public interface DoubleMutableListX extends DoubleListX, DoubleMutableCollection,
        PrimitiveMutableList<PrimitiveListIterator.OfDouble> {

    static DoubleMutableListX empty() {
        return new DoubleArrayList();
    }

    static DoubleMutableListX of(Iterable<Double> iterable) {
        return new DoubleArrayList(iterable);
    }

    static DoubleMutableListX of(DoubleListX doubleListX) {
        return new DoubleArrayList(doubleListX);
    }

    static DoubleMutableListX of(double... array) {
        return new DoubleArrayList(array);
    }

    static DoubleMutableListX withInitCapacity(int capacity) {
        return new DoubleArrayList(capacity);
    }

    PrimitiveListIterator.OfDouble listIterator();

    default PrimitiveIterator.OfDouble iterator() {
        return listIterator();
    }

    @Override
    default MutableListX<Double> boxed() {
        return asSequence().boxed().toMutableList();
    }

    long set(int index, long value);

    @Override
    default boolean remove(double d) {
        final var index = indexOf(d);
        if (index >= 0) {
            removeAt(index);
            return true;
        }
        return false;
    }

    double removeAt(int index);

    default double removeFirst() {
        return removeAt(0);
    }

    default double removeLast() {
        return removeAt(size() - 1);
    }

    void clear();
}
