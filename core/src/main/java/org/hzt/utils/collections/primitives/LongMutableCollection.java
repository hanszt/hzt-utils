package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.LongIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

public interface LongMutableCollection extends LongCollection,
        PrimitiveMutableCollectionX<Long, LongConsumer, LongPredicate, long[]> {

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
        final var iterator = PrimitiveIterators.longArrayIterator(array);
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
    default boolean removeIf(@NotNull LongPredicate predicate) {
        boolean removed = false;
        final PrimitiveIterator.OfLong iterator = iterator();
        while (iterator.hasNext()) {
            final long value = iterator.nextLong();
            if (predicate.test(value)) {
                removed = remove(value);
            }
        }
        return removed;
    }

    @Override
    default LongMutableListX plus(@NotNull Iterable<Long> iterable) {
        return (LongMutableListX) LongCollection.super.plus(iterable);
    }

    @Override
    default LongMutableListX plus(long @NotNull ... array) {
        return (LongMutableListX) LongCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Long> boxed();

    @Override
    long[] toArray();
}
