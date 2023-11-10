package org.hzt.utils.collections.primitives;

import org.hzt.utils.collections.MutableCollectionX;
import org.hzt.utils.iterables.primitives.PrimitiveIterable;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;

public interface DoubleMutableCollection extends DoubleCollection,
        PrimitiveMutableCollection<Double, DoubleConsumer, double[], DoublePredicate> {

    boolean add(double d);

    @Override
    default boolean addAll(final Iterable<Double> iterable) {
        boolean allAdded = true;
        if (iterable instanceof PrimitiveIterable.OfDouble) {
            final PrimitiveIterator.OfDouble iterator = ((PrimitiveIterable.OfDouble) iterable).iterator();
            while (iterator.hasNext()) {
                final boolean added = add(iterator.nextDouble());
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
    default boolean addAll(final double... array) {
        final PrimitiveIterator.OfDouble iterator = PrimitiveIterators.doubleArrayIterator(array);
        boolean allAdded = true;
        while (iterator.hasNext()) {
            final boolean added = add(iterator.nextDouble());
            if (!added) {
                allAdded = false;
            }
        }
        return allAdded;
    }

    boolean remove(double d);

    @Override
    default boolean removeAll(final Iterable<Double> iterable) {
        boolean allRemoved = true;
        if (iterable instanceof PrimitiveIterable.OfDouble) {
            final PrimitiveIterator.OfDouble iterator = ((PrimitiveIterable.OfDouble) iterable).iterator();
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
    default boolean removeAll(final double... array) {
        boolean allRemoved = true;
        final PrimitiveIterator.OfDouble iterator = PrimitiveIterators.doubleArrayIterator(array);
        while (iterator.hasNext()) {
            final boolean removed = remove(iterator.nextDouble());
            if (!removed) {
                allRemoved = false;
            }
        }
        return allRemoved;
    }

    @Override
    default boolean removeIf(final DoublePredicate predicate) {
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
    default DoubleMutableList plus(final Iterable<Double> iterable) {
        return (DoubleMutableList) DoubleCollection.super.plus(iterable);
    }

    @Override
    default DoubleMutableList plus(final double... array) {
        return (DoubleMutableList) DoubleCollection.super.plus(array);
    }

    @Override
    MutableCollectionX<Double> boxed();
}
