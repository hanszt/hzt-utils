package org.hzt.utils.iterables.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;

@FunctionalInterface
public interface LongIterable extends Iterable<Long> {

    PrimitiveIterator.OfLong iterator();

    default void forEachLong(@NotNull LongConsumer action) {
        PrimitiveIterator.OfLong intIterator = iterator();
        while (intIterator.hasNext()) {
            action.accept(intIterator.nextLong());
        }
    }

    @Override
    default Spliterator.OfLong spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
