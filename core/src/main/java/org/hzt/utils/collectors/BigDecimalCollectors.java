package org.hzt.utils.collectors;

import org.hzt.utils.statistics.BigDecimalStatistics;
import org.hzt.utils.statistics.BigDecimalSummaryStatistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

public final class BigDecimalCollectors {

    private static final Set<Collector.Characteristics> CH_ID
            = Set.copyOf(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private BigDecimalCollectors() {
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal(
            final Function<? super T, BigDecimal> toBigDecimalMapper, final int scale, final RoundingMode roundingMode) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper,
                summaryStatistics -> summaryStatistics.getAverage(scale, roundingMode), CH_NOID);
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal(
            final Function<? super T, BigDecimal> toBigDecimalMapper) {
        return averagingBigDecimal(toBigDecimalMapper, 2, RoundingMode.HALF_UP);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal() {
        return averagingBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> toMaxBigDecimal(
            final Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getMax, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> toMaxBigDecimal() {
        return toMaxBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> toMinBigDecimal(
            final Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getMin, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> toMinBigDecimal() {
        return toMinBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimalSummaryStatistics> summarizingBigDecimal(
            final Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper, Function.identity(), CH_ID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimalSummaryStatistics> summarizingBigDecimal() {
        return summarizingBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> summingBigDecimal(
            final Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getSum, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> summingBigDecimal() {
        return summingBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalStatistics, BigDecimalStatistics> toBigDecimalStatisticsBy(
            final Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalStatistics::getStatistics, CH_ID);
    }

    public static Collector<BigDecimal, BigDecimalStatistics, BigDecimalStatistics> toBigDecimalStatistics() {
        return getBigDecimalStatisticsCollectorImpl(Function.identity(), BigDecimalStatistics::getStatistics, CH_ID);
    }

    public static <T> Collector<T, BigDecimalStatistics, BigDecimal> standardDeviatingBigDecimal(
            final Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalStatistics::getStandardDeviation, CH_NOID);
    }

    public static <T> Collector<T, BigDecimalStatistics, BigDecimal> standardDeviatingBigDecimal(
            final Function<? super T, BigDecimal> toBigDecimalMapper, final int scale, final RoundingMode roundingMode) {
        return getBigDecimalStatisticsCollectorImpl(toBigDecimalMapper,
                bigDecimalStatistics -> bigDecimalStatistics.getStandardDeviation(scale, roundingMode), CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalStatistics, BigDecimal> toStandardDeviation(final int scale, final RoundingMode roundingMode) {
        return getBigDecimalStatisticsCollectorImpl(Function.identity(),
                bigDecimalStatistics -> bigDecimalStatistics.getStandardDeviation(scale, roundingMode), CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalStatistics, BigDecimal> toStandardDeviation() {
        return getBigDecimalStatisticsCollectorImpl(Function.identity(),
                bigDecimalStatistics -> bigDecimalStatistics.getStandardDeviation(2, RoundingMode.HALF_UP), CH_NOID);
    }

    private static <T, R> Collector<T, BigDecimalSummaryStatistics, R> getBigDecimalSummaryStatisticsCollectorImpl(
            final Function<? super T, BigDecimal> toBigDecimalMapper, final Function<BigDecimalSummaryStatistics, R> finisher,
            final Set<Collector.Characteristics> characteristics) {
        Objects.requireNonNull(toBigDecimalMapper);
        return Collector.of(BigDecimalSummaryStatistics::new,
                (bigDecimalSummaryStatistics, t) -> bigDecimalSummaryStatistics.accept(toBigDecimalMapper.apply(t)),
                BigDecimalSummaryStatistics::combine,
                finisher,
                characteristics.toArray(Collector.Characteristics[]::new));
    }

    private static <T, R> Collector<T, BigDecimalStatistics, R> getBigDecimalStatisticsCollectorImpl(
            final Function<? super T, BigDecimal> toBigDecimalMapper, final Function<BigDecimalStatistics, R> finisher,
            final Set<Collector.Characteristics> characteristics) {
        Objects.requireNonNull(toBigDecimalMapper);
        return Collector.of(BigDecimalStatistics::new,
                (statistics, t) -> statistics.accept(toBigDecimalMapper.apply(t)),
                BigDecimalStatistics::combine,
                finisher,
                characteristics.toArray(Collector.Characteristics[]::new));
    }
}
