package org.hzt.utils.statistics;

import org.hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.IntSummaryStatistics;
import java.util.Objects;

public final class IntStatistics extends IntSummaryStatistics
        implements NumberStatistics, Transformable<IntStatistics> {

    private long sumOfSquare = 0;

    public IntStatistics() {
        super();
    }

    public IntStatistics(long count, int min, int max, long sum) {
        super(count, min, max, sum);
    }

    @Override
    public void accept(int value) {
        super.accept(value);
        var squareValue = (long) value * (long) value;
        sumOfSquare += squareValue;
    }

    public IntStatistics combine(IntStatistics other) {
        super.combine(other);
        sumOfSquare = sumOfSquare + other.sumOfSquare;
        return this;
    }

    public Double getStandardDeviation() {
        final var average = getAverage();
        final var sumOfSquareAverage = (double) sumOfSquare / getCount();
        return getCount() > 0 ? Math.sqrt(sumOfSquareAverage - average * average) : 0.0D;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (IntStatistics) o;
        return getCount() == that.getCount() &&
                Double.compare(getAverage(), that.getAverage()) == 0 &&
                sumOfSquare == that.sumOfSquare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCount(), getAverage(), sumOfSquare);
    }

    @Override
    public String toString() {
        return String.format(
                "%s{count=%d, sum=%d, min=%d, average=%f, max=%d, standard deviation=%f}",
                this.getClass().getSimpleName(),
                getCount(),
                getSum(),
                getMin(),
                getAverage(),
                getMax(),
                getStandardDeviation());
    }

    @Override
    public @NotNull IntStatistics get() {
        return this;
    }
}
