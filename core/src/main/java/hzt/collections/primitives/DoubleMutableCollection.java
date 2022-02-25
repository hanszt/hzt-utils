package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;
import hzt.iterables.primitives.DoubleIterable;
import hzt.iterators.primitives.PrimitiveIterators;
import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

public interface DoubleMutableCollection extends PrimitiveMutableCollectionX<Double, DoubleConsumer, double[]>, DoubleCollection {

    boolean add(double d);

    @Override
    default boolean addAll(@NotNull Iterable<Double> iterable) {
        if (iterable instanceof DoubleIterable) {
            final PrimitiveIterator.OfDouble iterator = ((DoubleIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final boolean added = add(iterator.nextDouble());
                if (!added) {
                    return false;
                }
            }
            return true;
        }
        for (double i : iterable) {
            final boolean added = add(i);
            if (!added) {
                return false;
            }
        }
        return true;
    }

    boolean remove(double d);

    @Override
    default boolean addAll(double @NotNull ... array) {
        final PrimitiveIterator.OfDouble iterator = PrimitiveIterators.doubleArrayIterator(array);
        while (iterator.hasNext()) {
            final boolean added = add(iterator.nextDouble());
            if (!added) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean removeAll(@NotNull Iterable<Double> iterable) {
        if (iterable instanceof DoubleIterable) {
            final PrimitiveIterator.OfDouble iterator = ((DoubleIterable) iterable).iterator();
            while (iterator.hasNext()) {
                final boolean removed = remove(iterator.nextDouble());
                if (!removed) {
                    return false;
                }
            }
            return true;
        }
        for (double d : iterable) {
            final boolean added = remove(d);
            if (!added) {
                return false;
            }
        }
        return true;
    }

    @Override
    default boolean removeAll(double @NotNull ... array) {
        final PrimitiveIterator.OfDouble iterator = PrimitiveIterators.doubleArrayIterator(array);
        while (iterator.hasNext()) {
            final boolean removed = remove(iterator.nextDouble());
            if (!removed) {
                return false;
            }
        }
        return true;
    }

    @Override
    default DoubleMutableListX plus(@NotNull Iterable<Double> iterable) {
        return DoubleSequence.of(this).plus(iterable).toMutableList();
    }

    @Override
    default DoubleMutableListX plus(double @NotNull ... array) {
        return asSequence().plus(array).toMutableList();
    }

    @Override
    MutableCollectionX<Double> boxed();
}
