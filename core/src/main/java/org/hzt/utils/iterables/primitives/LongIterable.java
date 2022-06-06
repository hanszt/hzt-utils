package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterators.primitives.PrimitiveIteratorX;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

@FunctionalInterface
public interface LongIterable extends Iterable<Long> {

    PrimitiveIterator.OfLong iterator();

    default PrimitiveIteratorX.OfLong iteratorX() {
        return PrimitiveIteratorX.of(iterator());
    }

    default void forEachLong(@NotNull LongConsumer action) {
        PrimitiveIterator.OfLong intIterator = iterator();
        while (intIterator.hasNext()) {
            action.accept(intIterator.nextLong());
        }
    }

    @Override
    default void forEach(Consumer<? super Long> action) {
        PrimitiveIterator.OfLong intIterator = iterator();
        if (intIterator.hasNext()) {
            throw new UnsupportedOperationException("Use forEachLong instead");
        }
    }

    @Override
    default Spliterator.OfLong spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
