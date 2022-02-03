package hzt.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

final class FlatteningIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final Function<T, Iterator<R>> toIteratorFunction;

    private Iterator<R> itemIterator = null;

    FlatteningIterator(Iterator<T> iterator,
                       Function<T, Iterator<R>> toIteratorFunction) {
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
            } else {
                final T item  = iterator.next();
                final Iterator<R> nextItemIterator = toIteratorFunction.apply(item);
                if (nextItemIterator.hasNext()) {
                    itemIterator = nextItemIterator;
                    return true;
                }
            }
        }
        return true;
    }
}
