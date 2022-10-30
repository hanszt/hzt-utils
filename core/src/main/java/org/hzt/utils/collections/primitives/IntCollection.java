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

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;

public interface IntCollection extends IntReducable, IntCollectable, IntNumerable, IntStreamable, IntGroupable,
        PrimitiveCollection<Integer, IntConsumer, int[]> {

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

    default IntList filter(IntPredicate predicate) {
        IntMutableList ints = IntMutableList.withInitCapacity(size());
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            final int value = iterator.nextInt();
            if (predicate.test(value)) {
                ints.add(value);
            }
        }
        return ints;
    }

    default IntList map(IntUnaryOperator mapper) {
        IntMutableList ints = IntMutableList.withInitCapacity(size());
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            ints.add(mapper.applyAsInt(iterator.nextInt()));
        }
        return ints;
    }

    default LongList mapToLong(IntToLongFunction mapper) {
        LongMutableList longs = LongMutableList.withInitCapacity(size());
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            longs.add(mapper.applyAsLong(iterator.nextInt()));
        }
        return longs;
    }

    default DoubleList mapToDouble(IntToDoubleFunction mapper) {
        DoubleMutableList doubles = DoubleMutableList.withInitCapacity(size());
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            doubles.add(mapper.applyAsDouble(iterator.nextInt()));
        }
        return doubles;
    }

    default <R> ListX<R> mapToObj(IntFunction<R> mapper) {
        MutableListX<R> listX = MutableListX.withInitCapacity(size());
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            listX.add(mapper.apply(iterator.nextInt()));
        }
        return listX;
    }

    @Override
    default IntList plus(@NotNull Iterable<Integer> values) {
        IntMutableList list = toMutableList();
        list.addAll(values);
        return list;
    }

    @Override
    default IntList plus(int @NotNull ... array) {
        IntMutableList list = toMutableList();
        list.addAll(array);
        return list;
    }

    @Override
    default IntList take(long n) {
        return takeTo(() -> IntMutableList.withInitCapacity((int) n), n);
    }

    default IntSequence asSequence() {
        return IntSequence.of(this);
    }

    @Override
    default Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(iterator(), size(), Spliterator.ORDERED | Spliterator.NONNULL);
    }

    @Override
    default IntMutableList toMutableList() {
        return to(() -> IntMutableList.withInitCapacity(size()));
    }

    @Override
    int[] toArray();
}
