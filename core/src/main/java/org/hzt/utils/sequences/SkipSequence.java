package org.hzt.utils.sequences;

import org.hzt.utils.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

final class SkipSequence<T> implements SkipTakeSequence<T> {

    private final Sequence<T> upstream;
    private final long count;

    SkipSequence(Sequence<T> upstream, long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public Sequence<T> skip(long n) {
        final long n1 = count + n;
        return n1 < 0 ? new SkipSequence<>(this, n) : new SkipSequence<>(upstream, n1);
    }

    @Override
    public Sequence<T> take(long n) {
        final long n1 = count + n;
        return n1 < 0 ? new TakeSequence<>(this, n) : new SubSequence<>(upstream, count, n1);
    }

    @Override
    public long count() {
        return count;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Iterator<T> iterator = upstream.iterator();
            private long left = count;

            private void skip() {
                while (left > 0 && iterator.hasNext()) {
                    iterator.next();
                    left--;
                }
            }

            @Override
            public boolean hasNext() {
                skip();
                return iterator.hasNext();
            }

            @Override
            public T next() {
                skip();
                return iterator.next();
            }
        };
    }
}
