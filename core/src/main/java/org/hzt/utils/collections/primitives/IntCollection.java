package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.Sizable;
import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.IntCollectable;
import org.hzt.utils.iterables.primitives.IntGroupable;
import org.hzt.utils.iterables.primitives.IntNumerable;
import org.hzt.utils.iterables.primitives.IntReducable;
import org.hzt.utils.iterables.primitives.IntStreamable;
import org.hzt.utils.iterables.primitives.IntStringable;
import org.hzt.utils.iterables.primitives.PrimitiveIterableX;
import org.hzt.utils.sequences.primitives.IntSequence;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

public interface IntCollection extends
        IntReducable, IntCollectable, IntNumerable, IntStreamable, IntGroupable, IntStringable, Sizable,
        PrimitiveIterableX<Integer, IntConsumer, IntUnaryOperator, IntPredicate, IntBinaryOperator>,
        PrimitiveCollection<Integer, IntConsumer, int[]> {

    @Override
    default boolean isEmpty() {
        return none();
    }

    @Override
    default boolean isNotEmpty() {
        return any();
    }

    @Override
    default boolean containsAll(final Iterable<Integer> iterable) {
        return IntSequence.of(iterable).all(this::contains);
    }

    @Override
    default boolean containsAll(final int... array) {
        return IntSequence.of(array).all(this::contains);
    }

    boolean contains(int i);

    default IntList filter(final IntPredicate predicate) {
        return filterIf(true, predicate);
    }

    @Override
    default IntList filterNot(IntPredicate predicate) {
        return filterIf(false, predicate);
    }

    private IntList filterIf(final boolean sendWhen, final IntPredicate predicate) {
        return IntList.build(size(), ml -> {
            final var iterator = iterator();
            while (iterator.hasNext()) {
                final var value = iterator.nextInt();
                if (predicate.test(value) == sendWhen) {
                    ml.add(value);
                }
            }
        });
    }

    @Override
    CollectionX<Integer> boxed();

    default IntList map(final IntUnaryOperator mapper) {
        final var iterator = iterator();
        return IntList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsInt(iterator.nextInt()));
            }
        });
    }

    default LongList mapToLong(final IntToLongFunction mapper) {
        final var iterator = iterator();
        return LongList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsLong(iterator.nextInt()));
            }
        });
    }

    default DoubleList mapToDouble(final IntToDoubleFunction mapper) {
        final var iterator = iterator();
        return DoubleList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsDouble(iterator.nextInt()));
            }
        });
    }

    default <R> ListX<R> mapToObj(final IntFunction<R> mapper) {
        final var iterator = iterator();
        return ListX.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.apply(iterator.nextInt()));
            }
        });
    }

    @Override
    default IntList plus(final Iterable<Integer> values) {
        final var otherSizeOrZero = switch (values) {
            case Collection<Integer> c -> c.size();
            case Sizable c -> c.size();
            default -> 0;
        };
        return IntList.build(size() + otherSizeOrZero, ml -> {
            ml.addAll(this);
            ml.addAll(values);
        });
    }

    @Override
    default IntList plus(final int... array) {
        return IntList.build(size() + array.length, ml -> {
            ml.addAll(this);
            ml.addAll(array);
        });
    }

    default IntSequence asSequence() {
        return IntSequence.of(this);
    }

    @Override
    default Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.NONNULL);
    }

    @Override
    default IntList take(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return IntList.build((int) n, ml -> takeTo(() -> ml, n));
    }

    default IntList skip(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return IntList.build((int) (size() - n), ml -> skipTo(() -> ml, (int) n));
    }

    @Override
    default IntList distinct() {
        return distinctBy(e -> e);
    }

    @Override
    default IntList distinctBy(IntUnaryOperator selector) {
        return asSequence().distinctBy(selector).toList();
    }

    @Override
    default IntList minus(Iterable<Integer> values) {
        return switch (values) {
            case Collection<Integer> c -> minusSized(c.size(), values);
            case Sizable c -> minusSized(c.size(), values);
            default -> IntList.build(ml -> {
                final var set = IntSet.build(ms -> ms.addAll(values));
                listMinusSet(ml, iterator(), set);
            });
        };
    }

    private IntList minusSized(final int size, final Iterable<Integer> values) {
        final var set = IntSet.build(size, ms -> ms.addAll(values));
        final var iterator = iterator();
        return IntList.build(size() - size, ml -> listMinusSet(ml, iterator, set));
    }

    private static void listMinusSet(final IntMutableList ml, final PrimitiveIterator.OfInt iterator, final IntSet set) {
        while (iterator.hasNext()) {
            final var i = iterator.nextInt();
            if (!set.contains(i)) {
                ml.add(i);
            }
        }
    }

    @Override
    default IntList takeWhile(IntPredicate predicate) {
        return asSequence().takeWhile(predicate).toList();
    }

    @Override
    default IntList takeWhileInclusive(IntPredicate predicate) {
        return asSequence().takeWhileInclusive(predicate).toList();
    }

    @Override
    default IntList skipWhile(IntPredicate predicate) {
        return asSequence().skipWhile(predicate).toList();
    }

    @Override
    default IntList skipWhileInclusive(IntPredicate predicate) {
        return asSequence().skipWhileInclusive(predicate).toList();
    }

    @Override
    default IntList onEach(IntConsumer consumer) {
        return map(e -> {
            consumer.accept(e);
            return e;
        });
    }

    @Override
    default IntList zip(IntBinaryOperator merger, Iterable<Integer> other) {
        return asSequence().zip(merger, other).toList();
    }

    @Override
    default IntList zipWithNext(IntBinaryOperator merger) {
        return asSequence().zipWithNext(merger).toList();
    }

    @Override
    int[] toArray();

    @Override
    default IntMutableList toMutableList() {
        return to(() -> IntMutableList.withInitCapacity(size()));
    }
}
