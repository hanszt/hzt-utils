package org.hzt.utils.sequences;

import org.hzt.utils.PreConditions;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class TakeSequence<T> implements SkipTakeSequence<T> {

    private final Sequence<T> upstream;
    private final long count;

    TakeSequence(Sequence<T> upstream, long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public Sequence<T> skip(long n) {
        return n >= count ? new EmptySequence<>() : new SubSequence<>(upstream, n, count);
    }

    @Override
    public Sequence<T> take(long n) {
        return n >= count ? this : new TakeSequence<>(upstream, n);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Iterator<T> iterator = upstream.iterator();
            private long left = count;
            @Override
            public boolean hasNext() {
                return left > 0 && iterator.hasNext();
            }
            @Override
            public T next() {
                if (left == 0) {
                    throw new NoSuchElementException();
                }
                left--;
                return iterator.next();
            }
        };
    }

    @Override
    public long count() {
        return count;
    }
}
