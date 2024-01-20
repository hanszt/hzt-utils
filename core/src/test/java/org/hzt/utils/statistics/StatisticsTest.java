package org.hzt.utils.statistics;

import org.hzt.utils.It;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsTest {

    @SuppressWarnings("squid:S5977")
    private static final Random RANDOM = new Random();

    @Test
    void testStatisticsStandardDeviation() {
        final var list = Sequence.generate(RANDOM::nextGaussian)
                .take(1_000)
                .map(d -> (int) (d * 100))
                .toListX();

        final var intRange = list.mapToInt(It::asInt);
        final var longRange = intRange.mapToLong(It::asLong);
        final var doubleRange = intRange.mapToDouble(It::asDouble);

        final var stats = intRange.stats();

        It.println("stats = " + stats);
        final var standardDeviationIntRange = stats.getStandardDeviation();

        println("longRange.count() = " + longRange.count());
        println(longRange.sum());

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
        println(intRange.joinToString());
        println(longRange.joinToString());
        println(doubleRange.joinToString());
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        println(standardDeviationIntRange);

        assertAll(
                () -> assertEquals(standardDeviationIntRange, longRange.stats().also(It::println).getStandardDeviation()),
                () -> assertEquals(standardDeviationIntRange, doubleRange.stats().getStandardDeviation())
        );

    }
}
