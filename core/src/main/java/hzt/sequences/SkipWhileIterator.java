package hzt.sequences;

import java.util.Iterator;
import java.util.function.Predicate;

final class SkipWhileIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;
    private final Predicate<T> predicate;
    private T nextItem;
    // -1 for not skipping, 1 for nextItem, 0 for normal iteration
    private byte skipState = -1;

    SkipWhileIterator(Iterator<T> iterator, Predicate<T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    private void skip() {
        while (iterator.hasNext()) {
            final T item = iterator.next();
            if (!predicate.test(item)) {
                nextItem = item;
                skipState = 1;
                return;
            }
        }
        skipState = 0;
    }

    @Override
    public boolean hasNext() {
        if (skipState == -1) {
            skip();
        }
        return skipState == 1 || iterator.hasNext();
    }

    @Override
    public T next() {
        if (skipState == -1) {
            skip();
        }
        if (skipState == 1) {
            T result = nextItem;
            nextItem = null;
            skipState = 0;
            return result;
        }
        return iterator.next();
    }
}
