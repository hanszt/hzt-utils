package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public final class FilteringIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private final boolean sendWhen;
    private State nextState = State.NEXT_UNKNOWN;
    private T nextItem = null;

    private FilteringIterator(Iterator<T> iterator, Predicate<T> predicate, boolean sendWhen) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    public static <T> FilteringIterator<T> of(Iterator<T> iterator, Predicate<T> predicate, boolean sendWhen) {
        return new FilteringIterator<>(iterator, predicate, sendWhen);
    }

    @Override
    public boolean hasNext() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public T next() {
        if (nextState == State.NEXT_UNKNOWN) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final T result = nextItem;
        nextItem = null;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (predicate.test(item) == sendWhen) {
                nextItem = item;
                nextState = State.CONTINUE;
                return;
            }
        }
        nextState = State.DONE;
    }
}
