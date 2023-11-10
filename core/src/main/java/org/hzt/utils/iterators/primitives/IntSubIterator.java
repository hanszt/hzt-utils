package org.hzt.utils.iterators.primitives;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class IntSubIterator implements PrimitiveIterator.OfInt {

    private final OfInt iterator;
    private final long startIndex;
    private final long endIndex;

    private long position;

    private IntSubIterator(final OfInt iterator, final long startIndex, final long endIndex) {
        this.iterator = iterator;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public static IntSubIterator of(final OfInt iterator, final long startIndex, final long endIndex) {
        return new IntSubIterator(iterator, startIndex, endIndex);
    }

    private void skip() {
        while (position < startIndex && iterator.hasNext()) {
            iterator.nextInt();
            position++;
        }
    }

    @Override
    public boolean hasNext() {
        skip();
        return (position < endIndex) && iterator.hasNext();
    }

    @Override
    public int nextInt() {
        skip();
        if (position >= endIndex) {
            throw new NoSuchElementException();
        }
        position++;
        return iterator.nextInt();
    }
}
