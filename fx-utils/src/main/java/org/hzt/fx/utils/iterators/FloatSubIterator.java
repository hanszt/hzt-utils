package org.hzt.fx.utils.iterators;

import java.util.NoSuchElementException;

final class FloatSubIterator implements FloatIterator {

    private final FloatIterator iterator;
    private final long startIndex;
    private final long endIndex;

    private long position;

    private FloatSubIterator(FloatIterator iterator, long startIndex, long endIndex) {
        this.iterator = iterator;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public static FloatSubIterator of(FloatIterator iterator, long startIndex, long endIndex) {
        return new FloatSubIterator(iterator, startIndex, endIndex);
    }

    private void skip() {
        while (position < startIndex && iterator.hasNext()) {
            iterator.nextFloat();
            position++;
        }
    }

    @Override
    public boolean hasNext() {
        skip();
        return (position < endIndex) && iterator.hasNext();
    }

    @Override
    public float nextFloat() {
        skip();
        if (position >= endIndex) {
            throw new NoSuchElementException();
        }
        position++;
        return iterator.nextFloat();
    }
}
