package org.hzt.utils.iterators.primitives;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

public final class LongRangeIterator implements PrimitiveIterator.OfLong {

    private final long start;
    private final long endInclusive;
    private final long step;

    private long nextLong;

    private LongRangeIterator(long start, long endInclusive, long step) {
        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
        this.nextLong = start;
    }

    public static PrimitiveIterator.OfLong of(long start, long endInclusive, long step) {
        return new LongRangeIterator(start, endInclusive, step);
    }

    @Override
    public long nextLong() {
        if (checkHasNext()) {
            var current = nextLong;
            nextLong = nextLong + (start < endInclusive ? step : -step);
            return current;
        }
        throw new NoSuchElementException(nextLong + " is outside defined range");
    }

    @Override
    public boolean hasNext() {
        return checkHasNext();
    }

    private boolean checkHasNext() {
        if (start <= endInclusive) {
            return nextLong <= endInclusive;
        }
        return nextLong >= endInclusive;
    }
}
