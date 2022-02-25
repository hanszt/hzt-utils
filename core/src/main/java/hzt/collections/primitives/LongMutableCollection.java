package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;
import hzt.iterables.primitives.LongIterable;
import hzt.iterators.primitives.PrimitiveIterators;
import hzt.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

public interface LongMutableCollection extends PrimitiveMutableCollectionX<Long, LongConsumer, long[]>, LongCollection {

    boolean add(long l);

    @Override
    default boolean addAll(@NotNull Iterable<Long> iterable) {
        if (iterable instanceof LongIterable) {
            final var iterator = ((LongIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextLong());
                if (!added) {
                    return false;
                }
            }
            return true;
        }
        for (long i : iterable) {
            final var added = add(i);
            if (!added) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean addAll(long @NotNull ... array) {
        final PrimitiveIterator.OfLong iterator = PrimitiveIterators.longArrayIterator(array);
        while (iterator.hasNext()) {
            final var added = add(iterator.nextLong());
            if (!added) {
                return false;
            }
        }
        return true;
    }

    boolean remove(long l);

    @Override
    default boolean removeAll(@NotNull Iterable<Long> iterable) {
        if (iterable instanceof LongIterable) {
            final var iterator = ((LongIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final var removed = remove(iterator.nextLong());
                if (!removed) {
                    return false;
                }
            }
            return true;
        }
        for (long i : iterable) {
            final var removed = remove(i);
            if (!removed) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean removeAll(long @NotNull ... array) {
        final var iterator = PrimitiveIterators.longArrayIterator(array);
        while (iterator.hasNext()) {
            final var removed = remove(iterator.nextLong());
            if (!removed) {
                return false;
            }
        }
        return true;
    }

    @Override
    default LongMutableListX plus(@NotNull Iterable<Long> iterable) {
        return LongSequence.of(this).plus(iterable).toMutableList();
    }

    @Override
    default LongMutableListX plus(long @NotNull ... array) {
        return LongSequence.of(this).plus(array).toMutableList();
    }

    @Override
    MutableCollectionX<Long> boxed();

    @Override
    long[] toArray();
}
