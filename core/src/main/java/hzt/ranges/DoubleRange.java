package hzt.ranges;

import hzt.progressions.DoubleProgression;
import hzt.sequences.primitives.DoubleSequence;
import org.jetbrains.annotations.NotNull;

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

    public boolean containsAll(double @NotNull ... array) {
        return DoubleSequence.of(array).all(this::contains);
    }

    @Override
    public @NotNull Double start() {
        return super.start();
    }

    @Override
    public @NotNull Double endInclusive() {
        return super.endInclusive();
    }
}
