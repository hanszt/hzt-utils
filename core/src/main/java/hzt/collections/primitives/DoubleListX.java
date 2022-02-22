package hzt.collections.primitives;

import hzt.collections.ListX;
import hzt.iterables.primitives.PrimitiveSortable;
import hzt.numbers.DoubleX;
import hzt.utils.primitive_comparators.DoubleComparator;

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

    int indexOf(double d);

    @Override
    double[] toArray();

    @Override
    default ListX<Double> boxed() {
        return asSequence().boxed().toListX();
    }

    @Override
    default DoubleListX sorted() {
        return asSequence().sorted().toListX();
    }

    @Override
    default DoubleListX sorted(DoubleComparator comparator) {
        return asSequence().sorted(comparator).toListX();
    }

    @Override
    default DoubleListX sortedDescending() {
        return sorted(DoubleX::compareReversed);
    }
}
