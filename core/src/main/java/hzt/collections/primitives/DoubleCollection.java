package hzt.collections.primitives;

import hzt.iterables.primitives.DoubleCollectable;
import hzt.iterables.primitives.DoubleNumerable;
import hzt.iterables.primitives.DoubleReducable;
import hzt.iterables.primitives.DoubleStreamable;
import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleConsumer;

public interface DoubleCollection extends DoubleReducable, DoubleCollectable, DoubleNumerable, DoubleStreamable,
        PrimitiveCollectionX<Double, DoubleConsumer, double[]> {

    default int size() {
        return (int) asSequence().count();
    }

    @Override
    default boolean isEmpty() {
        return asSequence().none();
    }

    @Override
    default boolean isNotEmpty() {
        return asSequence().any();
    }

    @Override
    default boolean contains(Double o) {
        return contains((double) o);
    }

    @Override
    default boolean containsAll(@NotNull Iterable<Double> iterable) {
        return DoubleSequence.of(iterable).all(this::contains);
    }

    boolean contains(double o);

    @Override
    default DoubleListX plus(@NotNull Iterable<Double> values) {
        return DoubleListX.of(asSequence().plus(values));
    }

    default DoubleSequence asSequence() {
        return DoubleSequence.of(this);
    }

    @Override
    double[] toArray();
}
