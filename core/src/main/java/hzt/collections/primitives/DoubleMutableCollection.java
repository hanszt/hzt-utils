package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;
import hzt.iterables.primitives.DoubleIterable;
import hzt.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.function.DoubleConsumer;

public interface DoubleMutableCollection extends PrimitiveMutableCollectionX<Double, DoubleConsumer, double[]>, DoubleCollection {

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
