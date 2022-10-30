package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public interface IntMutableCollection extends IntCollection,
        PrimitiveMutableCollection<Integer, IntConsumer, int[], IntPredicate> {

    boolean add(int i);

    @Override
    default boolean addAll(@NotNull Iterable<Integer> iterable) {
        var allAdded = true;
        if (iterable instanceof PrimitiveIterable.OfInt) {
            final var iterator = ((PrimitiveIterable.OfInt) iterable).iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextInt());
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
        final var iterator = PrimitiveIterators.intArrayIterator(array);
        var allAdded = true;
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
        var allRemoved = true;
        if (iterable instanceof PrimitiveIterable.OfInt) {
            final var iterator = ((PrimitiveIterable.OfInt) iterable).iterator();
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
        var allRemoved = true;
        final var iterator = PrimitiveIterators.intArrayIterator(array);
        while (iterator.hasNext()) {
            final var removed = remove(iterator.nextInt());
            if (!removed) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeIf(@NotNull IntPredicate predicate) {
        var removed = false;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextInt();
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
