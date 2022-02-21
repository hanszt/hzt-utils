package hzt.collections.primitives;

import hzt.collections.ListX;

public interface DoubleListX extends DoubleCollection {

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
}
