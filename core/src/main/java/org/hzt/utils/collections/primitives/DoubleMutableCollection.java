package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

public interface DoubleMutableCollection extends DoubleCollection,
        PrimitiveMutableCollection<Double, DoubleConsumer, double[], DoublePredicate> {

    boolean add(double d);

    @Override
    default boolean addAll(@NotNull final Iterable<Double> iterable) {
        var allAdded = true;
        if (iterable instanceof PrimitiveIterable.OfDouble) {
            final var iterator = ((PrimitiveIterable.OfDouble) iterable).iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextDouble());
                if (!added) {
                    allAdded = false;
                }
            }
            return allAdded;
        }
        for (final double i : iterable) {
            if (!add(i)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    default boolean addAll(final double @NotNull ... array) {
        final var iterator = PrimitiveIterators.doubleArrayIterator(array);
        var allAdded = true;
        while (iterator.hasNext()) {
            if (!add(iterator.nextDouble())) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    boolean remove(double d);

    @Override
    default boolean removeAll(@NotNull final Iterable<Double> iterable) {
        var allRemoved = true;
        if (iterable instanceof PrimitiveIterable.OfDouble) {
            final var iterator = ((PrimitiveIterable.OfDouble) iterable).iterator();
            while (iterator.hasNext()) {
                if (!remove(iterator.nextDouble())) {
                    allRemoved = false;
                }
            }
            return allRemoved;
        }
        for (final double d : iterable) {
            if (!remove(d)) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeAll(final double @NotNull ... array) {
        var allRemoved = true;
        final var iterator = PrimitiveIterators.doubleArrayIterator(array);
        while (iterator.hasNext()) {
            final var removed = remove(iterator.nextDouble());
            if (!removed) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeIf(@NotNull final DoublePredicate predicate) {
        var removed = false;
        final var iterator = iterator();
        while (iterator.hasNext()) {
            final var value = iterator.nextDouble();
            if (predicate.test(value)) {
                removed = remove(value);
            }
        }
        return removed;
    }

    @Override
    default DoubleMutableList plus(@NotNull final Iterable<Double> iterable) {
        return (DoubleMutableList) DoubleCollection.super.plus(iterable);
    }

    @Override
    default DoubleMutableList plus(final double @NotNull ... array) {
        return (DoubleMutableList) DoubleCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Double> boxed();
}
