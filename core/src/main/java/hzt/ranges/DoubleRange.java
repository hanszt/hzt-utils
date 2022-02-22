package hzt.ranges;

import hzt.progressions.DoubleProgression;
import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

public final class DoubleRange extends DoubleProgression implements ClosedRange<Double> {

    public DoubleRange(double start, double endInclusive) {
        super(start, endInclusive, 1);
    }

    public boolean containsAll(double @NotNull ... array) {
        return DoubleSequence.of(array).all(this::contains);
    }

    @Override
    public Double start() {
        return getStart();
    }

    @Override
    public Double endInclusive() {
        return getEndInclusive();
    }
}
