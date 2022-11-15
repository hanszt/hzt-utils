package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

final class FilteringIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<? super T> predicate;
    private final boolean sendWhen;
    private State nextState = State.NEXT_UNKNOWN;
    private T nextItem = null;

    FilteringIterator(Iterator<T> iterator, Predicate<? super T> predicate, boolean sendWhen) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
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
        final var result = nextItem;
        nextItem = null;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }

    private void calculateNext() {
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (predicate.test(item) == sendWhen) {
                nextItem = item;
                nextState = State.CONTINUE;
                return;
            }
        }
        nextState = State.DONE;
    }
}
