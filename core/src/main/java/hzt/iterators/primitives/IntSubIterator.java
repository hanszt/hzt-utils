package hzt.iterators.primitives;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class IntSubIterator implements PrimitiveIterator.OfInt {

    private final OfInt iterator;
    private final long startIndex;
    private final long endIndex;

    private long position;

    private IntSubIterator(OfInt iterator, long startIndex, long endIndex) {
        this.iterator = iterator;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public static IntSubIterator of(OfInt iterator, long startIndex, long endIndex) {
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
