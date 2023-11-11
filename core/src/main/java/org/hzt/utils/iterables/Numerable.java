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

    default long count(final Predicate<T> predicate) {
        return IterableXHelper.count(this, predicate);
    }

    default long count() {
        return count(It::noFilter);
    }

    default long intSumOf(final ToIntFunction<? super T> selector) {
        long sum = 0;
        for (final var t : this) {
            if (t != null) {
                sum += selector.applyAsInt(t);
            }
        }
        return sum;
    }

    default long longSumOf(final ToLongFunction<? super T> selector) {
        long sum = 0;
        for (final var t : this) {
            if (t != null) {
                sum += selector.applyAsLong(t);
            }
        }
        return sum;
    }

    default double doubleSumOf(final ToDoubleFunction<? super T> selector) {
        double sum = 0;
        for (final var t : this) {
            if (t != null) {
                sum += selector.applyAsDouble(t);
            }
        }
        return sum;
    }

    default BigDecimalX bigDecimalSumOf(final Function<? super T, ? extends BigDecimal> selector) {
        var sum = BigDecimal.ZERO;
        for (final var t : this) {
            if (t != null) {
                final var augend = selector.apply(t);
                if (augend != null) {
                    sum = sum.add(augend);
                }
            }
        }
        return BigDecimalX.of(sum);
    }

    default double averageOf(final Function<T, Number> selector) {
        double sum = 0;
        long counter = 0;
        for (final var t : this) {
            if (t != null) {
                final var value = selector.apply(t).doubleValue();
                sum += value;
                counter++;
            }
        }
        return (counter != 0) ? (sum / counter) : 0;
    }

    default BigDecimalX bigDecimalAverageOf(final Function<? super T, ? extends BigDecimal> selector) {
        return bigDecimalAverageOf(selector, 2, RoundingMode.HALF_UP);
    }

    default BigDecimalX bigDecimalAverageOf(final Function<? super T, ? extends BigDecimal> selector, final int scale,
                                            final RoundingMode roundingMode) {
        var sum = BigDecimal.ZERO;
        long counter = 0;
        for (final var t : this) {
            if (t != null) {
                final var value = selector.apply(t);
                if (value != null) {
                    sum = sum.add(value);
                }
                counter++;
            }
        }
        return BigDecimalX.of(sum.divide(BigDecimal.valueOf(counter), scale, roundingMode));
    }

    default <R extends Comparable<? super R>> Optional<T> minBy(final Function<? super T, ? extends R> selector) {
        return IterableXHelper.compareBy(iterator(), selector, i -> i > 0);
    }

    default <R extends Comparable<? super R>> Optional<T> maxBy(final Function<? super T, ? extends R> selector) {
        return IterableXHelper.compareBy(iterator(), selector, i -> i < 0);
    }

    default <R extends Comparable<? super R>> R minOf(final Function<? super T, ? extends R> selector) {
        return IterableXHelper.comparisonOf(iterator(), selector, i -> i > 0);
    }

    default <R extends Comparable<? super R>> R maxOf(final Function<? super T, ? extends R> selector) {
        return IterableXHelper.comparisonOf(iterator(), selector, i -> i < 0);
    }

    default IntStatistics intStatsOf(final ToIntFunction<? super T> mapper) {
        final var statistics = new IntStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsInt(v)));
        return statistics;
    }

    default LongStatistics longStatsOf(final ToLongFunction<? super T> mapper) {
        final var statistics = new LongStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsLong(v)));
        return statistics;
    }

    default DoubleStatistics doubleStatsOf(final ToDoubleFunction<? super T> mapper) {
        final var statistics = new DoubleStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsDouble(v)));
        return statistics;
    }

    default BigDecimalStatistics bigDecimalStatsOf(final Function<T, ? extends BigDecimal> mapper) {
        final var statistics = new BigDecimalStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> {
            final var bigDecimal = mapper.apply(v);
            if (bigDecimal != null) {
                statistics.accept(bigDecimal);
            }
        });
        return statistics;
    }
}
