package org.hzt.utils.statistics;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.primitives.DoubleListX;
import org.hzt.utils.collections.primitives.IntListX;
import org.hzt.utils.collections.primitives.LongListX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsTest {

    @SuppressWarnings("squid:S5977")
    private static final Random RANDOM = new Random(0);

    @Test
    void testStatisticsStandardDeviation() {
        final ListX<Integer> list = Sequence.generate(RANDOM::nextGaussian)
                .take(1_000)
                .map(d -> (int) (d * 100))
                .toListX();

        final LongListX intRange = list.mapToLong(It::asLong);
        final IntListX longRange = intRange.mapToInt(It::longAsInt);
        final DoubleListX doubleRange = intRange.mapToDouble(It::asDouble);

        final double standardDeviationIntRange = intRange.stats().getStandardDeviation();

        It.println("longRange.count() = " + longRange.count());
        It.println(longRange.sum());

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
        It.println(intRange.joinToString());
        It.println(longRange.joinToString());
        It.println(doubleRange.joinToString());
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        It.println(standardDeviationIntRange);

        assertAll(
                () -> assertEquals(standardDeviationIntRange, longRange.stats().getStandardDeviation()),
                () -> assertEquals(standardDeviationIntRange, doubleRange.stats().getStandardDeviation())
        );

    }
}
