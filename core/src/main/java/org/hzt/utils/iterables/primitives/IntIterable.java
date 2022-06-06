package org.hzt.utils.iterables.primitives;

import org.hzt.utils.iterators.primitives.PrimitiveIteratorX;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@FunctionalInterface
public interface IntIterable extends Iterable<Integer> {

    PrimitiveIterator.OfInt iterator();

    default PrimitiveIteratorX.OfInt iteratorX() {
        return PrimitiveIteratorX.of(iterator());
    }

    default void forEachInt(@NotNull IntConsumer action) {
        PrimitiveIterator.OfInt intIterator = iterator();
        while (intIterator.hasNext()) {
            action.accept(intIterator.nextInt());
        }
    }

    @Override
    default void forEach(Consumer<? super Integer> action) {
        PrimitiveIterator.OfInt intIterator = iterator();
        if (intIterator.hasNext()) {
            throw new UnsupportedOperationException("Use forEachInt instead");
        }
    }

    @Override
    default Spliterator.OfInt spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
