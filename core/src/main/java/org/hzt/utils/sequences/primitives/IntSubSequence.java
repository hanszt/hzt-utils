package org.hzt.utils.sequences.primitives;

import org.hzt.utils.iterators.primitives.IntSubIterator;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

import static org.hzt.utils.PreConditions.require;

final class IntSubSequence implements IntSkipTakeSequence {

    private final IntSequence upstream;
    private final long startIndex;
    private final long endIndex;
    private final long count;

    IntSubSequence(IntSequence upstream, long startIndex, long endIndex) {
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
    public PrimitiveIterator.OfInt iterator() {
        return IntSubIterator.of(upstream.iterator(), startIndex, endIndex);
    }

    @Override
    public IntSequence skip(long n) {
        return n >= count ? PrimitiveIterators::emptyIntIterator : new IntSubSequence(upstream, startIndex + n, endIndex);
    }

    @Override
    public IntSubSequence take(long n) {
        return n >= count ? this : new IntSubSequence(upstream, startIndex, startIndex + 1);
    }

    @Override
    public long count() {
        return count;
    }
}
