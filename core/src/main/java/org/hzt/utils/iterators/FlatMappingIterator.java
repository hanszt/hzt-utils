package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

final class FlatMappingIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction;

    private Iterator<? extends R> itemIterator = null;

    FlatMappingIterator(Iterator<T> iterator,
                        Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction) {
        this.iterator = iterator;
        this.toIteratorFunction = toIteratorFunction;
    }

    @Override
    public boolean hasNext() {
        return ensureItemIterator();
    }

    @Override
    public R next() {
        if (!ensureItemIterator()) {
            throw new NoSuchElementException();
        }
        return itemIterator.next();
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
