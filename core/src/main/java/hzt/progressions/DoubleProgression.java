package hzt.progressions;

import hzt.sequences.primitives.DoubleSequence;

import java.util.PrimitiveIterator;

public class DoubleProgression implements DoubleSequence {

    private final double start;
    private final double endInclusive;
    private final double step;

    public DoubleProgression(double start, double endInclusive, double step) {
        if (step == 0) {
            throw new IllegalArgumentException("step must be none-zero");
        }
        this.start = start;
        this.endInclusive = endInclusive;
        this.step = step;
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

    protected double getStart() {
        return start;
    }

    protected double getEndInclusive() {
        return endInclusive;
    }
}
