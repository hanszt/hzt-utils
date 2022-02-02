package hzt.sequences;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static hzt.PreConditions.require;

final class SubSequence<T> implements SkipTakeSequence<T> {

    private final Sequence<T> upstream;
    private final int startIndex;
    private final int endIndex;
    private final int count;

    SubSequence(Sequence<T> upstream, int startIndex, int endIndex) {
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
        return new SubIterator<>(upstream.iterator(), startIndex, endIndex);
    }

    @Override
    public Sequence<T> skip(int n) {
        return n >= count ? new EmptySequence<>() : new SubSequence<>(upstream, startIndex + n, endIndex);
    }

    @Override
    public Sequence<T> take(int n) {
        return n >= count ? this : new SubSequence<>(upstream, startIndex, startIndex + 1);
    }

    @Override
    public int count() {
        return count;
    }
}
