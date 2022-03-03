package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.DoubleCollectable;
import org.hzt.utils.iterables.primitives.DoubleNumerable;
import org.hzt.utils.iterables.primitives.DoubleReducable;
import org.hzt.utils.iterables.primitives.DoubleStreamable;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;

public interface DoubleCollection extends DoubleReducable, DoubleCollectable, DoubleNumerable, DoubleStreamable,
        PrimitiveCollectionX<Double, DoubleConsumer, double[]> {

    default int size() {
        return (int) count();
    }

    @Override
    default boolean isEmpty() {
        return none();
    }

    @Override
    default boolean isNotEmpty() {
        return any();
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
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextDouble();
            if (predicate.test(value)) {
                doubles.add(value);
            }
        }
        return doubles;
    }

    default DoubleListX map(DoubleUnaryOperator mapper) {
        DoubleMutableListX doubles = DoubleMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsDouble(iterator.nextDouble()));
        }
        return doubles;
    }

    default IntListX mapToInt(DoubleToIntFunction mapper) {
        IntMutableListX ints = IntMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextDouble()));
        }
        return ints;
    }

    default LongListX mapToLong(DoubleToLongFunction mapper) {
        LongMutableListX longs = LongMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextDouble()));
        }
        return longs;
    }

    default <R> ListX<R> mapToObj(DoubleFunction<R> mapper) {
        MutableListX<R> doubles = MutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.apply(iterator.nextDouble()));
        }
        return doubles;
    }

    @Override
    default DoubleListX plus(@NotNull Iterable<Double> values) {
        DoubleMutableListX list = DoubleMutableListX.of(this);
        list.addAll(values);
        return list;
    }

    @Override
    default DoubleListX plus(double @NotNull ... array) {
        DoubleMutableListX list = DoubleMutableListX.of(this);
        list.addAll(array);
        return list;
    }

    default DoubleSequence asSequence() {
        return DoubleSequence.of(this);
    }

    @Override
    default Spliterator.OfDouble spliterator() {
        final var array = toArray();
        return Spliterators.spliterator(array, 0, array.length,
                Spliterator.ORDERED | Spliterator.NONNULL);
    }

    @Override
    double[] toArray();
}
