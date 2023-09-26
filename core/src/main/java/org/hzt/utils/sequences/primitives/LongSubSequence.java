package org.hzt.utils.sequences.primitives;

import org.hzt.utils.iterators.primitives.LongSubIterator;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

import static org.hzt.utils.PreConditions.require;

final class LongSubSequence implements LongSkipTakeSequence {

    private final LongSequence upstream;
    private final long startIndex;
    private final long endIndex;
    private final long count;

    LongSubSequence(final LongSequence upstream, final long startIndex, final long endIndex) {
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
    public PrimitiveIterator.OfLong iterator() {
        return LongSubIterator.of(upstream.iterator(), startIndex, endIndex);
    }

    @Override
    public LongSequence skip(final long n) {
        return n >= count ? PrimitiveIterators::emptyLongIterator : new LongSubSequence(upstream, startIndex + n, endIndex);
    }

    @Override
    public LongSubSequence take(final long n) {
        return n >= count ? this : new LongSubSequence(upstream, startIndex, startIndex + 1);
    }

    @Override
    public long count() {
        return count;
    }
}
