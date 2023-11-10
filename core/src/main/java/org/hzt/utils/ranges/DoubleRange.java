package org.hzt.utils.ranges;

import org.hzt.utils.progressions.DoubleProgression;
import org.hzt.utils.sequences.primitives.DoubleSequence;

public final class DoubleRange extends DoubleProgression implements ClosedRange<Double> {

    private DoubleRange(double start, double endInclusive) {
        super(start, endInclusive, 1);
    }

    private DoubleRange(double start, double endInclusive, double step) {
        super(start, endInclusive, step);
    }

    public static DoubleRange closed(double start, double endInclusive) {
        return new DoubleRange(start, endInclusive);
    }

    public static DoubleRange closed(double start, double endInclusive, double step) {
        return new DoubleRange(start, endInclusive, step);
    }

    public boolean containsAll(double... array) {
        return DoubleSequence.of(array).all(this::contains);
    }
}
