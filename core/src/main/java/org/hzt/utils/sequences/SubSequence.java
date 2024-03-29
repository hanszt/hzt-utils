package org.hzt.utils.sequences;

import org.hzt.utils.iterators.Iterators;

import java.util.Iterator;

import static org.hzt.utils.PreConditions.require;

final class SubSequence<T> implements SkipTakeSequence<T> {

    private final Sequence<T> upstream;
    private final long startIndex;
    private final long endIndex;
    private final long count;

    SubSequence(final Sequence<T> upstream, final long startIndex, final long endIndex) {
        require(startIndex >= 0, () -> "startIndex should be non-negative, but is " + startIndex);
        require(endIndex >= 0, () -> "endIndex should be non-negative, but is " + endIndex);
        require(endIndex >= startIndex, () -> "endIndex should be not less than startIndex, but was " + endIndex + " < " + startIndex);
        this.upstream = upstream;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.count = endIndex - startIndex;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.subIterator(upstream.iterator(), startIndex, endIndex);
    }

    @Override
    public Sequence<T> skip(final long n) {
        return n >= count ? new EmptySequence<>() : new SubSequence<>(upstream, startIndex + n, endIndex);
    }

    @Override
    public Sequence<T> take(final long n) {
        return n >= count ? this : new SubSequence<>(upstream, startIndex, startIndex + 1);
    }

    @Override
    public long count() {
        return count;
    }
}
