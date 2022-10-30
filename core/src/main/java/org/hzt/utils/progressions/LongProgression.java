package org.hzt.utils.progressions;

import org.hzt.utils.numbers.LongX;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var longs = (LongProgression) o;
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
                final var value = next;
                next += step;
                return value;
            }

            @Override
            public boolean hasNext() {
                return (step > 0) ? (next <= endInclusive) : (next >= endInclusive);
            }
        };
    }

    public @NotNull Long start() {
        return start;
    }

    public @NotNull Long endInclusive() {
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
