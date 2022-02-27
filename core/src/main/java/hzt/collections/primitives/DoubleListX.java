package hzt.collections.primitives;

import hzt.arrays.primitves.PrimitiveSort;
import hzt.collections.ListX;
import hzt.iterables.primitives.PrimitiveSortable;
import hzt.numbers.DoubleX;
import hzt.utils.primitive_comparators.DoubleComparator;

import java.util.Arrays;

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
        final double[] array = toArray();
        Arrays.sort(array);
        return DoubleListX.of(array);
    }

    @Override
    default DoubleListX sorted(DoubleComparator comparator) {
        final double[] array = toArray();
        PrimitiveSort.sort(array, comparator);
        return DoubleListX.of(array);
    }

    @Override
    default DoubleListX sortedDescending() {
        return sorted(DoubleX::compareReversed);
    }
}
