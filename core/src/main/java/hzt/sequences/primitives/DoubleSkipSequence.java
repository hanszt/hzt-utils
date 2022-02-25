package hzt.sequences.primitives;

import hzt.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

final class DoubleSkipSequence implements DoubleSkipTakeSequence {

    private final DoubleSequence upstream;
    private final long count;

    DoubleSkipSequence(DoubleSequence upstream, long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public DoubleSequence skip(long n) {
        final long n1 = count + n;
        return n1 < 0 ? new DoubleSkipSequence(this, n) : new DoubleSkipSequence(upstream, n1);
    }

    @Override
    public DoubleSequence take(long n) {
        final long n1 = count + n;
        return n1 < 0 ? new DoubleTakeSequence(this, n) : new DoubleSubSequence(upstream, count, n1);
    }

    @Override
    public long count() {
        return count;
    }

    @NotNull
    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return new PrimitiveIterator.OfDouble() {
            private final OfDouble iterator = upstream.iterator();
            private long left = count;

            private void skip() {
                while (left > 0 && iterator.hasNext()) {
                    iterator.nextDouble();
                    left--;
                }
            }

            @Override
            public boolean hasNext() {
                skip();
                return iterator.hasNext();
            }

            @Override
            public double nextDouble() {
                skip();
                return iterator.nextDouble();
            }
        };
    }
}
