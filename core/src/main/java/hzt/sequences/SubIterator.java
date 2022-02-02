package hzt.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class SubIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final int startIndex;
    private final int endIndex;

    private int position;

    SubIterator(Iterator<T> iterator, int startIndex, int endIndex) {
        this.iterator = iterator;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
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
