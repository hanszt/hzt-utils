package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public final class FlatteningIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final Function<T, Iterator<R>> toIteratorFunction;

    private Iterator<R> itemIterator = null;

    private FlatteningIterator(Iterator<T> iterator, Function<T, Iterator<R>> toIteratorFunction) {
        this.iterator = iterator;
        this.toIteratorFunction = toIteratorFunction;
    }

    public static <T, R> FlatteningIterator<T, R> of(Iterator<T> iterator, Function<T, Iterator<R>> toIteratorFunction) {
        return new FlatteningIterator<>(iterator, toIteratorFunction);
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
            } else {
                final Iterator<R> nextItemIterator = toIteratorFunction.apply(iterator.next());
                if (nextItemIterator.hasNext()) {
                    itemIterator = nextItemIterator;
                    return true;
                }
            }
        }
        return true;
    }
}
