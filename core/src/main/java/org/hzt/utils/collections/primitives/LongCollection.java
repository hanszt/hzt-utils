package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.LongCollectable;
import org.hzt.utils.iterables.primitives.LongGroupable;
import org.hzt.utils.iterables.primitives.LongNumerable;
import org.hzt.utils.iterables.primitives.LongReducable;
import org.hzt.utils.iterables.primitives.LongStreamable;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;

public interface LongCollection extends LongReducable, LongCollectable, LongNumerable, LongStreamable, LongGroupable,
        PrimitiveCollectionX<Long, LongConsumer, long[]> {

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
    default boolean containsAll(@NotNull Iterable<Long> iterable) {
        return LongSequence.of(iterable).all(this::contains);
    }

    @Override
    default boolean containsAll(long @NotNull ... array) {
        return LongSequence.of(array).all(this::contains);
    }

    boolean contains(long l);

    default LongListX filter(LongPredicate predicate) {
        LongMutableListX longs = LongMutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextLong();
            if (predicate.test(value)) {
                longs.add(value);
            }
        }
        return longs;
    }

    default LongListX map(LongUnaryOperator mapper) {
        LongMutableListX longs = LongMutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextLong()));
        }
        return longs;
    }

    default IntListX mapToInt(LongToIntFunction mapper) {
        IntMutableListX ints = IntMutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextLong()));
        }
        return ints;
    }

    default DoubleListX mapToDouble(LongToDoubleFunction mapper) {
        DoubleMutableListX doubles = DoubleMutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsDouble(iterator.nextLong()));
        }
        return doubles;
    }

    default <R> ListX<R> mapToObj(LongFunction<R> mapper) {
        MutableListX<R> doubles = MutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.apply(iterator.nextLong()));
        }
        return doubles;
    }

    @Override
    default LongListX plus(@NotNull Iterable<Long> values) {
        LongMutableListX listX = toMutableList();
        listX.addAll(values);
        return listX;
    }

    @Override
    default LongListX plus(long @NotNull ... array) {
        LongMutableListX listX = toMutableList();
        listX.addAll(array);
        return listX;
    }

    default LongSequence asSequence() {
        return LongSequence.of(this);
    }

    @Override
    default Spliterator.OfLong spliterator() {
        final var array = toArray();
        return Spliterators.spliterator(array, 0, array.length,
                Spliterator.ORDERED | Spliterator.NONNULL);
    }

    @Override
    long[] toArray();

    @Override
    default LongMutableListX toMutableList() {
        return to(() -> LongMutableListX.withInitCapacity(size()));
    }
}
