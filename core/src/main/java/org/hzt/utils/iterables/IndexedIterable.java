package org.hzt.utils.iterables;

import org.hzt.utils.function.IndexedConsumer;
import org.hzt.utils.iterators.Iterators;
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
        final var iterator = iterator();
        return new Iterator<>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public IndexedValue<T> next() {
                final var prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return new IndexedValue<>(index++, iterator.next());
            }
        };
    }

    default @NotNull PrimitiveIterator.OfInt indexIterator() {
        return Iterators.indexIterator(iterator());
    }

    default Spliterator<IndexedValue<T>> indexedSpliterator() {
        return Spliterators.spliteratorUnknownSize(indexedIterator(), Spliterator.ORDERED);
    }

    default Spliterator.OfInt indexSpliterator() {
        return Spliterators.spliteratorUnknownSize(indexIterator(), Spliterator.ORDERED);
    }

    default void forEachIndex(@NotNull final IntConsumer action) {
        final var indexIterator = indexIterator();
        while (indexIterator.hasNext()) {
            action.accept(indexIterator.nextInt());
        }
    }

    default void forEachIndexed(@NotNull final IndexedConsumer<T> action) {
        forEachIndexedValue(indexedValue -> action.accept(indexedValue.index(), indexedValue.value()));
    }

    default void forEachIndexedValue(@NotNull final Consumer<IndexedValue<T>> action) {
        final var iterator = indexedIterator();
        while (iterator.hasNext()) {
            final var next = iterator.next();
            action.accept(next);
        }
    }
}
