package org.hzt.utils.iterables;

import org.hzt.utils.function.primitives.IndexedConsumer;
import org.hzt.utils.iterators.IndexIterator;
import org.hzt.utils.iterators.IndexedIterator;
import org.hzt.utils.tuples.IndexedValue;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@FunctionalInterface
public interface IndexedIterable<T> extends Iterable<T> {

    default @NotNull Iterator<IndexedValue<T>> indexedIterator() {
        return IndexedIterator.of(iterator());
    }

    default @NotNull PrimitiveIterator.OfInt indexIterator() {
        return IndexIterator.of(iterator());
    }

    default Spliterator<IndexedValue<T>> indexedSpliterator() {
        return Spliterators.spliteratorUnknownSize(indexedIterator(), 0);
    }

    default Spliterator.OfInt indexSpliterator() {
        return Spliterators.spliteratorUnknownSize(indexIterator(), 0);
    }

    default void forEachIndex(@NotNull IntConsumer action) {
        final var indexIterator = indexIterator();
        while(indexIterator.hasNext()) {
            action.accept(indexIterator.nextInt());
        }
    }

    default void forEachIndexed(@NotNull IndexedConsumer<T> action) {
        forEachIndexedValue(indexedValue -> action.accept(indexedValue.index(), indexedValue.value()));
    }

    default void forEachIndexedValue(@NotNull Consumer<IndexedValue<T>> action) {
        Iterator<IndexedValue<T>> iterator = indexedIterator();
        while (iterator.hasNext()) {
            final IndexedValue<T> next = iterator.next();
            action.accept(next);
        }
    }
}
