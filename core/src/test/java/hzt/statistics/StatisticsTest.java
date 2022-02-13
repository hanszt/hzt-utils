package hzt.statistics;

import hzt.sequences.Sequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsTest {

    private static final Random RANDOM = new Random();

    @Test
    void testStatisticsStandardDeviation() {
        final var list = Sequence.generate(RANDOM::nextGaussian)
                .take(1_000)
                .map(d -> (int) (d * 100))
                .toListX();

        final var intRange = list.asIntRange(It::asInt);
        final var longRange = intRange.asLongRange(It::asLong);
        final var doubleRange = intRange.asDoubleRange(It::asDouble);

        final var stats = intRange.stats();

        It.println("stats = " + stats);
        final var standardDeviationIntRange = stats.getStandardDeviation();

        It.println("longRange.count() = " + longRange.count());
        It.println(longRange.sum());
        It.println(intRange.joinToString());
        It.println(longRange.joinToString());
        It.println(doubleRange.joinToString());
        It.println(standardDeviationIntRange);

        assertAll(
                () -> assertEquals(standardDeviationIntRange, longRange.stats().also(It::println).getStandardDeviation()),
                () -> assertEquals(standardDeviationIntRange, doubleRange.stats().getStandardDeviation())
        );

    }
}
