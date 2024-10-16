package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;

import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;
import java.util.function.LongPredicate;

public interface LongMutableCollection extends LongCollection,
        PrimitiveMutableCollection<Long, LongConsumer, long[], LongPredicate> {

    boolean add(long l);

    @Override
    default boolean addAll(final Iterable<Long> iterable) {
        boolean allAdded= true;
        if (iterable instanceof PrimitiveIterable.OfLong) {
            final PrimitiveIterator.OfLong iterator = ((PrimitiveIterable.OfLong) iterable).iterator();
            while (iterator.hasNext()) {
                final boolean added = add(iterator.nextLong());
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
    default boolean addAll(final long... array) {
        final PrimitiveIterator.OfLong iterator = PrimitiveIterators.longArrayIterator(array);
        boolean allAdded = true;
        while (iterator.hasNext()) {
            if (!add(iterator.nextLong())) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    boolean remove(long l);

    @Override
    default boolean removeAll(final Iterable<Long> iterable) {
        boolean allRemoved = true;
        if (iterable instanceof PrimitiveIterable.OfLong) {
            final PrimitiveIterator.OfLong iterator = ((PrimitiveIterable.OfLong) iterable).iterator();
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
    default boolean removeAll(final long... array) {
        boolean allRemoved = true;
        final PrimitiveIterator.OfLong iterator = PrimitiveIterators.longArrayIterator(array);
        while (iterator.hasNext()) {
            final boolean removed = remove(iterator.nextLong());
            if (!removed) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeIf(final LongPredicate predicate) {
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
    default LongMutableList plus(final Iterable<Long> iterable) {
        return (LongMutableList) LongCollection.super.plus(iterable);
    }

    @Override
    default LongMutableList plus(final long... array) {
        return (LongMutableList) LongCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Long> boxed();
}
