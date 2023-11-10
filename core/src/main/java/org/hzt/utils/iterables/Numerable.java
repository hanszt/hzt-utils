package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.numbers.BigDecimalX;
import org.hzt.utils.statistics.BigDecimalStatistics;
import org.hzt.utils.statistics.DoubleStatistics;
import org.hzt.utils.statistics.IntStatistics;
import org.hzt.utils.statistics.LongStatistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
public interface Numerable<T> extends Iterable<T> {

    default long count(Predicate<T> predicate) {
        return IterableXHelper.count(this, predicate);
    }

    default long count() {
        return count(It::noFilter);
    }

    default long intSumOf(ToIntFunction<? super T> selector) {
        long sum = 0;
        for (T t : this) {
            if (t != null) {
                sum += selector.applyAsInt(t);
            }
        }
        return sum;
    }

    default long longSumOf(ToLongFunction<? super T> selector) {
        long sum = 0;
        for (T t : this) {
            if (t != null) {
                sum += selector.applyAsLong(t);
            }
        }
        return sum;
    }

    default double doubleSumOf(ToDoubleFunction<? super T> selector) {
        double sum = 0;
        for (T t : this) {
            if (t != null) {
                sum += selector.applyAsDouble(t);
            }
        }
        return sum;
    }

    default BigDecimalX bigDecimalSumOf(Function<? super T, ? extends BigDecimal> selector) {
        BigDecimal sum = BigDecimal.ZERO;
        for (T t : this) {
            if (t != null) {
                final BigDecimal augend = selector.apply(t);
                if (augend != null) {
                    sum = sum.add(augend);
                }
            }
        }
        return BigDecimalX.of(sum);
    }

    default double averageOf(Function<T, Number> selector) {
        double sum = 0;
        long counter = 0;
        for (T t : this) {
            if (t != null) {
                final double value = selector.apply(t).doubleValue();
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default BigDecimalX bigDecimalAverageOf(Function<? super T, ? extends BigDecimal> selector) {
        return bigDecimalAverageOf(selector, 2, RoundingMode.HALF_UP);
    }

    default BigDecimalX bigDecimalAverageOf(Function<? super T, ? extends BigDecimal> selector, int scale,
                                            RoundingMode roundingMode) {
        BigDecimal sum = BigDecimal.ZERO;
        long counter = 0;
        for (T t : this) {
            if (t != null) {
                final BigDecimal value = selector.apply(t);
                if (value != null) {
                    sum = sum.add(value);
                }
                counter++;
            }
        }
        return BigDecimalX.of(sum.divide(BigDecimal.valueOf(counter), scale, roundingMode));
    }

    default <R extends Comparable<? super R>> Optional<T> minBy(Function<? super T, ? extends R> selector) {
        return IterableXHelper.compareBy(iterator(), selector, i -> i > 0);
    }

    default <R extends Comparable<? super R>> Optional<T> maxBy(Function<? super T, ? extends R> selector) {
        return IterableXHelper.compareBy(iterator(), selector, i -> i < 0);
    }

    default <R extends Comparable<? super R>> R minOf(Function<? super T, ? extends R> selector) {
        return IterableXHelper.comparisonOf(iterator(), selector, i -> i > 0);
    }

    default <R extends Comparable<? super R>> R maxOf(Function<? super T, ? extends R> selector) {
        return IterableXHelper.comparisonOf(iterator(), selector, i -> i < 0);
    }

    default IntStatistics intStatsOf(ToIntFunction<? super T> mapper) {
        IntStatistics statistics = new IntStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsInt(v)));
        return statistics;
    }

    default LongStatistics longStatsOf(ToLongFunction<? super T> mapper) {
        LongStatistics statistics = new LongStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsLong(v)));
        return statistics;
    }

    default DoubleStatistics doubleStatsOf(ToDoubleFunction<? super T> mapper) {
        DoubleStatistics statistics = new DoubleStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsDouble(v)));
        return statistics;
    }

    default BigDecimalStatistics bigDecimalStatsOf(Function<T, ? extends BigDecimal> mapper) {
        BigDecimalStatistics statistics = new BigDecimalStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> {
            final BigDecimal bigDecimal = mapper.apply(v);
            if (bigDecimal != null) {
                statistics.accept(bigDecimal);
            }
        });
        return statistics;
    }
}
