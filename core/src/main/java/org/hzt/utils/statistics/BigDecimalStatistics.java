package org.hzt.utils.statistics;

import org.hzt.utils.Transformable;
import org.hzt.utils.numbers.BigDecimalX;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class BigDecimalStatistics extends BigDecimalSummaryStatistics
        implements NumberStatistics, Transformable<BigDecimalStatistics> {

    private BigDecimal sumOfSquare = BigDecimal.ZERO;

    public BigDecimalStatistics() {
        super();
    }

    public BigDecimalStatistics(final long count, final BigDecimal sum, final BigDecimal min, final BigDecimal max) {
        super(count, sum, min, max);
    }

    @Override
    public void accept(final BigDecimal value) {
        super.accept(value);
        final var squareValue = value.multiply(value);
        sumOfSquare = sumOfSquare.add(squareValue);
    }

    public BigDecimalStatistics combine(final BigDecimalStatistics other) {
        super.combine(other);
        sumOfSquare = sumOfSquare.add(other.sumOfSquare);
        return this;
    }

    private BigDecimal getSumOfSquare() {
        return sumOfSquare;
    }

    public BigDecimalStatistics getStatistics() {
        return this;
    }

    public BigDecimalX getStandardDeviation() {
        return getStandardDeviation(2, RoundingMode.HALF_UP, MathContext.DECIMAL32);
    }

    public BigDecimalX getStandardDeviation(final int scale, final RoundingMode roundingMode) {
        return getStandardDeviation(scale, roundingMode, MathContext.DECIMAL128);
    }

    public BigDecimalX getStandardDeviation(final int scale, final RoundingMode roundingMode, final MathContext mathContext) {
        final BigDecimal average = getAverage(scale, roundingMode);
        final var subtract = (getSumOfSquare().divide(BigDecimal.valueOf(getCount()), scale, roundingMode))
                .subtract(average.multiply(average));
        final var stdDeviation = getCount() > 0 ?
                subtract.sqrt(mathContext) : BigDecimal.ZERO;
        return BigDecimalX.of(stdDeviation.setScale(scale, roundingMode));
    }

    @Override
    public String toString() {
        return String.format(
                "%s{count=%d, sum=%s, min=%s, average=%s, max=%s, standard deviation=%s}",
                this.getClass().getSimpleName(),
                getCount(),
                getSum(),
                getMin(),
                getAverage(),
                getMax(),
                getStandardDeviation());
    }

    @Override
    public BigDecimalStatistics get() {
        return this;
    }
}
