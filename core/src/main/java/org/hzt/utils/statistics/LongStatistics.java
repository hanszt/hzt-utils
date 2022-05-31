package org.hzt.utils.statistics;

import org.hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.LongSummaryStatistics;
import java.util.Objects;

public final class LongStatistics extends LongSummaryStatistics
        implements NumberStatistics, Transformable<LongStatistics> {

    private long sumOfSquare = 0;

    public LongStatistics() {
        super();
    }

    @Override
    public void accept(long value) {
        super.accept(value);
        long squareValue = value * value;
        sumOfSquare += squareValue;
    }

    public LongStatistics combine(LongStatistics other) {
        super.combine(other);
        sumOfSquare = sumOfSquare + other.sumOfSquare;
        return this;
    }

    public Double getStandardDeviation() {
        final double average = getAverage();
        final double sumOfSquareAverage = (double) sumOfSquare / getCount();
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
        LongStatistics that = (LongStatistics) o;
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
    public @NotNull LongStatistics get() {
        return this;
    }
}
