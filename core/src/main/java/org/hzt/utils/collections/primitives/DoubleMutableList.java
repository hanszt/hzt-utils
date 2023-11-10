package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.DoubleComparator;

public interface DoubleMutableList extends DoubleList, DoubleMutableCollection,
        PrimitiveMutableList<PrimitiveListIterator.OfDouble, DoubleComparator> {

    static DoubleMutableList empty() {
        return new DoubleArrayList();
    }

    static DoubleMutableList of(Iterable<Double> iterable) {
        return new DoubleArrayList(iterable);
    }

    static DoubleMutableList of(DoubleList doubleList) {
        return new DoubleArrayList(doubleList);
    }

    static DoubleMutableList of(double... array) {
        return new DoubleArrayList(array);
    }

    static DoubleMutableList withInitCapacity(int capacity) {
        return new DoubleArrayList(capacity);
    }

    @Override
    default DoubleMutableList plus(Iterable<Double> iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default DoubleMutableList plus(double... iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default MutableListX<Double> boxed() {
        return MutableListX.of(this);
    }

    double set(int index, double value);

    boolean add(int index, double value);

    boolean addAll(int index, PrimitiveIterable.OfDouble iterable);

    @Override
    default boolean remove(double d) {
        final int index = indexOf(d);
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
