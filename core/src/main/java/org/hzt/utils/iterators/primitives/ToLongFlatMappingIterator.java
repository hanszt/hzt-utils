package org.hzt.utils.iterators.primitives;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Function;

final class ToLongFlatMappingIterator<T> implements PrimitiveIterator.OfLong {

    private final Iterator<T> iterator;
    private final Function<? super T, ? extends PrimitiveIterator.OfLong> toIteratorFunction;

    private PrimitiveIterator.OfLong itemIterator = null;

    ToLongFlatMappingIterator(final Iterator<T> iterator,
                              final Function<? super T, ? extends PrimitiveIterator.OfLong> toIteratorFunction) {
        this.iterator = iterator;
        this.toIteratorFunction = toIteratorFunction;
    }

    @Override
    public boolean hasNext() {
        return ensureItemIterator();
    }

    @Override
    public long nextLong() {
        if (!ensureItemIterator()) {
            throw new NoSuchElementException();
        }
        return itemIterator.nextLong();
    }

    private boolean ensureItemIterator() {
        if (itemIterator != null && !itemIterator.hasNext()) {
            itemIterator = null;
        }
        while (itemIterator == null) {
            if (!iterator.hasNext()) {
                return false;
            }
            final var nextItemIterator = toIteratorFunction.apply(iterator.next());
            if (nextItemIterator.hasNext()) {
                itemIterator = nextItemIterator;
                return true;
            }
        }
        return true;
    }
}
