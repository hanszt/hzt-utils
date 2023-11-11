package org.hzt.utils.sequences.primitives;

import org.hzt.utils.iterators.primitives.DoubleSubIterator;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;

import java.util.PrimitiveIterator;

import static org.hzt.utils.PreConditions.require;

final class DoubleSubSequence implements DoubleSkipTakeSequence {

    private final DoubleSequence upstream;
    private final long startIndex;
    private final long endIndex;
    private final long count;

    DoubleSubSequence(final DoubleSequence upstream, final long startIndex, final long endIndex) {
        require(startIndex >= 0, () -> "startIndex should be non-negative, but is " + startIndex);
        require(endIndex >= 0, () -> "endIndex should be non-negative, but is " + endIndex);
        require(endIndex >= startIndex, () -> "endIndex should be not less than startIndex, but was " + endIndex + " < " + startIndex);
        this.upstream = upstream;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.count = endIndex - startIndex;
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return DoubleSubIterator.of(upstream.iterator(), startIndex, endIndex);
    }

    @Override
    public DoubleSequence skip(final long n) {
        return n >= count ? PrimitiveIterators::emptyDoubleIterator : new DoubleSubSequence(upstream, startIndex + n, endIndex);
    }

    @Override
    public DoubleSubSequence take(final long n) {
        return n >= count ? this : new DoubleSubSequence(upstream, startIndex, startIndex + 1);
    }

    @Override
    public long count() {
        return count;
    }
}
