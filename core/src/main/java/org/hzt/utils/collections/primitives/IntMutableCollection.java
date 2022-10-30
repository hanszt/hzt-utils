package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public interface IntMutableCollection extends IntCollection,
        PrimitiveMutableCollection<Integer, IntConsumer, IntPredicate, int[]> {

    boolean add(int i);

    @Override
    default boolean addAll(@NotNull Iterable<Integer> iterable) {
        boolean allAdded = true;
        if (iterable instanceof PrimitiveIterable.OfInt) {
            final PrimitiveIterator.OfInt iterator = ((PrimitiveIterable.OfInt) iterable).iterator();
            while (iterator.hasNext()) {
                final boolean added = add(iterator.nextInt());
                if (!added) {
                    allAdded = false;
                }
            }
            return allAdded;
        }
        for (int i : iterable) {
            if (!add(i)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    default boolean addAll(int @NotNull ... array) {
        final PrimitiveIterator.OfInt iterator = PrimitiveIterators.intArrayIterator(array);
        boolean allAdded = true;
        while (iterator.hasNext()) {
            if (!add(iterator.nextInt())) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    boolean remove(int i);

    @Override
    default boolean removeAll(@NotNull Iterable<Integer> iterable) {
        boolean allRemoved = true;
        if (iterable instanceof PrimitiveIterable.OfInt) {
            final PrimitiveIterator.OfInt iterator = ((PrimitiveIterable.OfInt) iterable).iterator();
            while (iterator.hasNext()) {
                if (!remove(iterator.nextInt())) {
                    allRemoved = false;
                }
            }
            return allRemoved;
        }
        for (int i : iterable) {
            if (!remove(i)) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeAll(int @NotNull ... array) {
        boolean allRemoved = true;
        final PrimitiveIterator.OfInt iterator = PrimitiveIterators.intArrayIterator(array);
        while (iterator.hasNext()) {
            final boolean removed = remove(iterator.nextInt());
            if (!removed) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeIf(@NotNull IntPredicate predicate) {
        boolean removed = false;
        final PrimitiveIterator.OfInt iterator = iterator();
        while (iterator.hasNext()) {
            final int value = iterator.nextInt();
            if (predicate.test(value)) {
                removed = remove(value);
            }
        }
        return removed;
    }

    @Override
    default IntMutableList plus(@NotNull Iterable<Integer> iterable) {
        return (IntMutableList) IntCollection.super.plus(iterable);
    }

    @Override
    default IntMutableList plus(int @NotNull ... array) {
        return (IntMutableList) IntCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Integer> boxed();
}
