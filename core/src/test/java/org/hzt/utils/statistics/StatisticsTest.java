package org.hzt.utils.statistics;

import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsTest.class);

    @Test
    void testStatisticsStandardDeviation() {
        final var list = Sequence.generate(new Random(0)::nextGaussian)
                .take(1_000)
                .map(d -> (int) (d * 100))
                .toListX();

        final var intRange = list.mapToInt(It::asInt);
        final var longRange = intRange.mapToLong(It::asLong);
        final var doubleRange = intRange.mapToDouble(It::asDouble);

        final var stats = intRange.stats();

        LOGGER.atDebug().setMessage(() -> "stats = " + stats).log();
        final var standardDeviationIntRange = stats.getStandardDeviation();

        LOGGER.atDebug().setMessage(() -> "longRange.count() = " + longRange.count()).log();
        LOGGER.atDebug().setMessage(() -> "standard deviation: " + longRange.sum()).log();

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
        LOGGER.atDebug().setMessage(() -> intRange.joinToString()).log();
        LOGGER.atDebug().setMessage(() -> longRange.joinToString()).log();
        LOGGER.atDebug().setMessage(() -> doubleRange.joinToString()).log();
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        LOGGER.debug("standard deviation: {}", standardDeviationIntRange);

        assertAll(
                () -> assertEquals(standardDeviationIntRange, longRange.stats().also(it -> LOGGER.trace("{}", it)).getStandardDeviation()),
                () -> assertEquals(standardDeviationIntRange, doubleRange.stats().getStandardDeviation())
        );

    }
}
