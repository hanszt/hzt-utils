package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.DoubleCollectable;
import org.hzt.utils.iterables.primitives.DoubleGroupable;
import org.hzt.utils.iterables.primitives.DoubleNumerable;
import org.hzt.utils.iterables.primitives.DoubleReducable;
import org.hzt.utils.iterables.primitives.DoubleStreamable;
import org.hzt.utils.iterables.primitives.DoubleStringable;
import org.hzt.utils.sequences.primitives.DoubleSequence;

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
    default boolean containsAll(final Iterable<Double> iterable) {
        return DoubleSequence.of(iterable).all(this::contains);
    }

    @Override
    default boolean containsAll(final double... array) {
        return DoubleSequence.of(array).all(this::contains);
    }

    boolean contains(double o);

    default DoubleList filter(final DoublePredicate predicate) {
        final var doubles = DoubleMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextDouble();
            if (predicate.test(value)) {
                doubles.add(value);
            }
        }
        return DoubleList.copyOf(doubles);
    }

    default DoubleList map(final DoubleUnaryOperator mapper) {
        final var mutableList = DoubleMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            mutableList.add(mapper.applyAsDouble(iterator.nextDouble()));
        }
        return DoubleList.copyOf(mutableList);
    }

    default IntList mapToInt(final DoubleToIntFunction mapper) {
        final var ints = IntMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextDouble()));
        }
        return IntList.copyOf(ints);
    }

    default LongList mapToLong(final DoubleToLongFunction mapper) {
        final var longs = LongMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextDouble()));
        }
        return LongList.copyOf(longs);
    }

    default <R> ListX<R> mapToObj(final DoubleFunction<R> mapper) {
        final MutableListX<R> list = MutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            list.add(mapper.apply(iterator.nextDouble()));
        }
        return ListX.copyOf(list);
    }

    @Override
    default DoubleList plus(final Iterable<Double> values) {
        final var list = toMutableList();
        list.addAll(values);
        return DoubleList.copyOf(list);
    }

    @Override
    default DoubleList plus(final double... array) {
        final var list = toMutableList();
        list.addAll(array);
        return DoubleList.copyOf(list);
    }

    default DoubleSequence asSequence() {
        return DoubleSequence.of(this);
    }

    @Override
    default Spliterator.OfDouble spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.NONNULL);
    }

    default DoubleList take(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return DoubleList.copyOf(takeTo(() -> DoubleMutableList.withInitCapacity((int) n), (int) n));
    }

    default DoubleList skip(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return DoubleList.copyOf(skipTo(() -> DoubleMutableList.withInitCapacity((int) (size() - n)), (int) n));
    }

    @Override
    double[] toArray();

    @Override
    default DoubleMutableList toMutableList() {
        return to(() -> DoubleMutableList.withInitCapacity(size()));
    }
}
