package hzt.ranges;

import hzt.progressions.LongProgression;

public final class LongRange extends LongProgression implements ClosedRange<Long> {

    private LongRange(long start, long endInclusive) {
        super(start, endInclusive, 1);
    }

    public static LongRange of(int start, int endInclusive) {
        return new LongRange(start, endInclusive);
    }

    @Override
    public Long start() {
        return getStart();
    }

    @Override
    public Long endInclusive() {
        return getEndInclusive();
    }
}
