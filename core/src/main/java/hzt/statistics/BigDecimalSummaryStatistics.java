package hzt.statistics;

import hzt.numbers.BigDecimalX;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

public class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {

    public static final BigDecimal INIT_MIN_VALUE = BigDecimal.valueOf(Double.MAX_VALUE);
    public static final BigDecimal INIT_MAX_VALUE = BigDecimal.valueOf(-Double.MAX_VALUE);

    private long count;
    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal min = INIT_MIN_VALUE;
    private BigDecimal max = INIT_MAX_VALUE;

    public BigDecimalSummaryStatistics() {
        super();
    }

    public BigDecimalSummaryStatistics(long count, BigDecimal sum, BigDecimal min, BigDecimal max) {
        if (count < 0L) {
            throw new IllegalArgumentException("Negative count value");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Minimum greater than maximum");
        }
        this.count = count;
        this.sum = sum != null ? sum : BigDecimal.ZERO;
        this.min = min;
        this.max = max != null ? max :  INIT_MAX_VALUE;
    }

    @Override
    public void accept(BigDecimal value) {
        if (value != null) {
            ++count;
            sum = sum.add(value);
            min = min.compareTo(value) < 0 ? min : value;
            max = max.compareTo(value) > 0 ? max : value;
        }
    }

    public BigDecimalSummaryStatistics combine(BigDecimalSummaryStatistics other) {
        count += other.count;
        sum = sum.add(other.sum);
        min = min.compareTo(other.min) < 0 ? min : other.min;
        max = max.compareTo(other.max) > 0 ? max : other.max;
        return other;
    }

    public BigDecimalX getAverage() {
        final int SCALE = 2;
        return getAverage(SCALE);
    }

    public BigDecimalX getAverage(int scale) {
        return getAverage(scale, RoundingMode.HALF_UP);
    }

    public BigDecimalX getAverage(int scale, RoundingMode roundingMode) {
        BigDecimal countAsBD = BigDecimal.valueOf(this.count);
        return BigDecimalX.of(BigDecimal.ZERO.compareTo(countAsBD) == 0 ? BigDecimal.ZERO : sum.divide(countAsBD, scale, roundingMode));
    }

    public long getCount() {
        return count;
    }

    public BigDecimalX getSum() {
        return BigDecimalX.of(sum);
    }

    public BigDecimalX getMin() {
        return BigDecimalX.of(min);
    }

    public BigDecimalX getMax() {
        return BigDecimalX.of(max);
    }

    @Override
    public String toString() {
        return "BigDecimalSummaryStatistics[" +
                "count=" + count + ", " +
                "sum=" + sum + ", " +
                "average=" + getAverage() + ", " +
                "min=" + min + ", " +
                "max=" + max + ']';
    }
}
