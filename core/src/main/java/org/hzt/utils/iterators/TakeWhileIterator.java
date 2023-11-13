package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

final class TakeWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<? super T> predicate;
    private final boolean inclusive;
    private boolean inclusiveConsumed = false;
    private T nextItem;
    private State nextState = State.INIT_UNKNOWN;

    TakeWhileIterator(final Iterator<T> iterator, final Predicate<? super T> predicate, final boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    private void calculateNext() {
        if (iterator.hasNext()) {
            final T item = iterator.next();
            if (predicate.test(item) && !inclusiveConsumed) {
                nextState = State.CONTINUE;
                nextItem = item;
                return;
            }
            if (inclusive && !inclusiveConsumed && nextState != State.INIT_UNKNOWN) {
                nextState = State.CONTINUE;
                nextItem = item;
                inclusiveConsumed = true;
                return;
            }
        }
        nextState = State.DONE;
    }

    @Override
    public boolean hasNext() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        return nextState == State.CONTINUE;
    }

    @Override
    public T next() {
        if (nextState.isUnknown()) {
            calculateNext();
        }
        if (nextState == State.DONE) {
            throw new NoSuchElementException();
        }
        final T result = nextItem;
        // Clean next to avoid keeping reference on yielded instance
        nextItem = null;
        nextState = State.NEXT_UNKNOWN;
        return result;
    }
}
