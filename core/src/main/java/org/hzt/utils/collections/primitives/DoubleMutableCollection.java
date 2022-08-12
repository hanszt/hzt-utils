package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

public interface DoubleMutableCollection extends DoubleCollection,
        PrimitiveMutableCollection<Double, DoubleConsumer, DoublePredicate, double[]> {

    boolean add(double d);

    @Override
    default boolean addAll(@NotNull Iterable<Double> iterable) {
        boolean allAdded = true;
        if (iterable instanceof PrimitiveIterable.OfDouble doubleIterable) {
            final var iterator = doubleIterable.iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextDouble());
                if (!added) {
                    allAdded = false;
                }
            }
            return allAdded;
        }
        for (double i : iterable) {
            if (!add(i)) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    @Override
    default boolean addAll(double @NotNull ... array) {
        final var iterator = PrimitiveIterators.doubleArrayIterator(array);
        boolean allAdded = true;
        while (iterator.hasNext()) {
            if (!add(iterator.nextDouble())) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    boolean remove(double d);

    @Override
    default boolean removeAll(@NotNull Iterable<Double> iterable) {
        boolean allRemoved = true;
        if (iterable instanceof PrimitiveIterable.OfDouble doubleIterable) {
            final var iterator = doubleIterable.iterator();
            while (iterator.hasNext()) {
                if (!remove(iterator.nextDouble())) {
                    allRemoved = false;
                }
            }
            return allRemoved;
        }
        for (double d : iterable) {
            if (!remove(d)) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeAll(double @NotNull ... array) {
        boolean allRemoved = true;
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
    default boolean removeIf(@NotNull DoublePredicate predicate) {
        boolean removed = false;
        final PrimitiveIterator.OfDouble iterator = iterator();
        while (iterator.hasNext()) {
            final double value = iterator.nextDouble();
            if (predicate.test(value)) {
                removed = remove(value);
            }
        }
        return removed;
    }

    @Override
    default DoubleMutableList plus(@NotNull Iterable<Double> iterable) {
        return (DoubleMutableList) DoubleCollection.super.plus(iterable);
    }

    @Override
    default DoubleMutableList plus(double @NotNull ... array) {
        return (DoubleMutableList) DoubleCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Double> boxed();
}
