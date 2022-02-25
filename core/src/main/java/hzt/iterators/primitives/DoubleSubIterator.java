package hzt.iterators.primitives;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class DoubleSubIterator implements PrimitiveIterator.OfDouble {

    private final OfDouble iterator;
    private final long startIndex;
    private final long endIndex;

    private long position;

    private DoubleSubIterator(OfDouble iterator, long startIndex, long endIndex) {
        this.iterator = iterator;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public static DoubleSubIterator of(OfDouble iterator, long startIndex, long endIndex) {
        return new DoubleSubIterator(iterator, startIndex, endIndex);
    }

    private void skip() {
        while (position < startIndex && iterator.hasNext()) {
            iterator.nextDouble();
            position++;
        }
    }

    @Override
    public boolean hasNext() {
        skip();
        return (position < endIndex) && iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        skip();
        if (position >= endIndex) {
            throw new NoSuchElementException();
        }
        position++;
        return iterator.nextDouble();
    }
}
