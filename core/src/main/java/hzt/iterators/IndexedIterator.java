package hzt.iterators;

import hzt.tuples.IndexedValue;

import java.util.Iterator;

public final class IndexedIterator<T> implements Iterator<IndexedValue<T>> {

    private final Iterator<T> iterator;
    private int index = 0;

    private IndexedIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public static <T> IndexedIterator<T> of (Iterator<T> iterator) {
        return new IndexedIterator<>(iterator);
    }

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
}
