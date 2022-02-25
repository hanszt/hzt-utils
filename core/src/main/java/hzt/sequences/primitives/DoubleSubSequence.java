package hzt.sequences.primitives;

import hzt.iterators.primitives.DoubleSubIterator;
import hzt.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

import static hzt.PreConditions.require;

final class DoubleSubSequence implements DoubleSkipTakeSequence {

    private final DoubleSequence upstream;
    private final long startIndex;
    private final long endIndex;
    private final long count;

    DoubleSubSequence(DoubleSequence upstream, long startIndex, long endIndex) {
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
    public PrimitiveIterator.OfDouble iterator() {
        return DoubleSubIterator.of(upstream.iterator(), startIndex, endIndex);
    }

    @Override
    public DoubleSequence skip(long n) {
        return n >= count ? PrimitiveIterators::emptyDoubleIterator : new DoubleSubSequence(upstream, startIndex + n, endIndex);
    }

    @Override
    public DoubleSubSequence take(long n) {
        return n >= count ? this : new DoubleSubSequence(upstream, startIndex, startIndex + 1);
    }

    @Override
    public long count() {
        return count;
    }
}
