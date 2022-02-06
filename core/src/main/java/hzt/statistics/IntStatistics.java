package hzt.statistics;

import hzt.utils.Transformable;

import java.util.IntSummaryStatistics;

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
        long squareValue = (long) value * (long) value;
        sumOfSquare += squareValue;
    }

    public IntStatistics combine(IntStatistics other) {
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
    public IntStatistics get() {
        return this;
    }
}
