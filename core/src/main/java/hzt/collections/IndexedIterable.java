package hzt.collections;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IndexedIterable<T> {

    Iterator<IndexedValue<T>> indexedIterator();

    default void forEachIndexed(BiConsumer<Integer, T> action) {
        forEachIndexedValue(indexedValue -> action.accept(indexedValue.index(), indexedValue.value()));
    }

    default void forEachIndexedValue(Consumer<IndexedValue<T>> action) {
        Objects.requireNonNull(action);
        Iterator<IndexedValue<T>> iterator = indexedIterator();
        while (iterator.hasNext()) {
            final IndexedValue<T> next = iterator.next();
            action.accept(next);
        }
    }
}
