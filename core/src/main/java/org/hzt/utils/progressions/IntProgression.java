package org.hzt.utils.progressions;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.PrimitiveIterator;

public class IntProgression implements IntSequence {

    private final int start;
    private final int endInclusive;
    private final int step;

    protected IntProgression(int start, int endInclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step must be none-zero");
        }
        if (step == Integer.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Int.MIN_VALUE to avoid overflow on negation.");
        }
        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
    }

    public static IntProgression empty() {
        return new IntProgression(0, -1, 1);
    }

    public static IntX from(int start) {
        return IntX.of(start);
    }

    public static IntProgression until(int end) {
        return new IntProgression(0, end, 1);
    }

    public static IntProgression until(int end, int step) {
        return new IntProgression(0, end, step);
    }

    public static IntProgression downTo(int endInclusive) {
        if (endInclusive > 0) {
            return new IntProgression(0, 0, -1);
        }
        return new IntProgression(0, endInclusive, -1);
    }

    public static IntProgression closed(int start, int endInclusive, int step) {
        return new IntProgression(start, endInclusive, step);
    }

    public static IntProgression closed(int start, int endInclusive) {
        return new IntProgression(start, endInclusive, -1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var integers = (IntProgression) o;
        return start == integers.start && endInclusive == integers.endInclusive && getStep() == integers.getStep();
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, endInclusive, getStep());
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int next = start;
            @Override
            public int nextInt() {
                if (step == 0) {
                    throw new IllegalArgumentException("step may not be zero");
                }
                final var value = next;
                next += step;
                return value;
            }

            @Override
            public boolean hasNext() {
                return step != 0 && (step > 0) ? (next <= endInclusive) : (next >= endInclusive);
            }
        };
    }

    public @NotNull Integer start() {
        return start;
    }

    public @NotNull Integer endInclusive() {
        return endInclusive;
    }

    public int getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "IntProgression{" +
                "start=" + start +
                ", endInclusive=" + endInclusive +
                ", step=" + step +
                '}';
    }
}
