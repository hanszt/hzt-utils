package hzt.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

final class FlatteningIterator<T, R, E> implements Iterator<E> {

    private final Iterator<T> iterator;
    private final Function<T, R> transformer;
    private final Function<R, Iterator<E>> toIteratorFunction;

    private Iterator<E> itemIterator = null;

    FlatteningIterator(Iterator<T> iterator,
                       Function<T, R> transformer,
                       Function<R, Iterator<E>> toIteratorFunction) {
        this.iterator = iterator;
        this.transformer = transformer;
        this.toIteratorFunction = toIteratorFunction;
    }

    @Override
    public boolean hasNext() {
        return ensureItemIterator();
    }

    @Override
    public E next() {
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
                final Iterator<E> nextItemIterator = toIteratorFunction.apply(transformer.apply(item));
                if (nextItemIterator.hasNext()) {
                    itemIterator = nextItemIterator;
                    return true;
                }
            }
        }
        return true;
    }
}
