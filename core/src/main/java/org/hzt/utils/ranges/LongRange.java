package org.hzt.utils.ranges;

import org.hzt.utils.progressions.LongProgression;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

public final class LongRange extends LongProgression implements ClosedRange<Long> {

    private LongRange(final long start, final long endInclusive, final long step) {
        super(start, endInclusive, step);
    }

    public static LongRange empty() {
        return new LongRange(0, -1, 1);
    }

    public static LongRange until(final long end) {
        return of(0, end, 1);
    }

    public static LongRange of(final long start, final long endExclusive) {
        return closed(start, endExclusive - 1L);
    }

    public static LongRange of(final long start, final long endExclusive, final long step) {
        return closed(start, endExclusive - 1L, step);
    }

    public static LongSequence closed(final long endInclusive) {
        return closed(0, endInclusive);
    }

    public static LongRange closed(final long start, final long endInclusive) {
        return closed(start, endInclusive, 1);
    }

    public static LongRange closed(final long start, final long endInclusive, final long step) {
        if (start >= endInclusive) {
            return LongRange.empty();
        }
        return new LongRange(start, endInclusive, step);
    }

    public boolean containsAll(final long @NotNull ... array) {
        return LongSequence.of(array).all(this::contains);
    }
}
