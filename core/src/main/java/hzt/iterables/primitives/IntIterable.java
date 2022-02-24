package hzt.iterables.primitives;

import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

@FunctionalInterface
public interface IntIterable extends Iterable<Integer> {

    PrimitiveIterator.OfInt iterator();

    default void forEachInt(@NotNull IntConsumer action) {
        PrimitiveIterator.OfInt intIterator = iterator();
        while (intIterator.hasNext()) {
            action.accept(intIterator.nextInt());
        }
    }

    @Override
    default Spliterator.OfInt spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
