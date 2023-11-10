package org.hzt.utils.progressions;

import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.primitives.DoubleSequence;

import java.util.Objects;
import java.util.PrimitiveIterator;

public class DoubleProgression implements DoubleSequence {

    private final double start;
    private final double endInclusive;
    private final double step;

    public DoubleProgression(final double start, final double endInclusive, final double step) {
        if (Double.compare(step, 0) == 0) {
            throw new IllegalArgumentException("step must be none-zero");
        }
        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
    }

    static DoubleX from(final double start) {
        return DoubleX.of(start);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DoubleProgression doubles = (DoubleProgression) o;
        return Double.compare(doubles.start, start) == 0 &&
                Double.compare(doubles.endInclusive, endInclusive) == 0 &&
                Double.compare(doubles.getStep(), getStep()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, endInclusive, getStep());
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return new PrimitiveIterator.OfDouble() {
            private double next = start;

            @Override
            public double nextDouble() {
                final double value = next;
                next += step;
                return value;
            }

            @Override
            public boolean hasNext() {
                return (step > 0) ? (next <= endInclusive) : (next >= endInclusive);
            }
        };
    }

    public Double start() {
        return start;
    }

    public Double endInclusive() {
        return endInclusive;
    }

    public double getStep() {
        return step;
    }

    @Override
    public String toString() {
        return "DoubleProgression{" +
                "start=" + start +
                ", endInclusive=" + endInclusive +
                ", step=" + step +
                '}';
    }
}
