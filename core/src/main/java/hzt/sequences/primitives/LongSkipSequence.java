package hzt.sequences.primitives;

import hzt.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

final class LongSkipSequence implements LongSkipTakeSequence {

    private final LongSequence upstream;
    private final long count;

    LongSkipSequence(LongSequence upstream, long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public LongSequence skip(long n) {
        final long n1 = count + n;
        return n1 < 0 ? new LongSkipSequence(this, n) : new LongSkipSequence(upstream, n1);
    }

    @Override
    public LongSequence take(long n) {
        final long n1 = count + n;
        return n1 < 0 ? new LongTakeSequence(this, n) : new LongSubSequence(upstream, count, n1);
    }

    @Override
    public long count() {
        return count;
    }

    @NotNull
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
