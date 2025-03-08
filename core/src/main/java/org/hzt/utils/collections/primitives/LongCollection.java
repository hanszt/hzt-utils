package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.primitives.LongCollectable;
import org.hzt.utils.iterables.primitives.LongGroupable;
import org.hzt.utils.iterables.primitives.LongNumerable;
import org.hzt.utils.iterables.primitives.LongReducable;
import org.hzt.utils.iterables.primitives.LongStreamable;
import org.hzt.utils.iterables.primitives.LongStringable;
import org.hzt.utils.iterables.primitives.PrimitiveIterableX;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.util.Collection;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;

public interface LongCollection extends
        LongReducable, LongCollectable, LongNumerable, LongStreamable, LongGroupable, LongStringable,
        PrimitiveIterableX<Long, LongConsumer, LongUnaryOperator, LongPredicate, LongBinaryOperator>,
        PrimitiveCollection<Long, LongConsumer, long[]> {

    @Override
    default boolean isEmpty() {
        return none();
    }

    @Override
    default boolean isNotEmpty() {
        return any();
    }

    @Override
    default boolean containsAll(final Iterable<Long> iterable) {
        return LongSequence.of(iterable).all(this::contains);
    }

    @Override
    default boolean containsAll(final long... array) {
        return LongSequence.of(array).all(this::contains);
    }

    boolean contains(long l);

    default LongList filter(final LongPredicate predicate) {
        return filterIf(true, predicate);
    }

    @Override
    default LongList filterNot(LongPredicate predicate) {
        return filterIf(false, predicate);
    }

    private LongList filterIf(final boolean sendWhen, final LongPredicate predicate) {
        return LongList.build(size(), ml -> {
            final var iterator = iterator();
            while (iterator.hasNext()) {
                final var value = iterator.nextLong();
                if (predicate.test(value) == sendWhen) {
                    ml.add(value);
                }
            }
        });
    }

    @Override
    CollectionX<Long> boxed();

    default LongList map(final LongUnaryOperator mapper) {
        final var iterator = iterator();
        return LongList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsLong(iterator.nextLong()));
            }
        });
    }

    default IntList mapToInt(final LongToIntFunction mapper) {
        final var iterator = iterator();
        return IntList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsInt(iterator.nextLong()));
            }
        });
    }

    default DoubleList mapToDouble(final LongToDoubleFunction mapper) {
        final var iterator = iterator();
        return DoubleList.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.applyAsDouble(iterator.nextLong()));
            }
        });
    }

    default <R> ListX<R> mapToObj(final LongFunction<R> mapper) {
        final var iterator = iterator();
        return ListX.build(size(), ml -> {
            while (iterator.hasNext()) {
                ml.add(mapper.apply(iterator.nextLong()));
            }
        });
    }

    @Override
    default LongList plus(final Iterable<Long> values) {
        final var listX = toMutableList();
        listX.addAll(values);
        return LongList.copyOf(listX);
    }

    @Override
    default LongList plus(final long... array) {
        final var list = toMutableList();
        list.addAll(array);
        return list;
    }

    default LongSequence asSequence() {
        return LongSequence.of(this);
    }

    @Override
    default Spliterator.OfLong spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.NONNULL);
    }

    @Override
    default LongList take(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return LongList.build((int) n, ml -> takeTo(() -> ml, n));
    }

    default LongList skip(final long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return LongList.copyOf(skipTo(() -> LongMutableList.withInitCapacity((int) (size() - n)), (int) n));
    }

    @Override
    default LongList distinct() {
        return distinctBy(e -> e);
    }

    @Override
    default LongList distinctBy(LongUnaryOperator selector) {
        return asSequence().distinctBy(selector).toList();
    }

    @Override
    default LongList minus(Iterable<Long> values) {
        return switch (values) {
            case Collection<Long> c -> minusSized(c.size(), values);
            case CollectionX<Long> c -> minusSized(c.size(), values);
            case LongCollection c -> minusSized(c.size(), values);
            default -> LongList.build(ml -> {
                final var set = LongSet.build(ms -> fillSet(values, ms));
                listMinusSet(ml, iterator(), set);
            });
        };
    }

    private LongList minusSized(final int size, final Iterable<Long> values) {
        final var set = LongSet.build(size, ms -> fillSet(values, ms));
        final var iterator = iterator();
        return LongList.build(size() - size, ml -> listMinusSet(ml, iterator, set));
    }

    private static void listMinusSet(final LongMutableList ml, final PrimitiveIterator.OfLong iterator, final LongSet set) {
        while (iterator.hasNext()) {
            final var i = iterator.nextLong();
            if (!set.contains(i)) {
                ml.add(i);
            }
        }
    }

    private static void fillSet(final Iterable<Long> values, final LongMutableSet ms) {
        final var iter = values.iterator();
        if (iter instanceof PrimitiveIterator.OfLong pi) {
            while (pi.hasNext()) {
                ms.add(pi.nextLong());
            }
        } else {
            for (final var value : values) {
                ms.add(value);
            }
        }
    }

    @Override
    default LongList takeWhile(LongPredicate predicate) {
        return asSequence().takeWhile(predicate).toList();
    }

    @Override
    default LongList takeWhileInclusive(LongPredicate predicate) {
        return asSequence().takeWhileInclusive(predicate).toList();
    }

    @Override
    default LongList skipWhile(LongPredicate predicate) {
        return asSequence().skipWhile(predicate).toList();
    }

    @Override
    default LongList skipWhileInclusive(LongPredicate predicate) {
        return asSequence().skipWhileInclusive(predicate).toList();
    }

    @Override
    default LongList onEach(LongConsumer consumer) {
        return map(e -> {
            consumer.accept(e);
            return e;
        });
    }

    @Override
    default LongList zip(LongBinaryOperator merger, Iterable<Long> other) {
        return asSequence().zip(merger, other).toList();
    }

    @Override
    default LongList zipWithNext(LongBinaryOperator merger) {
        return asSequence().zipWithNext(merger).toList();
    }

    @Override
    long[] toArray();

    @Override
    default LongMutableList toMutableList() {
        return to(() -> LongMutableList.withInitCapacity(size()));
    }
}
