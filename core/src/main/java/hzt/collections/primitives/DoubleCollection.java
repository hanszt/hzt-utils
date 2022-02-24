package hzt.collections.primitives;

import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.iterables.primitives.DoubleCollectable;
import hzt.iterables.primitives.DoubleNumerable;
import hzt.iterables.primitives.DoubleReducable;
import hzt.iterables.primitives.DoubleStreamable;
import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;

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

    @Override
    default boolean containsAll(double @NotNull ... array) {
        return DoubleSequence.of(array).all(this::contains);
    }

    boolean contains(double o);

    default DoubleListX filter(DoublePredicate predicate) {
        DoubleMutableListX doubles = DoubleMutableListX.empty();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            final double value = iterator.nextDouble();
            if (predicate.test(value)) {
                doubles.add(value);
            }
        }
        return doubles;
    }

    default DoubleListX map(DoubleUnaryOperator mapper) {
        DoubleMutableListX doubles = DoubleMutableListX.empty();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsDouble(iterator.nextDouble()));
        }
        return doubles;
    }

    default IntListX mapToInt(DoubleToIntFunction mapper) {
        IntMutableListX ints = IntMutableListX.empty();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextDouble()));
        }
        return ints;
    }

    default LongListX mapToLong(DoubleToLongFunction mapper) {
        LongMutableListX longs = LongMutableListX.empty();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextDouble()));
        }
        return longs;
    }

    default <R> ListX<R> mapToObj(DoubleFunction<R> mapper) {
        MutableListX<R> doubles = MutableListX.empty();
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.apply(iterator.nextDouble()));
        }
        return doubles;
    }

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
