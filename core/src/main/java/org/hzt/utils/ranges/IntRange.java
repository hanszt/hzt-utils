package org.hzt.utils.ranges;

import org.hzt.utils.PreConditions;
import org.hzt.utils.progressions.IntProgression;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

public final class IntRange extends IntProgression implements ClosedRange<Integer> {

    private IntRange(int start, int endInclusive, int step) {
        super(start, endInclusive, step);
    }

    public static IntRange empty() {
        return new IntRange(0, -1, 1);
    }

    public static IntRange of(int start, int endExclusive, int step) {
        return IntRange.closed(start, endExclusive - 1, step);
    }

    public static IntRange of(int start, int endExclusive) {
        if (start >= endExclusive) {
            return IntRange.empty();
        }
        return IntRange.of(start, endExclusive, 1);
    }

    public static IntRange closed(int start, int endInclusive) {
        return IntRange.closed(start, endInclusive, 1);
    }

    public static IntRange closed(int start, int endInclusive, int step) {
        PreConditions.require(step > 0, () -> "Step must be greater than zero");
        if (start > endInclusive) {
            return IntRange.empty();
        }
        return new IntRange(start, endInclusive, step);
    }

    public boolean containsAll(int @NotNull ... array) {
        return IntSequence.of(array).all(this::contains);
    }

    @Override
    public @NotNull Integer start() {
        return super.start();
    }

    @Override
    public @NotNull Integer endInclusive() {
        return super.endInclusive();
    }
}
