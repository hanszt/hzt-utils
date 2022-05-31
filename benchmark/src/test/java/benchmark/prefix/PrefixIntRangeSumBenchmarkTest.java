package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrefixIntRangeSumBenchmarkTest {

    private final PrefixIntRangeSumBenchmark intRangeSumBenchmark = new PrefixIntRangeSumBenchmark();

    @Test
    void testSequenceStreamAndImperativeSumCalculationYieldsSameResult() {
        final long mapFilterSum = intRangeSumBenchmark.parallelStreamMapFilterSum();
        final long mapFilterSum1 = intRangeSumBenchmark.loopMapFilterSum();
        final long mapFilterSum2 = intRangeSumBenchmark.streamMapFilterSum();
        final long mapFilterSum3 = intRangeSumBenchmark.intSequenceMapFilterSum();
        final long mapFilterSum4 = intRangeSumBenchmark.intListXMapFilterSum();

        System.out.println("mapFilterSum = " + mapFilterSum);

        assertAll(
                () -> assertEquals(mapFilterSum, mapFilterSum1),
                () -> assertEquals(mapFilterSum1, mapFilterSum2),
                () -> assertEquals(mapFilterSum2, mapFilterSum3),
                () -> assertEquals(mapFilterSum3, mapFilterSum4)
        );
    }

}
