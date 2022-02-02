package hzt.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

final class FilteringIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private final boolean sendWhen;
    // -1 for unknown, 0 for done, 1 for continue
    private byte nextState = -1;
    private T nextItem = null;

    FilteringIterator(Iterator<T> iterator, Predicate<T> predicate, boolean sendWhen) {
        this.iterator = iterator;
        this.predicate = predicate;
        this.sendWhen = sendWhen;
    }

    @Override
    public boolean hasNext() {
        if (nextState == -1) {
            calculateNext();
        }
        return nextState == 1;
    }

    @Override
    public T next() {
        if (nextState == -1) {
            calculateNext();
        }
        if (nextState == 0) {
            throw new NoSuchElementException();
        }
        final T result = nextItem;
        nextItem = null;
        nextState = -1;
        return result;
    }

    private void calculateNext() {
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (predicate.test(item) == sendWhen) {
                nextItem = item;
                nextState = 1;
                return;
            }
        }
        nextState = 0;
    }
}
