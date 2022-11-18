package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.DoubleCollectable;
import org.hzt.utils.iterables.primitives.DoubleGroupable;
import org.hzt.utils.iterables.primitives.DoubleNumerable;
import org.hzt.utils.iterables.primitives.DoubleReducable;
import org.hzt.utils.iterables.primitives.DoubleStreamable;
import org.hzt.utils.iterables.primitives.DoubleStringable;
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

public interface DoubleCollection extends
        DoubleReducable, DoubleCollectable, DoubleNumerable, DoubleStreamable, DoubleGroupable, DoubleStringable,
        PrimitiveCollection<Double, DoubleConsumer, double[]> {

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

    default DoubleList filter(DoublePredicate predicate) {
        var doubles = DoubleMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextDouble();
            if (predicate.test(value)) {
                doubles.add(value);
            }
        }
        return doubles;
    }

    default DoubleList map(DoubleUnaryOperator mapper) {
        var doubles = DoubleMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsDouble(iterator.nextDouble()));
        }
        return doubles;
    }

    default IntList mapToInt(DoubleToIntFunction mapper) {
        var ints = IntMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextDouble()));
        }
        return ints;
    }

    default LongList mapToLong(DoubleToLongFunction mapper) {
        var longs = LongMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextDouble()));
        }
        return longs;
    }

    default <R> ListX<R> mapToObj(DoubleFunction<R> mapper) {
        MutableListX<R> doubles = MutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.apply(iterator.nextDouble()));
        }
        return doubles;
    }

    @Override
    default DoubleList plus(@NotNull Iterable<Double> values) {
        var list = toMutableList();
        list.addAll(values);
        return list;
    }

    @Override
    default DoubleList plus(double @NotNull ... array) {
        var list = toMutableList();
        list.addAll(array);
        return list;
    }

    default DoubleSequence asSequence() {
        return DoubleSequence.of(this);
    }

    @Override
    default Spliterator.OfDouble spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.NONNULL);
    }

    default DoubleList take(long n) {
        return takeTo(() -> DoubleMutableList.withInitCapacity((int) n), n);
    }

    @Override
    double[] toArray();

    @Override
    default DoubleMutableList toMutableList() {
        return to(() -> DoubleMutableList.withInitCapacity(size()));
    }
}
