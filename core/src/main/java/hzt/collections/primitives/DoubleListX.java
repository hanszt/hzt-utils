package hzt.collections.primitives;

import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleConsumer;

public interface DoubleListX extends PrimitiveCollectionX<Double, DoubleConsumer, double[]>, DoubleSequence {

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
    
    default int size() {
        return (int) count();
    }

    default boolean isEmpty() {
        return none();
    }

    default boolean isNotEmpty() {
        return any();
    }

    @Override
    default boolean contains(Double o) {
        return contains((double) o);
    }

    @Override
    default boolean containsAll(@NotNull Iterable<Double> iterable) {
        return DoubleSequence.of(iterable).all(this::contains);
    }

    default boolean contains(double o) {
        return indexOf(o) >= 0;
    }

    int indexOf(double d);

    @Override
    double[] toArray();
}
