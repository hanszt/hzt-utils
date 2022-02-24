package hzt.collections.primitives;

import hzt.collections.MutableCollectionX;
import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

public interface DoubleMutableCollection extends PrimitiveMutableCollectionX<Double, DoubleConsumer, double[]>, DoubleCollection {
    @Override
    default boolean add(Double d) {
        return add((double) d);
    }

    @Override
    default boolean addAll(Iterable<Double> iterable) {
        final PrimitiveIterator.OfDouble iterator = DoubleSequence.of(iterable).iterator();
        while (iterator.hasNext()) {
            add(iterator.nextDouble());
        }
        return true;
    }

    @Override
    default DoubleMutableListX plus(@NotNull Iterable<Double> iterable) {
        return DoubleSequence.of(this).plus(iterable).toMutableList();
    }

    @Override
    default MutableCollectionX<Double> boxed() {
        return asSequence().boxed().toMutableList();
    }

    boolean add(double d);
}
