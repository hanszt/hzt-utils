package hzt.statistics;

import hzt.collections.ListView;
import hzt.ranges.DoubleRange;
import hzt.ranges.LongRange;
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
        final ListView<Integer> list = Sequence.generate(RANDOM::nextGaussian)
                .take(1_000)
                .map(d -> (int) (d * 100))
                .toListView();

        final LongRange intRange = list.asLongRange(It::asLong);
        final LongRange longRange = intRange.asLongRange(It::asLong);
        final DoubleRange doubleRange = intRange.asDoubleRange(It::asDouble);

        final Double standardDeviationIntRange = intRange.stats().getStandardDeviation();

        It.println(intRange.joinToString());
        It.println(longRange.joinToString());
        It.println(doubleRange.joinToString());
        It.println(standardDeviationIntRange);

        assertAll(
                () -> assertEquals(standardDeviationIntRange, longRange.stats().getStandardDeviation()),
                () -> assertEquals(standardDeviationIntRange, doubleRange.stats().getStandardDeviation())
        );

    }
}
