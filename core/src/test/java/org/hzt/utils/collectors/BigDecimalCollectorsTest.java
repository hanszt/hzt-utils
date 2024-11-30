package org.hzt.utils.collectors;

import org.hzt.test.ReplaceCamelCaseBySentence;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.utils.It;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static org.hzt.utils.collectors.BigDecimalCollectors.averagingBigDecimal;
import static org.hzt.utils.collectors.BigDecimalCollectors.standardDeviatingBigDecimal;
import static org.hzt.utils.collectors.BigDecimalCollectors.summarizingBigDecimal;
import static org.hzt.utils.collectors.BigDecimalCollectors.summingBigDecimal;
import static org.hzt.utils.collectors.BigDecimalCollectors.toBigDecimalStatisticsBy;
import static org.hzt.utils.collectors.BigDecimalCollectors.toMaxBigDecimal;
import static org.hzt.utils.collectors.BigDecimalCollectors.toMinBigDecimal;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class BigDecimalCollectorsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BigDecimalCollectorsTest.class);

    @Test
    void testSummarizingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        LOGGER.atDebug().setMessage(() -> "Sample bankaccountList:").log();
        sampleBankAccountList.forEach(it -> LOGGER.trace("{}", it));

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        LOGGER.atDebug().setMessage(() -> "bigDecimalSummaryStatistics = " + bigDecimalSummaryStatistics).log();

        assertAll(
                () -> assertEquals(BigDecimal.valueOf(46_502.27), bigDecimalSummaryStatistics.getAverage()),
                () -> assertEquals(BigDecimal.valueOf(232_511.34), bigDecimalSummaryStatistics.getSum()),
                () -> assertEquals(BigDecimal.valueOf(-4_323), bigDecimalSummaryStatistics.getMin()),
                () -> assertEquals(BigDecimal.valueOf(234_235.34), bigDecimalSummaryStatistics.getMax()),
                () -> assertEquals(5, bigDecimalSummaryStatistics.getCount())
        );
    }

    @Test
    void testAveragingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        LOGGER.atDebug().setMessage(() -> "Sample bankaccountList:").log();
        sampleBankAccountList.forEach(it -> LOGGER.trace("{}", it));

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var average = sampleBankAccountList.stream()
                .collect(averagingBigDecimal(BankAccount::getBalance));

        LOGGER.atDebug().setMessage(() -> "average = " + average).log();

        final BigDecimal expected = bigDecimalSummaryStatistics.getAverage();
        assertEquals(average, expected);
    }

    @Test
    void testStandardDeviatingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        LOGGER.atDebug().setMessage(() -> "Sample bankaccountList:").log();
        sampleBankAccountList.forEach(it -> LOGGER.trace("{}", it));

        final var doubleStatistics = sampleBankAccountList.stream()
                .map(BankAccount::getBalance)
                .collect(CollectorsX.toDoubleStatisticsBy(BigDecimal::doubleValue));

        final var expectedStandardDeviationFromDouble = BigDecimal.valueOf(doubleStatistics.getStandardDeviation())
                .setScale(2, RoundingMode.HALF_UP);

        final var bigDecimalStatistics = sampleBankAccountList.stream()
                .collect(toBigDecimalStatisticsBy(BankAccount::getBalance));
        final BigDecimal expected = bigDecimalStatistics.getStandardDeviation();

        final var standardDeviationBalances = sampleBankAccountList.stream()
                .collect(standardDeviatingBigDecimal(BankAccount::getBalance));

        LOGGER.atDebug().setMessage(() -> "bigDecimalStatistics = " + bigDecimalStatistics).log();
        LOGGER.atDebug().setMessage(() -> "doubleStatistics = " + doubleStatistics).log();

        assertAll(
                () -> assertEquals(expected, standardDeviationBalances),
                () -> assertEquals(expectedStandardDeviationFromDouble, standardDeviationBalances)
        );
    }

    @Test
    void testStatisticsFromRandomGaussianDataset() {
        final var targetMean = BigDecimal.valueOf(3);
        final var targetStdDev = BigDecimal.valueOf(4);

        final var statistics = TestSampleGenerator
                .gaussianDoubles(100_000, targetMean.doubleValue(), targetStdDev.doubleValue(), new Random(0))
                .mapToObj(BigDecimal::valueOf)
                .collect(BigDecimalCollectors.toBigDecimalStatistics());

        final BigDecimal standardDeviation = statistics.getStandardDeviation()
                .setScale(1, RoundingMode.HALF_UP);
        final BigDecimal average = statistics.getAverage()
                .setScale(1, RoundingMode.HALF_UP);

        assertAll(
                () -> assertEquals(targetStdDev.setScale(1, RoundingMode.HALF_UP), standardDeviation),
                () -> assertEquals(targetMean.setScale(1, RoundingMode.HALF_UP), average)
        );
    }

    @Test
    void testSummingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        LOGGER.atDebug().setMessage(() -> "Sample bankaccountList:").log();
        sampleBankAccountList.forEach(it -> LOGGER.trace("{}", it));

        final var sumAsDouble = sampleBankAccountList.stream()
                .map(BankAccount::getBalance)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var sum = sampleBankAccountList.stream()
                .collect(summingBigDecimal(BankAccount::getBalance));

        LOGGER.atDebug().setMessage(() -> "sum = " + sum).log();

        final BigDecimal expected = bigDecimalSummaryStatistics.getSum();

        assertAll(
                () -> assertEquals(sum, expected),
                () -> assertEquals(sumAsDouble, sum.doubleValue())
        );
    }

    @Test
    void testToMaxBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        LOGGER.atDebug().setMessage(() -> "Sample bankaccountList:").log();
        sampleBankAccountList.forEach(it -> LOGGER.trace("{}", it));

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var max = sampleBankAccountList.stream()
                .collect(toMaxBigDecimal(BankAccount::getBalance));

        LOGGER.atDebug().setMessage(() -> "max = " + max).log();

        final BigDecimal expected = bigDecimalSummaryStatistics.getMax();
        assertEquals(max, expected);
    }

    @Test
    void testToMinBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        LOGGER.atDebug().setMessage(() -> "Sample bankaccountList:").log();
        sampleBankAccountList.forEach(it -> LOGGER.trace("{}", it));

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var min = sampleBankAccountList.stream()
                .collect(toMinBigDecimal(BankAccount::getBalance));

        LOGGER.atDebug().setMessage(() -> "min = " + min).log();

        final BigDecimal expected = bigDecimalSummaryStatistics.getMin();
        assertEquals(min, expected);
    }
}
