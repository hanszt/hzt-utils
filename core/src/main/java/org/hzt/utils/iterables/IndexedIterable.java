package org.hzt.utils.iterables;

import org.hzt.utils.iterators.IndexedIterator;
import org.hzt.utils.tuples.IndexedValue;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface IndexedIterable<T> extends Iterable<T> {

    default @NotNull Iterator<IndexedValue<T>> indexedIterator() {
        return IndexedIterator.of(iterator());
    }

    default void forEachIndexed(@NotNull BiConsumer<Integer, T> action) {
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
