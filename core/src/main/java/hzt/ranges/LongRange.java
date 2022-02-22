package hzt.ranges;

import hzt.progressions.LongProgression;
import hzt.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

public final class LongRange extends LongProgression implements ClosedRange<Long> {

    private LongRange(long start, long endInclusive) {
        super(start, endInclusive, 1);
    }

    public static LongRange of(int start, int endExclusive) {
        return new LongRange(start, endExclusive - 1L);
    }

    public static LongRange closed(int start, int endInclusive) {
        return new LongRange(start, endInclusive);
    }

    public boolean containsAll(long @NotNull ... array) {
        return LongSequence.of(array).all(this::contains);
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
