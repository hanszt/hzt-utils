package org.hzt.utils.sequences.primitives;

import org.hzt.utils.PreConditions;

import java.util.PrimitiveIterator;

final class LongSkipSequence implements LongSkipTakeSequence {

    private final LongSequence upstream;
    private final long count;

    LongSkipSequence(final LongSequence upstream, final long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public LongSequence skip(final long n) {
        final long n1 = count + n;
        return n1 < 0 ? new LongSkipSequence(this, n) : new LongSkipSequence(upstream, n1);
    }

    @Override
    public LongSequence take(final long n) {
        final long n1 = count + n;
        return n1 < 0 ? new LongTakeSequence(this, n) : new LongSubSequence(upstream, count, n1);
    }

    @Override
    public long count() {
        return count;
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return new PrimitiveIterator.OfLong() {
            private final PrimitiveIterator.OfLong iterator = upstream.iterator();
            private long left = count;

            private void skip() {
                while (left > 0 && iterator.hasNext()) {
                    iterator.nextLong();
                    left--;
                }
            }

            @Override
            public boolean hasNext() {
                skip();
                return iterator.hasNext();
            }

            @Override
            public long nextLong() {
                skip();
                return iterator.nextLong();
            }
        };
    }
}
