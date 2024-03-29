package org.hzt.utils.ranges;

import org.hzt.utils.PreConditions;
import org.hzt.utils.progressions.IntProgression;
import org.hzt.utils.sequences.primitives.IntSequence;

public final class IntRange extends IntProgression implements ClosedRange<Integer> {

    private IntRange(final int start, final int endInclusive, final int step) {
        super(start, endInclusive, step);
    }

    public static IntRange empty() {
        return new IntRange(0, -1, 1);
    }

    public static IntRange of(final int start, final int endExclusive, final int step) {
        return IntRange.closed(start, endExclusive - step, step);
    }

    public static IntRange of(final int start, final int endExclusive) {
        if (start >= endExclusive) {
            return IntRange.empty();
        }
        return IntRange.of(start, endExclusive, 1);
    }

    public static IntRange closed(final int start, final int endInclusive) {
        return IntRange.closed(start, endInclusive, 1);
    }

    public static IntRange closed(final int start, final int endInclusive, final int step) {
        PreConditions.require(step > 0, () -> "Step must be greater than zero");
        if (start > endInclusive) {
            return IntRange.empty();
        }
        return new IntRange(start, endInclusive, step);
    }

    public boolean containsAll(final int... array) {
        return IntSequence.of(array).all(this::contains);
    }

    public boolean contains(final int value) {
        return start() <= value && value <= endInclusive();
    }
    @Override
    public Integer start() {
        return super.start();
    }

    @Override
    public Integer endInclusive() {
        return super.endInclusive();
    }
}
