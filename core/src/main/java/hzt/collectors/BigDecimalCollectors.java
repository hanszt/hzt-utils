package hzt.collectors;

import hzt.statistics.BigDecimalStatistics;
import hzt.statistics.BigDecimalSummaryStatistics;

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
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private BigDecimalCollectors() {
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper, int scale, RoundingMode roundingMode) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper,
                summaryStatistics -> summaryStatistics.getAverage(scale, roundingMode), CH_NOID);
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return averagingBigDecimal(toBigDecimalMapper, 2, RoundingMode.HALF_UP);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal() {
        return averagingBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> toMaxBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getMax, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> toMaxBigDecimal() {
        return toMaxBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> toMinBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getMin, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> toMinBigDecimal() {
        return toMinBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimalSummaryStatistics> summarizingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper, Function.identity(), CH_ID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimalSummaryStatistics> summarizingBigDecimal() {
        return summarizingBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> summingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalSummaryStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getSum, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> summingBigDecimal() {
        return summingBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalStatistics, BigDecimalStatistics> toBigDecimalStatisticsBy(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalStatistics::getStatistics, CH_ID);
    }

    public static Collector<BigDecimal, BigDecimalStatistics, BigDecimalStatistics> toBigDecimalStatistics() {
        return getBigDecimalStatisticsCollectorImpl(Function.identity(), BigDecimalStatistics::getStatistics, CH_ID);
    }

    public static <T> Collector<T, BigDecimalStatistics, BigDecimal> standardDeviatingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalStatisticsCollectorImpl(toBigDecimalMapper, BigDecimalStatistics::getStandardDeviation, CH_NOID);
    }

    public static <T> Collector<T, BigDecimalStatistics, BigDecimal> standardDeviatingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper, int scale, RoundingMode roundingMode) {
        return getBigDecimalStatisticsCollectorImpl(toBigDecimalMapper,
                bigDecimalStatistics -> bigDecimalStatistics.getStandardDeviation(scale, roundingMode), CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalStatistics, BigDecimal> toStandardDeviation(int scale, RoundingMode roundingMode) {
        return getBigDecimalStatisticsCollectorImpl(Function.identity(),
                bigDecimalStatistics -> bigDecimalStatistics.getStandardDeviation(scale, roundingMode), CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalStatistics, BigDecimal> toStandardDeviation() {
        return getBigDecimalStatisticsCollectorImpl(Function.identity(),
                bigDecimalStatistics -> bigDecimalStatistics.getStandardDeviation(2, RoundingMode.HALF_UP), CH_NOID);
    }

    private static <T, R> CollectorImpl<T, BigDecimalSummaryStatistics, R> getBigDecimalSummaryStatisticsCollectorImpl(
            Function<? super T, BigDecimal> toBigDecimalMapper, Function<BigDecimalSummaryStatistics, R> finisher,
            Set<Collector.Characteristics> characteristics) {
        Objects.requireNonNull(toBigDecimalMapper);
        return new CollectorImpl<>(BigDecimalSummaryStatistics::new,
                (bigDecimalSummaryStatistics, t) -> bigDecimalSummaryStatistics.accept(toBigDecimalMapper.apply(t)),
                BigDecimalSummaryStatistics::combine,
                finisher,
                characteristics);
    }

    private static <T, R> CollectorImpl<T, BigDecimalStatistics, R> getBigDecimalStatisticsCollectorImpl(
            Function<? super T, BigDecimal> toBigDecimalMapper, Function<BigDecimalStatistics, R> finisher,
            Set<Collector.Characteristics> characteristics) {
        Objects.requireNonNull(toBigDecimalMapper);
        return new CollectorImpl<>(BigDecimalStatistics::new,
                (statistics, t) -> statistics.accept(toBigDecimalMapper.apply(t)),
                BigDecimalStatistics::combine,
                finisher,
                characteristics);
    }
}
