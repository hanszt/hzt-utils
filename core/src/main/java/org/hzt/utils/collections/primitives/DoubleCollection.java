package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.Sizable;
import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.DoubleCollectable;
import org.hzt.utils.iterables.primitives.DoubleGroupable;
import org.hzt.utils.iterables.primitives.DoubleNumerable;
import org.hzt.utils.iterables.primitives.DoubleReducable;
import org.hzt.utils.iterables.primitives.DoubleStreamable;
import org.hzt.utils.iterables.primitives.DoubleStringable;
import org.hzt.utils.iterables.primitives.PrimitiveIterableX;
import org.hzt.utils.sequences.primitives.DoubleSequence;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;

public interface DoubleCollection extends
        DoubleReducable, DoubleCollectable, DoubleNumerable, DoubleStreamable, DoubleGroupable, DoubleStringable, Sizable,
        PrimitiveIterableX<Double, DoubleConsumer, DoubleUnaryOperator, DoublePredicate, DoubleBinaryOperator>,
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
        return filterIf(true, predicate);
    }

    @Override
    default DoubleList filterNot(DoublePredicate predicate) {
        return filterIf(false, predicate);
    }

    private DoubleList filterIf(final boolean sendWhen, final DoublePredicate predicate) {
        return DoubleList.build(size(), ml -> {
            final var iterator = iterator();
            while (iterator.hasNext()) {
                final var value = iterator.nextDouble();
                if (predicate.test(value) == sendWhen) {
                    ml.add(value);
                }
            }
        });
    }

    @Override
    CollectionX<Double> boxed();

    default DoubleList map(final DoubleUnaryOperator mapper) {
        final var iterator = iterator();
        return DoubleList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsDouble(iterator.nextDouble()));
            }
        });
    }

    default IntList mapToInt(final DoubleToIntFunction mapper) {
        final var iterator = iterator();
        return IntList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsInt(iterator.nextDouble()));
            }
        });
    }

    default LongList mapToLong(final DoubleToLongFunction mapper) {
        final var iterator = iterator();
        return LongList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsLong(iterator.nextDouble()));
            }
        });
    }

    default <R> ListX<R> mapToObj(final DoubleFunction<R> mapper) {
        final var iterator = iterator();
        return ListX.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.apply(iterator.nextDouble()));
            }
        });
    }

    @Override
    default DoubleList plus(final Iterable<Double> values) {
        final var otherSizeOrZero = switch (values) {
            case Collection<Double> c -> c.size();
            case Sizable c -> c.size();
            default -> 0;
        };
        return DoubleList.build(size() + otherSizeOrZero, ml -> {
            ml.addAll(this);
            ml.addAll(values);
        });
    }

    @Override
    default DoubleList plus(final double... array) {
        return DoubleList.build(size() + array.length, ml -> {
           ml.addAll(this);
           ml.addAll(array);
        });
    }

    default DoubleSequence asSequence() {
        return DoubleSequence.of(this);
    }

    @Override
    default Spliterator.OfDouble spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.NONNULL);
    }

    @Override
    default DoubleList take(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return DoubleList.build((int) n, ml -> takeTo(() -> ml, (int) n));
    }

    default DoubleList skip(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return DoubleList.build((int) (size() - n), ml ->  skipTo(() -> ml, (int) n));
    }

    @Override
    default DoubleList distinct() {
        return distinctBy(e -> e);
    }

    @Override
    default DoubleList distinctBy(DoubleUnaryOperator selector) {
        return asSequence().distinctBy(selector).toList();
    }

    @Override
    default DoubleList minus(Iterable<Double> values) {
        return switch (values) {
            case Collection<Double> c -> minusSized(c.size(), values);
            case Sizable c -> minusSized(c.size(), values);
            default -> DoubleList.build(ml -> {
                final var set = DoubleSet.build(ms -> ms.addAll(values));
                listMinusSet(ml, iterator(), set);
            });
        };
    }

    private DoubleList minusSized(final int size, final Iterable<Double> values) {
        final var set = DoubleSet.build(size, ms -> ms.addAll(values));
        final var iterator = iterator();
        return DoubleList.build(size() - size, ml -> listMinusSet(ml, iterator, set));
    }

    private static void listMinusSet(final DoubleMutableList ml, final PrimitiveIterator.OfDouble iterator, final DoubleSet set) {
        while (iterator.hasNext()) {
            final var i = iterator.nextDouble();
            if (!set.contains(i)) {
                ml.add(i);
            }
        }
    }

    @Override
    default DoubleList takeWhile(DoublePredicate predicate) {
        return asSequence().takeWhile(predicate).toList();
    }

    @Override
    default DoubleList takeWhileInclusive(DoublePredicate predicate) {
        return asSequence().takeWhileInclusive(predicate).toList();
    }

    @Override
    default DoubleList skipWhile(DoublePredicate predicate) {
        return asSequence().skipWhile(predicate).toList();
    }

    @Override
    default DoubleList skipWhileInclusive(DoublePredicate predicate) {
        return asSequence().skipWhileInclusive(predicate).toList();
    }

    @Override
    default DoubleList onEach(DoubleConsumer consumer) {
        return map(e -> {
            consumer.accept(e);
            return e;
        });
    }

    @Override
    default DoubleList zip(DoubleBinaryOperator merger, Iterable<Double> other) {
        return asSequence().zip(merger, other).toList();
    }

    @Override
    default DoubleList zipWithNext(DoubleBinaryOperator merger) {
        return asSequence().zipWithNext(merger).toList();
    }

    @Override
    double[] toArray();

    @Override
    default DoubleMutableList toMutableList() {
        return to(() -> DoubleMutableList.withInitCapacity(size()));
    }
}
