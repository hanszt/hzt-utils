package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.PrimitiveIterator;

final class IndexIterator<T> implements PrimitiveIterator.OfInt {

    private final Iterator<T> iterator;
    int index = 0;

    IndexIterator(final Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public int nextInt() {
        final var prevIndex = index;
        if (prevIndex < 0) {
            throw new IllegalStateException("indexed iterator index overflow");
        }
        switch (iterator) {
            case PrimitiveIterator.OfInt ofInt -> ofInt.nextInt();
            case PrimitiveIterator.OfLong ofLong -> ofLong.nextLong();
            case PrimitiveIterator.OfDouble ofDouble -> ofDouble.nextDouble();
            case null -> throw new IllegalStateException("Iterator was null");
            default -> iterator.next();
        }
        return index++;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}
