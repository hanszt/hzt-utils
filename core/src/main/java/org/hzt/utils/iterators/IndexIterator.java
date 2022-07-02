package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.PrimitiveIterator;

public final class IndexIterator<T> implements PrimitiveIterator.OfInt {

    private final Iterator<T> iterator;
    private int index = 0;

    private IndexIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public static <T> IndexIterator<T> of(Iterator<T> iterator) {
        return new IndexIterator<>(iterator);
    }

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
}
