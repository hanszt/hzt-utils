package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

final class FlatMappingIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction;

    private Iterator<? extends R> itemIterator = null;

    FlatMappingIterator(final Iterator<T> iterator,
                        final Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction) {
        this.iterator = iterator;
        this.toIteratorFunction = toIteratorFunction;
    }

    @Override
    public boolean hasNext() {
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

    @Override
    public R next() {
        if (hasNext()) {
            return itemIterator.next();
        }
        throw new NoSuchElementException();
    }
}
