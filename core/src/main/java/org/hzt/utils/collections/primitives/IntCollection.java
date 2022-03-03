package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.iterables.primitives.IntCollectable;
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

public interface IntCollection extends IntReducable, IntCollectable, IntNumerable, IntStreamable,
        PrimitiveCollectionX<Integer, IntConsumer, int[]> {

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
    default boolean containsAll(@NotNull Iterable<Integer> iterable) {
        return IntSequence.of(iterable).all(this::contains);
    }

    @Override
    default boolean containsAll(int @NotNull ... array) {
        return IntSequence.of(array).all(this::contains);
    }

    boolean contains(int i);

    default IntListX filter(IntPredicate predicate) {
        IntMutableListX doubles = IntMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextInt();
            if (predicate.test(value)) {
                doubles.add(value);
            }
        }
        return doubles;
    }

    default IntListX map(IntUnaryOperator mapper) {
        IntMutableListX doubles = IntMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsInt(iterator.nextInt()));
        }
        return doubles;
    }

    default LongListX mapToLong(IntToLongFunction mapper) {
        LongMutableListX longs = LongMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextInt()));
        }
        return longs;
    }

    default DoubleListX mapToDouble(IntToDoubleFunction mapper) {
        DoubleMutableListX doubles = DoubleMutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsDouble(iterator.nextInt()));
        }
        return doubles;
    }

    default <R> ListX<R> mapToObj(IntFunction<R> mapper) {
        MutableListX<R> doubles = MutableListX.empty();
        final var iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.apply(iterator.nextInt()));
        }
        return doubles;
    }

    @Override
    default IntListX plus(@NotNull Iterable<Integer> values) {
        IntMutableListX list = IntMutableListX.empty();
        list.addAll(values);
        return list;
    }

    @Override
    default IntListX plus(int @NotNull ... array) {
        IntMutableListX list = IntMutableListX.empty();
        list.addAll(array);
        return list;
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
    int[] toArray();
}