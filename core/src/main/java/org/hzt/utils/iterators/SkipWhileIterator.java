package org.hzt.utils.iterators;

import java.util.Iterator;
import java.util.function.Predicate;

public final class SkipWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private final boolean inclusive;

    private T nextItem = null;
    private SkipState state = SkipState.SKIPPING;

    SkipWhileIterator(Iterator<T> iterator, Predicate<T> predicate, boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    public static <T> SkipWhileIterator<T> of(Iterator<T> iterator, Predicate<T> predicate, boolean inclusive) {
        return new SkipWhileIterator<>(iterator, predicate, inclusive);
    }

    private void skip() {
        while (iterator.hasNext()) {
            T prev = nextItem;
            nextItem = iterator.next();
            if (!predicate.test(nextItem)) {
                if (prev != null && inclusive && iterator.hasNext()) {
                    nextItem = iterator.next();
                }
                state = SkipState.NEXT_ITEM;
                return;
            }
        }
        state = SkipState.NORMAL_ITERATION;
    }

    @Override
    public boolean hasNext() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        return state == SkipState.NEXT_ITEM || iterator.hasNext();
    }

    @Override
    public T next() {
        if (state == SkipState.SKIPPING) {
            skip();
        }
        if (state == SkipState.NEXT_ITEM) {
            T result = nextItem;
            nextItem = null;
            state = SkipState.NORMAL_ITERATION;
            return result;
        }
        return iterator.next();
    }

    private enum SkipState {
        SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
