package hzt.ranges;

import hzt.progressions.IntProgression;

public final class IntRange extends IntProgression implements ClosedRange<Integer> {

    private IntRange(int start, int endInclusive) {
        super(start, endInclusive, 1);
    }

    public static IntRange closed(int start, int endExclusive) {
        return new IntRange(start, endExclusive - 1);
    }

    public static IntRange of(int start, int endInclusive) {
        return new IntRange(start, endInclusive);
    }

    @Override
    public Integer start() {
        return getStart();
    }

    @Override
    public Integer endInclusive() {
        return getEndInclusive();
    }
}
