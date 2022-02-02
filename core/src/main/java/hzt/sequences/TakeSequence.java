package hzt.sequences;

import hzt.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class TakeSequence<T> implements SkipTakeSequence<T> {

    private final Sequence<T> upstream;
    private final int count;

    TakeSequence(Sequence<T> upstream, int count) {
        PreConditions.requireGreaterThanZero(count);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public Sequence<T> skip(int n) {
        return n >= count ? new EmptySequence<>() : new SubSequence<>(upstream, n, count);
    }

    @Override
    public Sequence<T> take(int n) {
        return n >= count ? this : new TakeSequence<>(upstream, n);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Iterator<T> iterator = upstream.iterator();
            private int left = count;
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
    public int count() {
        return count;
    }
}
