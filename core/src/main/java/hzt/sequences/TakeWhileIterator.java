package hzt.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

final class TakeWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private final boolean inclusive;

    private boolean inclusiveConsumed = false;
    private T nextItem;
    // -2 for start unknown, -1 for unknown, 0 for done, 1 for continue
    private byte nextState = -2;

    TakeWhileIterator(Iterator<T> iterator, Predicate<T> predicate, boolean inclusive) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.inclusive = inclusive;
    }

    private void calculateNext() {
        if (iterator.hasNext()) {
            final T item = iterator.next();
            if (predicate.test(item) && !inclusiveConsumed) {
                nextState = 1;
                nextItem = item;
                return;
            }
            if (inclusive && !inclusiveConsumed && nextState != -2) {
                nextState = 1;
                nextItem = item;
                inclusiveConsumed = true;
                return;
            }
        }
        nextState = 0;
    }

    @Override
    public boolean hasNext() {
        if (nextState < 0) {
            calculateNext();
        }
        return nextState == 1;
    }

    @Override
    public T next() {
        if (nextState < 0) {
            calculateNext();
        }
        if (nextState == 0) {
            throw new NoSuchElementException();
        }
        T result = nextItem;
        // Clean next to avoid keeping reference on yielded instance
        nextItem = null;
        nextState = -1;
        return result;
    }
}
