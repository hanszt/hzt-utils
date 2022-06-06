package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterators.primitives.PrimitiveIteratorX;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

@FunctionalInterface
public interface DoubleIterable extends Iterable<Double> {

    PrimitiveIterator.OfDouble iterator();

    default PrimitiveIteratorX.OfDouble iteratorX() {
        return PrimitiveIteratorX.of(iterator());
    }

    default void forEachDouble(@NotNull DoubleConsumer action) {
        PrimitiveIterator.OfDouble intIterator = iterator();
        while (intIterator.hasNext()) {
            action.accept(intIterator.nextDouble());
        }
    }

    @Override
    default void forEach(Consumer<? super Double> action) {
        PrimitiveIterator.OfDouble intIterator = iterator();
        if (intIterator.hasNext()) {
            throw new UnsupportedOperationException("Use forEachDouble instead");
        }
    }

    @Override
    default Spliterator.OfDouble spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
