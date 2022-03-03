package org.hzt.utils.sequences.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

final class LongTakeSequence implements LongSkipTakeSequence {

    private final LongSequence upstream;
    private final long count;

    LongTakeSequence(LongSequence upstream, long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public LongSequence skip(long n) {
        return n >= count ? PrimitiveIterators::emptyLongIterator : new LongSubSequence(upstream, n, count);
    }

    @Override
    public LongSequence take(long n) {
        return n >= count ? this : new LongTakeSequence(upstream, n);
    }

    @NotNull
    @Override
    public PrimitiveIterator.OfLong iterator() {
        return new PrimitiveIterator.OfLong() {
            private final PrimitiveIterator.OfLong iterator = upstream.iterator();
            private long left = count;
            @Override
            public boolean hasNext() {
                return left > 0 && iterator.hasNext();
            }
            @Override
            public long nextLong() {
                if (left == 0) {
                    throw new NoSuchElementException();
                }
                left--;
                return iterator.nextLong();
            }
        };
    }

    @Override
    public long count() {
        return count;
    }
}
