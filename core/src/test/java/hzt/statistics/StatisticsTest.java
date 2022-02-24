package hzt.statistics;

import hzt.collections.ListX;
import hzt.sequences.Sequence;
import hzt.sequences.primitives.DoubleSequence;
import hzt.sequences.primitives.IntSequence;
import hzt.sequences.primitives.LongSequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsTest {

    private static final Random RANDOM = new Random();

    @Test
    void testStatisticsStandardDeviation() {
        final ListX<Integer> list = Sequence.generate(RANDOM::nextGaussian)
                .take(1_000)
                .map(d -> (int) (d * 100))
                .toListX();

        final LongSequence intRange = list.mapToLong(It::asLong);
        final IntSequence longRange = intRange.mapToInt(It::longAsInt);
        final DoubleSequence doubleRange = intRange.mapToDouble(It::asDouble);

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
