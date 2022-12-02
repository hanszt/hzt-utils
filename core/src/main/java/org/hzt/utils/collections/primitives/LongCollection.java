package org.hzt.utils.collections.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.LongCollectable;
import org.hzt.utils.iterables.primitives.LongGroupable;
import org.hzt.utils.iterables.primitives.LongNumerable;
import org.hzt.utils.iterables.primitives.LongReducable;
import org.hzt.utils.iterables.primitives.LongStreamable;
import org.hzt.utils.iterables.primitives.LongStringable;
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

public interface LongCollection extends
        LongReducable, LongCollectable, LongNumerable, LongStreamable, LongGroupable, LongStringable,
        PrimitiveCollection<Long, LongConsumer, long[]> {

    @Override
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

    default LongList filter(LongPredicate predicate) {
        var longs = LongMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextLong();
            if (predicate.test(value)) {
                longs.add(value);
            }
        }
        return longs;
    }

    default LongList map(LongUnaryOperator mapper) {
        var longs = LongMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextLong()));
        }
        return longs;
    }

    default IntList mapToInt(LongToIntFunction mapper) {
        var ints = IntMutableList.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextLong()));
        }
        return ints;
    }

    default DoubleList mapToDouble(LongToDoubleFunction mapper) {
        var doubles = DoubleMutableList.withInitCapacity(size());
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
    default LongList plus(@NotNull Iterable<Long> values) {
        var listX = toMutableList();
        listX.addAll(values);
        return listX;
    }

    @Override
    default LongList plus(long @NotNull ... array) {
        var listX = toMutableList();
        listX.addAll(array);
        return listX;
    }

    default LongSequence asSequence() {
        return LongSequence.of(this);
    }

    @Override
    default Spliterator.OfLong spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.NONNULL);
    }

    @Override
    default PrimitiveCollection<Long, LongConsumer, long[]> take(long n) {
        return takeTo(() -> LongMutableList.withInitCapacity((int) n), n);
    }

    default LongList skip(long n) {
        PreConditions.require(n <= Integer.MAX_VALUE);
        return skipTo(() -> LongMutableList.withInitCapacity((int) (size() - n)), (int) n);
    }

    @Override
    long[] toArray();

    @Override
    default LongMutableList toMutableList() {
        return to(() -> LongMutableList.withInitCapacity(size()));
    }
}
