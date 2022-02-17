package hzt.iterables.primitive;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.DoubleConsumer;

@FunctionalInterface
public interface DoubleIterable extends Iterable<Double> {

    PrimitiveIterator.OfDouble doubleIterator();

    @Override
    default PrimitiveIterator.OfDouble iterator() {
        return doubleIterator();
    }

    default void forEachDouble(@NotNull DoubleConsumer action) {
        PrimitiveIterator.OfDouble intIterator = doubleIterator();
        while (intIterator.hasNext()) {
            action.accept(intIterator.nextDouble());
        }
    }

    @Override
    default Spliterator.OfDouble spliterator() {
        return Spliterators.spliteratorUnknownSize(doubleIterator(), 0);
    }
}
