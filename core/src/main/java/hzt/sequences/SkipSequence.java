package hzt.sequences;

import hzt.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

final class SkipSequence<T> implements SkipTakeSequence<T> {

    private final Sequence<T> upstream;
    private final int count;

    SkipSequence(Sequence<T> upstream, int count) {
        PreConditions.requireGreaterThanZero(count);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public Sequence<T> skip(int n) {
        final int n1 = count + n;
        return n1 < 0 ? new SkipSequence<>(this, n) : new SkipSequence<>(upstream, n1);
    }

    @Override
    public Sequence<T> take(int n) {
        final int n1 = count + n;
        return n1 < 0 ? new TakeSequence<>(this, n) : new SubSequence<>(upstream, count, n1);
    }

    @Override
    public int count() {
        return count;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Iterator<T> iterator = upstream.iterator();
            private int left = count;
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
