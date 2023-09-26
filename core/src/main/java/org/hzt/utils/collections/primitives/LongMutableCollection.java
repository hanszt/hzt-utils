package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

public interface LongMutableCollection extends LongCollection,
        PrimitiveMutableCollection<Long, LongConsumer, long[], LongPredicate> {

    boolean add(long l);

    @Override
    default boolean addAll(@NotNull final Iterable<Long> iterable) {
        var allAdded= true;
        if (iterable instanceof PrimitiveIterable.OfLong) {
            final var iterator = ((PrimitiveIterable.OfLong) iterable).iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextLong());
                if (!added) {
                    allAdded = false;
                }
            }
            return allAdded;
        }
        for (final long i : iterable) {
            if (!add(i)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    default boolean addAll(final long @NotNull ... array) {
        final var iterator = PrimitiveIterators.longArrayIterator(array);
        var allAdded = true;
        while (iterator.hasNext()) {
            if (!add(iterator.nextLong())) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    boolean remove(long l);

    @Override
    default boolean removeAll(@NotNull final Iterable<Long> iterable) {
        var allRemoved = true;
        if (iterable instanceof PrimitiveIterable.OfLong) {
            final var iterator = ((PrimitiveIterable.OfLong) iterable).iterator();
            while (iterator.hasNext()) {
                if (!remove(iterator.nextLong())) {
                    allRemoved = false;
                }
            }
            return allRemoved;
        }
        for (final long i : iterable) {
            if (!remove(i)) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeAll(final long @NotNull ... array) {
        var allRemoved = true;
        final var iterator = PrimitiveIterators.longArrayIterator(array);
        while (iterator.hasNext()) {
            final var removed = remove(iterator.nextLong());
            if (!removed) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeIf(@NotNull final LongPredicate predicate) {
        var removed = false;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextLong();
            if (predicate.test(value)) {
                removed = remove(value);
            }
        }
        return removed;
    }

    @Override
    default LongMutableList plus(@NotNull final Iterable<Long> iterable) {
        return (LongMutableList) LongCollection.super.plus(iterable);
    }

    @Override
    default LongMutableList plus(final long @NotNull ... array) {
        return (LongMutableList) LongCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Long> boxed();
}
