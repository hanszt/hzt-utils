package hzt.ranges;

import hzt.sequences.primitives.DoubleSequence;

import java.util.PrimitiveIterator;

public final class DoubleRange implements ClosedRange<Double>, DoubleSequence {

    private final double start;
    private final double endInclusive;

    public DoubleRange(double start, double endInclusive) {
        this.start = start;
        this.endInclusive = endInclusive;
    }

    @Override
    public Double start() {
        return start;
    }

    @Override
    public Double endInclusive() {
        return endInclusive;
    }

    @Override
    public PrimitiveIterator.OfDouble iterator() {
        return null;
    }
}
