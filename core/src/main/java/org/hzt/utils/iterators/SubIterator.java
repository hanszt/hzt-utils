package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class SubIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final long startIndex;
    private final long endIndex;

    private long position;

    private SubIterator(Iterator<T> iterator, long startIndex, long endIndex) {
        this.iterator = iterator;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public static <T> SubIterator<T> of(Iterator<T> iterator, long startIndex, long endIndex) {
        return new SubIterator<>(iterator, startIndex, endIndex);
    }

    private void skip() {
        while (position < startIndex && iterator.hasNext()) {
            iterator.next();
            position++;
        }
    }

    @Override
    public boolean hasNext() {
        skip();
        return (position < endIndex) && iterator.hasNext();
    }

    @Override
    public T next() {
        skip();
        if (position >= endIndex) {
            throw new NoSuchElementException();
        }
        position++;
        return iterator.next();
    }
}
