package hzt.sequences.primitives;

import hzt.iterators.primitives.LongSubIterator;
import hzt.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

import static hzt.PreConditions.require;

final class LongSubSequence implements LongSkipTakeSequence {

    private final LongSequence upstream;
    private final long startIndex;
    private final long endIndex;
    private final long count;

    LongSubSequence(LongSequence upstream, long startIndex, long endIndex) {
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
    public LongSequence skip(long n) {
        return n >= count ? PrimitiveIterators::emptyLongIterator : new LongSubSequence(upstream, startIndex + n, endIndex);
    }

    @Override
    public LongSubSequence take(long n) {
        return n >= count ? this : new LongSubSequence(upstream, startIndex, startIndex + 1);
    }

    @Override
    public long count() {
        return count;
    }
}
