package org.hzt.utils.iterators;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public final class FlatteningIterator<T, R> implements Iterator<R> {

    private final Iterator<T> iterator;
    private final Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction;

    private Iterator<? extends R> itemIterator = null;

    private FlatteningIterator(Iterator<T> iterator,
                               Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction) {
        this.iterator = iterator;
        this.toIteratorFunction = toIteratorFunction;
    }

    public static <T, R> FlatteningIterator<T, R> of(@NotNull Iterator<T> iterator,
                                                     @NotNull Function<? super T, ? extends Iterator<? extends R>> toIteratorFunction) {
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
                final Iterator<? extends R> nextItemIterator = toIteratorFunction.apply(iterator.next());
                if (nextItemIterator.hasNext()) {
                    itemIterator = nextItemIterator;
                    return true;
                }
            }
        }
        return true;
    }
}
