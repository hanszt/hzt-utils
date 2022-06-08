package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public interface IntMutableCollection extends IntCollection,
        PrimitiveMutableCollectionX<Integer, IntConsumer, IntPredicate, int[]>{

    boolean add(int i);

    @Override
    default boolean addAll(@NotNull Iterable<Integer> iterable) {
        if (iterable instanceof PrimitiveIterable.OfInt) {
            final var iterator = ((PrimitiveIterable.OfInt) iterable).iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextInt());
                if (!added) {
                    return false;
                }
            }
            return true;
        }
        for (int i : iterable) {
            final var added = add(i);
            if (!added) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean addAll(int @NotNull ... array) {
        final var iterator = PrimitiveIterators.intArrayIterator(array);
        while (iterator.hasNext()) {
            final var added = add(iterator.nextInt());
            if (!added) {
                return false;
            }
        }
        return true;
    }

    boolean remove(int i);

    @Override
    default boolean removeAll(@NotNull Iterable<Integer> iterable) {
        if (iterable instanceof PrimitiveIterable.OfInt) {
            final var iterator = ((PrimitiveIterable.OfInt) iterable).iterator();
            while (iterator.hasNext()) {
                final var removed = remove(iterator.nextInt());
                if (!removed) {
                    return false;
                }
            }
            return true;
        }
        for (int i : iterable) {
            final var removed = remove(i);
            if (!removed) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean removeAll(int @NotNull ... array) {
        final var iterator = PrimitiveIterators.intArrayIterator(array);
        while (iterator.hasNext()) {
            final var removed = remove(iterator.nextInt());
            if (!removed) {
                return false;
            }
        }
        return true;
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
    default IntMutableListX plus(@NotNull Iterable<Integer> iterable) {
        return (IntMutableListX) IntCollection.super.plus(iterable);
    }

    @Override
    default IntMutableListX plus(int @NotNull ... array) {
        return (IntMutableListX) IntCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Integer> boxed();

    @Override
    int[] toArray();
}
