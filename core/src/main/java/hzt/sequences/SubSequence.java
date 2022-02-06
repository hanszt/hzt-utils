package hzt.sequences;

import hzt.iterators.SubIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static hzt.PreConditions.require;

final class SubSequence<T> implements SkipTakeSequence<T> {

    private final Sequence<T> upstream;
    private final long startIndex;
    private final long endIndex;
    private final long count;

    SubSequence(Sequence<T> upstream, long startIndex, long endIndex) {
        require(startIndex >= 0, () -> "startIndex should be non-negative, but is " + startIndex);
        require(endIndex >= 0, () -> "endIndex should be non-negative, but is " + endIndex);
        require(endIndex >= startIndex, () -> "endIndex should be not less than startIndex, but was " + endIndex + " < " + startIndex);
        this.upstream = upstream;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.count = endIndex - startIndex;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return SubIterator.of(upstream.iterator(), startIndex, endIndex);
    }

    @Override
    public Sequence<T> skip(long n) {
        return n >= count ? new EmptySequence<>() : new SubSequence<>(upstream, startIndex + n, endIndex);
    }

    @Override
    public Sequence<T> take(long n) {
        return n >= count ? this : new SubSequence<>(upstream, startIndex, startIndex + 1);
    }

    @Override
    public long count() {
        return count;
    }
}
