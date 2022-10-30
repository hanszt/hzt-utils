package benchmark.prefix;

import org.hzt.utils.statistics.DoubleStatistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrefixDoubleSequenceToStatsBenchmarkTest {

    private final PrefixDoubleSequenceToStatsBenchmark toStatsBenchmark = new PrefixDoubleSequenceToStatsBenchmark();

    @Test
    void testLoopStreamAndSequenceToSummaryStatsYieldsSameResult() {
        final DoubleStatistics doubleStatistics = toStatsBenchmark.sequenceMapFilterToStats();
        final DoubleStatistics doubleStatistics1 = toStatsBenchmark.loopMapFilterToStats();
        final DoubleStatistics doubleStatistics2 = toStatsBenchmark.streamMapFilterToStats();

        System.out.println("doubleStatistics = " + doubleStatistics);

        assertAll(
                () -> assertEquals(doubleStatistics, doubleStatistics1),
                () -> assertEquals(doubleStatistics, doubleStatistics2)
        );
    }

}
