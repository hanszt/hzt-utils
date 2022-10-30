package org.hzt.utils.statistics;

import org.hzt.utils.Transformable;
import org.jetbrains.annotations.NotNull;

import java.util.DoubleSummaryStatistics;
import java.util.Objects;

public final class DoubleStatistics extends DoubleSummaryStatistics
        implements NumberStatistics, Transformable<DoubleStatistics> {

    private double sumOfSquare = 0.0D;
    private double sumOfSquareCompensation; // Low order bits of sum
    private double simpleSumOfSquare; // Used to compute right sum for non-finite inputs

    public DoubleStatistics() {
        super();
    }

    public DoubleStatistics(long count, double min, double max, double sum) {
        super(count, min, max, sum);
    }

    @Override
    public void accept(double value) {
        super.accept(value);
        var squareValue = value * value;
        simpleSumOfSquare += squareValue;
        sumOfSquareWithCompensation(squareValue);
    }

    public DoubleStatistics combine(DoubleStatistics other) {
        super.combine(other);
        simpleSumOfSquare += other.simpleSumOfSquare;
        sumOfSquareWithCompensation(other.sumOfSquare);
        sumOfSquareWithCompensation(other.sumOfSquareCompensation);
        return this;
    }

    private void sumOfSquareWithCompensation(double value) {
        var tmp = value - sumOfSquareCompensation;
        var velvel = sumOfSquare + tmp; // Little wolf of rounding error
        sumOfSquareCompensation = (velvel - sumOfSquare) - tmp;
        sumOfSquare = velvel;
    }

    public double getSumOfSquare() {
        var correctedSumOfSquare =  sumOfSquare + sumOfSquareCompensation;
        if (Double.isNaN(correctedSumOfSquare) && Double.isInfinite(simpleSumOfSquare)) {
            return simpleSumOfSquare;
        }
        return correctedSumOfSquare;
    }

    public Double getStandardDeviation() {
        final var average = getAverage();
        return getCount() > 0 ? Math.sqrt((getSumOfSquare() / getCount()) - average * average) : 0.0D;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        var that = (DoubleStatistics) o;
        return getCount() == that.getCount() &&
                Double.compare(getAverage(), that.getAverage()) == 0 &&
                Double.compare(that.getSumOfSquare(), getSumOfSquare()) == 0 &&
                Double.compare(that.sumOfSquareCompensation, sumOfSquareCompensation) == 0 &&
                Double.compare(that.simpleSumOfSquare, simpleSumOfSquare) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCount(), getAverage(), getSumOfSquare(), sumOfSquareCompensation, simpleSumOfSquare);
    }

    @Override
    public String toString() {
        return String.format(
                "%s{count=%d, sum=%f, min=%f, average=%f, max=%f, standard deviation=%f}",
                this.getClass().getSimpleName(),
                getCount(),
                getSum(),
                getMin(),
                getAverage(),
                getMax(),
                getStandardDeviation());
    }

    @Override
    public @NotNull DoubleStatistics get() {
        return this;
    }
}
