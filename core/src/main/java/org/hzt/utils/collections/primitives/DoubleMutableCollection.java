package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.DoubleIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

public interface DoubleMutableCollection extends DoubleCollection,
        PrimitiveMutableCollectionX<Double, DoubleConsumer, DoublePredicate, double[]> {

    boolean add(double d);

    @Override
    default boolean addAll(@NotNull Iterable<Double> iterable) {
        if (iterable instanceof DoubleIterable) {
            final var iterator = ((DoubleIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final var added = add(iterator.nextDouble());
                if (!added) {
                    return false;
                }
            }
            return true;
        }
        for (double i : iterable) {
            final var added = add(i);
            if (!added) {
                return false;
            }
        }
        return true;
    }

    boolean remove(double d);

    @Override
    default boolean addAll(double @NotNull ... array) {
        final var iterator = PrimitiveIterators.doubleArrayIterator(array);
        while (iterator.hasNext()) {
            final var added = add(iterator.nextDouble());
            if (!added) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean removeAll(@NotNull Iterable<Double> iterable) {
        if (iterable instanceof DoubleIterable) {
            final var iterator = ((DoubleIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final var removed = remove(iterator.nextDouble());
                if (!removed) {
                    return false;
                }
            }
            return true;
        }
        for (double d : iterable) {
            final var added = remove(d);
            if (!added) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean removeAll(double @NotNull ... array) {
        final var iterator = PrimitiveIterators.doubleArrayIterator(array);
        while (iterator.hasNext()) {
            final var removed = remove(iterator.nextDouble());
            if (!removed) {
                return false;
            }
        }
        return true;
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
    default DoubleMutableListX plus(@NotNull Iterable<Double> iterable) {
        return (DoubleMutableListX) DoubleCollection.super.plus(iterable);
    }

    @Override
    default DoubleMutableListX plus(double @NotNull ... array) {
        return (DoubleMutableListX) DoubleCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Double> boxed();
}
