package org.hzt.utils.statistics;

import org.hzt.utils.Transformable;

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

    @Override
    public void accept(final double value) {
        super.accept(value);
        final double squareValue = value * value;
        simpleSumOfSquare += squareValue;
        sumOfSquareWithCompensation(squareValue);
    }

    public DoubleStatistics combine(final DoubleStatistics other) {
        super.combine(other);
        simpleSumOfSquare += other.simpleSumOfSquare;
        sumOfSquareWithCompensation(other.sumOfSquare);
        sumOfSquareWithCompensation(other.sumOfSquareCompensation);
        return this;
    }

    private void sumOfSquareWithCompensation(final double value) {
        final double tmp = value - sumOfSquareCompensation;
        final double velvel = sumOfSquare + tmp; // Little wolf of rounding error
        sumOfSquareCompensation = (velvel - sumOfSquare) - tmp;
        sumOfSquare = velvel;
    }

    public double getSumOfSquare() {
        final double correctedSumOfSquare = sumOfSquare + sumOfSquareCompensation;
        if (Double.isNaN(correctedSumOfSquare) && Double.isInfinite(simpleSumOfSquare)) {
            return simpleSumOfSquare;
        }
        return correctedSumOfSquare;
    }

    public Double getStandardDeviation() {
        final double average = getAverage();
        return getCount() > 0 ? Math.sqrt((getSumOfSquare() / getCount()) - average * average) : 0.0D;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DoubleStatistics that = (DoubleStatistics) o;
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
    public DoubleStatistics get() {
        return this;
    }
}
