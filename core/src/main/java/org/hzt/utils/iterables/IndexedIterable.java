package org.hzt.utils.iterables;

import org.hzt.utils.function.IndexedConsumer;
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
        final Iterator<T> iterator = iterator();
        return new Iterator<IndexedValue<T>>() {

            private int index = 0;
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            @Override
            public IndexedValue<T> next() {
                int prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                return new IndexedValue<>(index++, iterator.next());
            }
        };
    }

    default @NotNull PrimitiveIterator.OfInt indexIterator() {
        final Iterator<T> iterator = iterator();
        return new PrimitiveIterator.OfInt() {

            int index = 0;
            @Override
            public int nextInt() {
                int prevIndex = index;
                if (prevIndex < 0) {
                    throw new IllegalStateException("indexed iterator index overflow");
                }
                iterator.next();
                return index++;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
        };
    }

    default Spliterator<IndexedValue<T>> indexedSpliterator() {
        return Spliterators.spliteratorUnknownSize(indexedIterator(), Spliterator.ORDERED);
    }

    default Spliterator.OfInt indexSpliterator() {
        return Spliterators.spliteratorUnknownSize(indexIterator(), Spliterator.ORDERED);
    }

    default void forEachIndex(@NotNull IntConsumer action) {
        final PrimitiveIterator.OfInt indexIterator = indexIterator();
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
