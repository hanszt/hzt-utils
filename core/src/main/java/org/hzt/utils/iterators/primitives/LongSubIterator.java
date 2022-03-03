package org.hzt.utils.iterators.primitives;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class LongSubIterator implements PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final long startIndex;
    private final long endIndex;

    private int position;

    private LongSubIterator(PrimitiveIterator.OfLong iterator, long startIndex, long endIndex) {
        this.iterator = iterator;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public static LongSubIterator of(PrimitiveIterator.OfLong iterator, long startIndex, long endIndex) {
        return new LongSubIterator(iterator, startIndex, endIndex);
    }

    private void skip() {
        while (position < startIndex && iterator.hasNext()) {
            iterator.nextLong();
            position++;
        }
    }

    @Override
    public boolean hasNext() {
        skip();
        return (position < endIndex) && iterator.hasNext();
    }

    @Override
    public long nextLong() {
        skip();
        if (position >= endIndex) {
            throw new NoSuchElementException();
        }
        position++;
        return iterator.nextLong();
    }
}
