package hzt.iterables.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;

@FunctionalInterface
public interface LongIterable extends Iterable<Long> {

    PrimitiveIterator.OfLong longIterator();

    @Override
    default PrimitiveIterator.OfLong iterator() {
        return longIterator();
    }

    default void forEachLong(@NotNull LongConsumer action) {
        PrimitiveIterator.OfLong intIterator = longIterator();
        while (intIterator.hasNext()) {
            action.accept(intIterator.nextLong());
        }
    }

    @Override
    default Spliterator.OfLong spliterator() {
        return Spliterators.spliteratorUnknownSize(longIterator(), 0);
    }
}
