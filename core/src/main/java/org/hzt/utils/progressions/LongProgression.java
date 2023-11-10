package org.hzt.utils.progressions;

import org.hzt.utils.numbers.LongX;
import org.hzt.utils.sequences.primitives.LongSequence;

import java.util.Objects;
import java.util.PrimitiveIterator;

public class LongProgression implements LongSequence {

    private final long start;
    private final long endInclusive;
    private final long step;

    protected LongProgression(final long start, final long endInclusive, final long step) {
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

    public static LongX from(final long start) {
        return LongX.of(start);
    }

    public static LongProgression downTo(final long l) {
        if (l > 0) {
            return new LongProgression(0L, 0, -1);
        }
        return new LongProgression(0, l + 1, -1);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LongProgression longs = (LongProgression) o;
        return start == longs.start && endInclusive == longs.endInclusive && step == longs.step;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, endInclusive, step);
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

    public Long start() {
        return start;
    }

    public Long endInclusive() {
        return endInclusive;
    }

    public long getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "LongProgression{" +
                "start=" + start +
                ", endInclusive=" + endInclusive +
                ", step=" + step +
                '}';
    }
}
