package org.hzt.utils.ranges;

import org.hzt.utils.progressions.LongProgression;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

public final class LongRange extends LongProgression implements ClosedRange<Long> {

    private LongRange(long start, long endInclusive, long step) {
        super(start, endInclusive, step);
    }

    public static LongRange empty() {
        return new LongRange(0, -1, 1);
    }

    public static LongRange until(long end) {
        return of(0, end, 1);
    }

    public static LongRange of(long start, long endExclusive) {
        return closed(start, endExclusive - 1L);
    }

    public static LongRange of(long start, long endExclusive, long step) {
        return closed(start, endExclusive - 1L, step);
    }

    public static LongSequence closed(long endInclusive) {
        return closed(0, endInclusive);
    }

    public static LongRange closed(long start, long endInclusive) {
        return closed(start, endInclusive, 1);
    }

    public static LongRange closed(long start, long endInclusive, long step) {
        if (start >= endInclusive) {
            return LongRange.empty();
        }
        return new LongRange(start, endInclusive, step);
    }

    public boolean containsAll(long @NotNull ... array) {
        return LongSequence.of(array).all(this::contains);
    }

    @Override
    public @NotNull Long start() {
        return super.start();
    }

    @Override
    public @NotNull Long endInclusive() {
        return super.endInclusive();
    }
}
