package hzt.iterables;

import hzt.numbers.BigDecimalX;
import hzt.statistics.BigDecimalStatistics;
import hzt.statistics.DoubleStatistics;
import hzt.statistics.IntStatistics;
import hzt.statistics.LongStatistics;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public interface Numerable<T> extends Iterable<T> {

    default long count(@NotNull Predicate<T> predicate) {
        return IterableXHelper.count(this, predicate);
    }

    default long count() {
        return count(It::noFilter);
    }

    default long sumOfInts(@NotNull ToIntFunction<? super T> selector) {
        long sum = 0;
        for (T t : this) {
            if (t != null) {
                sum += selector.applyAsInt(t);
            }
        }
        return sum;
    }

    default long sumOfLongs(@NotNull ToLongFunction<? super T> selector) {
        long sum = 0;
        for (T t : this) {
            if (t != null) {
                sum += selector.applyAsLong(t);
            }
        }
        return sum;
    }

    default double sumOfDoubles(@NotNull ToDoubleFunction<? super T> selector) {
        double sum = 0;
        for (T t : this) {
            if (t != null) {
                sum += selector.applyAsDouble(t);
            }
        }
        return sum;
    }

    default BigDecimalX bigDecimalSum(@NotNull Function<? super T, ? extends BigDecimal> selector) {
        var sum = BigDecimal.ZERO;
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

    default double averageOf(@NotNull Function<T, Number> selector) {
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

    default BigDecimalX toBigDecimalXAverage(@NotNull Function<? super T, ? extends BigDecimal> selector) {
        return toBigDecimalXAverage(selector, 2, RoundingMode.HALF_UP);
    }

    default BigDecimalX toBigDecimalXAverage(@NotNull Function<? super T, ? extends BigDecimal> selector, int scale,
                                             @NotNull RoundingMode roundingMode) {
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

    @NotNull
    default <R extends Comparable<R>> Optional<T> minBy(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper.compareBy(iterator(),
                selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    default <R extends Comparable<R>> Optional<T> maxBy(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper.compareBy(iterator(),
                selector, (first, second) -> first != null && second != null && first.compareTo(second) < 0);
    }

    @NotNull
    default <R extends Comparable<R>> R minOf(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper
                .comparisonOf(iterator(), selector, (first, second) -> first != null && second != null && first.compareTo(second) > 0);
    }

    @NotNull
    default <R extends Comparable<R>> R maxOf(@NotNull Function<? super T, ? extends R> selector) {
        return IterableXHelper
                .comparisonOf(iterator(), selector, (first, second) -> first != null && second != null && first.compareTo(second) < 0);
    }

    default IntStatistics statsOfInts(@NotNull ToIntFunction<? super T> mapper) {
        var statistics = new IntStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsInt(v)));
        return statistics;
    }

    default LongStatistics statsOfLongs(@NotNull ToLongFunction<? super T> mapper) {
        var statistics = new LongStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsLong(v)));
        return statistics;
    }

    default DoubleStatistics statsOfDoubles(@NotNull ToDoubleFunction<? super T> mapper) {
        var statistics = new DoubleStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> statistics.accept(mapper.applyAsDouble(v)));
        return statistics;
    }

    default BigDecimalStatistics statsOfBigDecimals(@NotNull Function<T, ? extends BigDecimal> mapper) {
        var statistics = new BigDecimalStatistics();
        IterableXHelper.exposeNonNullVal(this, v -> {
            final BigDecimal bigDecimal = mapper.apply(v);
            if (bigDecimal != null) {
                statistics.accept(bigDecimal);
            }
        });
        return statistics;
    }
}
