package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterators.primitives.PrimitiveListIterator;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

public interface DoubleMutableListX extends DoubleListX, DoubleMutableCollection,
        PrimitiveMutableList<PrimitiveListIterator.OfDouble, DoubleComparator> {

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

    @Override
    default DoubleMutableListX plus(@NotNull Iterable<Double> iterable) {
        addAll(iterable);
        return this;
    }

    @Override
    default DoubleMutableListX plus(double @NotNull ... iterable) {
        addAll(iterable);
        return this;
    }

    PrimitiveListIterator.OfDouble listIterator();

    default PrimitiveIterator.OfDouble iterator() {
        return listIterator();
    }

    @Override
    default MutableListX<Double> boxed() {
        return asSequence().boxed().toMutableList();
    }

    double set(int index, double value);

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
