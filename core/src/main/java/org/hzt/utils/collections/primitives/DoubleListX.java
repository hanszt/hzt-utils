package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveSort;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.PrimitiveSortable;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.primitive_comparators.DoubleComparator;

import java.util.Arrays;
import java.util.function.Consumer;

public interface DoubleListX extends DoubleCollection, PrimitiveSortable<DoubleComparator> {

    static DoubleListX empty() {
        return new DoubleArrayList();
    }

    static DoubleListX of(Iterable<Double> iterable) {
        return new DoubleArrayList(iterable);
    }

    static DoubleListX of(DoubleListX doubleListX) {
        return new DoubleArrayList(doubleListX);
    }

    static DoubleListX of(double... array) {
        return new DoubleArrayList(array);
    }

    static DoubleListX build(Consumer<DoubleMutableListX> factory) {
        final DoubleMutableListX listX = DoubleMutableListX.empty();
        factory.accept(listX);
        return listX;
    }

    default boolean contains(double o) {
        return indexOf(o) >= 0;
    }

    double get(int index);

    int indexOf(double d);

    int lastIndexOf(double d);

    @Override
    default ListX<Double> boxed() {
        return asSequence().boxed().toListX();
    }

    @Override
    default DoubleListX sorted() {
        final var array = toArray();
        Arrays.sort(array);
        return DoubleListX.of(array);
    }

    @Override
    default DoubleListX sorted(DoubleComparator comparator) {
        final var array = toArray();
        PrimitiveSort.sort(array, comparator);
        return DoubleListX.of(array);
    }

    @Override
    default DoubleListX sortedDescending() {
        return sorted(DoubleX::compareReversed);
    }
}
