package org.hzt.utils.collectors;

import org.hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hzt.utils.collectors.BigDecimalCollectors.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class BigDecimalCollectorsTest {

    @Test
    void testSummarizingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        It.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(It::println);

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        It.println("bigDecimalSummaryStatistics = " + bigDecimalSummaryStatistics);

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
        It.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(It::println);

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var average = sampleBankAccountList.stream()
                .collect(averagingBigDecimal(BankAccount::getBalance));

        It.println("average = " + average);

        final BigDecimal expected = bigDecimalSummaryStatistics.getAverage();
        assertEquals(average, expected);
    }

    @Test
    void testStandardDeviatingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        It.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(It::println);

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

        It.println("bigDecimalStatistics = " + bigDecimalStatistics);
        It.println("doubleStatistics = " + doubleStatistics);

        assertAll(
                () -> assertEquals(expected, standardDeviationBalances),
                () -> assertEquals(expectedStandardDeviationFromDouble, standardDeviationBalances)
        );
    }

    @Test
    void testStatisticsFromRandomGaussianDataset() {
        var targetMean = BigDecimal.valueOf(3);
        var targetStdDev = BigDecimal.valueOf(4);

        final var statistics = TestSampleGenerator
                .gaussianDoubles(100_000, targetMean.doubleValue(), targetStdDev.doubleValue())
                .mapToObj(BigDecimal::valueOf)
                .collect(BigDecimalCollectors.toBigDecimalStatistics());

        It.println("statistics = " + statistics);

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
        It.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(It::println);

        final var sumAsDouble = sampleBankAccountList.stream()
                .map(BankAccount::getBalance)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var sum = sampleBankAccountList.stream()
                .collect(summingBigDecimal(BankAccount::getBalance));

        It.println("sum = " + sum);

        final BigDecimal expected = bigDecimalSummaryStatistics.getSum();

        assertAll(
                () -> assertEquals(sum, expected),
                () -> assertEquals(sumAsDouble, sum.doubleValue())
        );
    }

    @Test
    void testToMaxBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        It.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(It::println);

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var max = sampleBankAccountList.stream()
                .collect(toMaxBigDecimal(BankAccount::getBalance));

        It.println("max = " + max);

        final BigDecimal expected = bigDecimalSummaryStatistics.getMax();
        assertEquals(max, expected);
    }

    @Test
    void testToMinBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        It.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(It::println);

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var min = sampleBankAccountList.stream()
                .collect(toMinBigDecimal(BankAccount::getBalance));

        It.println("min = " + min);

        final BigDecimal expected = bigDecimalSummaryStatistics.getMin();
        assertEquals(min, expected);
    }
}
