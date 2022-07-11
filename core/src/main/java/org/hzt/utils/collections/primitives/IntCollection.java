package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.IntCollectable;
import org.hzt.utils.iterables.primitives.IntGroupable;
import org.hzt.utils.iterables.primitives.IntNumerable;
import org.hzt.utils.iterables.primitives.IntReducable;
import org.hzt.utils.iterables.primitives.IntStreamable;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

public interface IntCollection extends IntReducable, IntCollectable, IntNumerable, IntStreamable, IntGroupable,
        PrimitiveCollectionX<Integer, IntConsumer, int[]> {

    default boolean isEmpty() {
        return none();
    }

    default boolean isNotEmpty() {
        return any();
    }

    @Override
    default boolean containsAll(@NotNull Iterable<Integer> iterable) {
        return IntSequence.of(iterable).all(this::contains);
    }

    @Override
    default boolean containsAll(int @NotNull ... array) {
        return IntSequence.of(array).all(this::contains);
    }

    boolean contains(int i);

    default IntListX filter(IntPredicate predicate) {
        IntMutableListX ints = IntMutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextInt();
            if (predicate.test(value)) {
                ints.add(value);
            }
        }
        return ints;
    }

    default IntListX map(IntUnaryOperator mapper) {
        IntMutableListX ints = IntMutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextInt()));
        }
        return ints;
    }

    default LongListX mapToLong(IntToLongFunction mapper) {
        LongMutableListX longs = LongMutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextInt()));
        }
        return longs;
    }

    default DoubleListX mapToDouble(IntToDoubleFunction mapper) {
        DoubleMutableListX doubles = DoubleMutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsDouble(iterator.nextInt()));
        }
        return doubles;
    }

    default <R> ListX<R> mapToObj(IntFunction<R> mapper) {
        MutableListX<R> listX = MutableListX.withInitCapacity(size());
        final var iterator = iterator();
        while (iterator.hasNext()) {
            listX.add(mapper.apply(iterator.nextInt()));
        }
        return listX;
    }

    @Override
    default IntListX plus(@NotNull Iterable<Integer> values) {
        IntMutableListX list = toMutableList();
        list.addAll(values);
        return list;
    }

    @Override
    default IntListX plus(int @NotNull ... array) {
        IntMutableListX list = toMutableList();
        list.addAll(array);
        return list;
    }

    @Override
    default IntListX take(long n) {
        return takeTo(() -> IntMutableListX.withInitCapacity((int) n), n);
    }

    default IntSequence asSequence() {
        return IntSequence.of(this);
    }

    @Override
    default Spliterator.OfInt spliterator() {
        final var array = toArray();
        return Spliterators.spliterator(array, 0, array.length,
                Spliterator.ORDERED | Spliterator.NONNULL);
    }

    @Override
    default IntMutableListX toMutableList() {
        return to(() -> IntMutableListX.withInitCapacity(size()));
    }

    @Override
    int[] toArray();
}
