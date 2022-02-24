package hzt.iterables.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;

@FunctionalInterface
public interface DoubleIterable extends Iterable<Double> {

    @Override
    PrimitiveIterator.OfDouble iterator();

    default void forEachDouble(@NotNull DoubleConsumer action) {
        PrimitiveIterator.OfDouble intIterator = iterator();
        while (intIterator.hasNext()) {
            action.accept(intIterator.nextDouble());
        }
    }

    @Override
    default Spliterator.OfDouble spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
