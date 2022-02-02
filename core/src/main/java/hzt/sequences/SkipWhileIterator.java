package hzt.sequences;

import java.util.Iterator;
import java.util.function.Predicate;

final class SkipWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private T nextItem = null;
    private SkipState state = SkipState.NOT_SKIPPING;

    SkipWhileIterator(Iterator<T> iterator, Predicate<T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    private void skip() {
        while (iterator.hasNext()) {
            final T item = iterator.next();
            if (!predicate.test(item)) {
                nextItem = item;
                state = SkipState.NEXT_ITEM;
                return;
            }
        }
        state = SkipState.NORMAL_ITERATION;
    }

    @Override
    public boolean hasNext() {
        if (state == SkipState.NOT_SKIPPING) {
            skip();
        }
        return state == SkipState.NEXT_ITEM || iterator.hasNext();
    }

    @Override
    public T next() {
        if (state == SkipState.NOT_SKIPPING) {
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
        NOT_SKIPPING, NEXT_ITEM, NORMAL_ITERATION
    }
}
