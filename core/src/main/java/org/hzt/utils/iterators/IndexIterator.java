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
        if (iterator instanceof PrimitiveIterator.OfInt) {
            ((PrimitiveIterator.OfInt) iterator).nextInt();
        } else if (iterator instanceof PrimitiveIterator.OfLong) {
            ((PrimitiveIterator.OfLong) iterator).nextLong();
        } else if (iterator instanceof PrimitiveIterator.OfDouble) {
            ((PrimitiveIterator.OfDouble) iterator).nextDouble();
        } else {
            iterator.next();
        }
        return index++;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }
}
