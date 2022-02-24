package hzt.progressions;

import hzt.numbers.LongX;
import hzt.sequences.primitives.LongSequence;

import java.util.PrimitiveIterator;

public class LongProgression implements LongSequence {

    private final long start;
    private final long endInclusive;
    private final long step;

    protected LongProgression(long start, long endInclusive, long step) {
        if (step == 0) {
            throw new IllegalArgumentException("step must be none-zero");
        }
        if (step == Long.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
    }

    public static LongX from(long start) {
        return LongX.of(start);
    }

    public static LongProgression downTo(long l) {
        if (l > 0) {
            return new LongProgression(0L, 0, -1);
        }
        return new LongProgression(0, l + 1, -1);
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return new PrimitiveIterator.OfLong() {
            private long next = start;
            @Override
            public long nextLong() {
                final long value = next;
                next += step;
                return value;
            }

            @Override
            public boolean hasNext() {
                return (step > 0) ? (next <= endInclusive) : (next >= endInclusive);
            }
        };
    }

    protected Long start() {
        return start;
    }

    protected Long endInclusive() {
        return endInclusive;
    }
}
