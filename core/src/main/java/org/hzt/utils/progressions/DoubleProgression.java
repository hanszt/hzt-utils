package org.hzt.utils.progressions;

import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

import java.util.PrimitiveIterator;

public class DoubleProgression implements DoubleSequence {

    private final double start;
    private final double endInclusive;
    private final double step;

    public DoubleProgression(double start, double endInclusive, double step) {
        if (Double.compare(step, 0) == 0) {
            throw new IllegalArgumentException("step must be none-zero");
        }
        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
    }

    static DoubleX from(double start) {
        return DoubleX.of(start);
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

    protected @NotNull Double start() {
        return start;
    }

    protected @NotNull Double endInclusive() {
        return endInclusive;
    }
}
