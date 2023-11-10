package org.hzt.utils.sequences.primitives;

import org.hzt.utils.PreConditions;

import java.util.PrimitiveIterator;

final class IntSkipSequence implements IntSkipTakeSequence {

    private final IntSequence upstream;
    private final long count;

    IntSkipSequence(final IntSequence upstream, final long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public IntSequence skip(final long n) {
        final long n1 = count + n;
        return n1 < 0 ? new IntSkipSequence(this, n) : new IntSkipSequence(upstream, n1);
    }

    @Override
    public IntSequence take(final long n) {
        final long n1 = count + n;
        return n1 < 0 ? new IntTakeSequence(this, n) : new IntSubSequence(upstream, count, n1);
    }

    @Override
    public long count() {
        return count;
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private final OfInt iterator = upstream.iterator();
            private long left = count;

            private void skip() {
                while (left > 0 && iterator.hasNext()) {
                    iterator.nextInt();
                    left--;
                }
            }

            @Override
            public boolean hasNext() {
                skip();
                return iterator.hasNext();
            }

            @Override
            public int nextInt() {
                skip();
                return iterator.nextInt();
            }
        };
    }
}
