package hzt.sequences.primitives;

import hzt.PreConditions;
import hzt.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

final class DoubleTakeSequence implements DoubleSkipTakeSequence {

    private final DoubleSequence upstream;
    private final long count;

    DoubleTakeSequence(DoubleSequence upstream, long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public DoubleSequence skip(long n) {
        return n >= count ? PrimitiveIterators::emptyDoubleIterator : new DoubleSubSequence(upstream, n, count);
    }

    @Override
    public DoubleSequence take(long n) {
        return n >= count ? this : new DoubleTakeSequence(upstream, n);
    }

    @NotNull
    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return new PrimitiveIterator.OfDouble() {
            private final OfDouble iterator = upstream.iterator();
            private long left = count;
            @Override
            public boolean hasNext() {
                return left > 0 && iterator.hasNext();
            }
            @Override
            public double nextDouble() {
                if (left == 0) {
                    throw new NoSuchElementException();
                }
                left--;
                return iterator.nextDouble();
            }
        };
    }

    @Override
    public long count() {
        return count;
    }
}
