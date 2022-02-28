package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;
import hzt.iterables.primitives.LongIterable;
import hzt.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.LongConsumer;

public interface LongMutableCollection extends PrimitiveMutableCollectionX<Long, LongConsumer, long[]>, LongCollection {

    boolean add(long l);

    @Override
    default boolean addAll(@NotNull Iterable<Long> iterable) {
        if (iterable instanceof LongIterable) {
            final PrimitiveIterator.OfLong iterator = ((LongIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final boolean added = add(iterator.nextLong());
                if (!added) {
                    return false;
                }
            }
            return true;
        }
        for (long i : iterable) {
            final boolean added = add(i);
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
            final boolean added = add(iterator.nextLong());
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
            final PrimitiveIterator.OfLong iterator = ((LongIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final boolean removed = remove(iterator.nextLong());
                if (!removed) {
                    return false;
                }
            }
            return true;
        }
        for (long i : iterable) {
            final boolean removed = remove(i);
            if (!removed) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean removeAll(long @NotNull ... array) {
        final PrimitiveIterator.OfLong iterator = PrimitiveIterators.longArrayIterator(array);
        while (iterator.hasNext()) {
            final boolean removed = remove(iterator.nextLong());
            if (!removed) {
                return false;
            }
        }
        return true;
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
