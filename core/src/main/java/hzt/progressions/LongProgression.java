package hzt.progressions;

import hzt.sequences.primitives.LongSequence;

import java.util.PrimitiveIterator;

public class LongProgression implements LongSequence {

    private final long start;
    private final long endInclusive;
    private final long step;

    public LongProgression(long start, long endInclusive, long step) {
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

    protected long getStart() {
        return start;
    }

    protected long getEndInclusive() {
        return endInclusive;
    }
}
