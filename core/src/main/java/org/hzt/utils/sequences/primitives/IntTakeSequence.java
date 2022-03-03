package org.hzt.utils.sequences.primitives;

import org.hzt.utils.PreConditions;
import org.hzt.utils.iterators.primitives.PrimitiveIterators;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

final class IntTakeSequence implements IntSkipTakeSequence {

    private final IntSequence upstream;
    private final long count;

    IntTakeSequence(IntSequence upstream, long count) {
        PreConditions.require(count >= 0);
        this.upstream = upstream;
        this.count = count;
    }

    @Override
    public IntSequence skip(long n) {
        return n >= count ? PrimitiveIterators::emptyIntIterator : new IntSubSequence(upstream, n, count);
    }

    @Override
    public IntSequence take(long n) {
        return n >= count ? this : new IntTakeSequence(upstream, n);
    }

    @NotNull
    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private final OfInt iterator = upstream.iterator();
            private long left = count;
            @Override
            public boolean hasNext() {
                return left > 0 && iterator.hasNext();
            }
            @Override
            public int nextInt() {
                if (left == 0) {
                    throw new NoSuchElementException();
                }
                left--;
                return iterator.nextInt();
            }
        };
    }

    @Override
    public long count() {
        return count;
    }
}
