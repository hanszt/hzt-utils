package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrefixIntRangeSumBenchmarkTest {

    private final PrefixIntRangeSumBenchmark intRangeSumBenchmark = new PrefixIntRangeSumBenchmark();

    @Test
    void testSequenceStreamAndImperativeSumCalculationYieldsSameResult() {
        final var mapFilterSum = intRangeSumBenchmark.parallelStreamMapFilterSum();
        final var mapFilterSum1 = intRangeSumBenchmark.loopMapFilterSum();
        final var mapFilterSum2 = intRangeSumBenchmark.streamMapFilterSum();
        final var mapFilterSum3 = intRangeSumBenchmark.intSequenceMapFilterSum();
        final var mapFilterSum4 = intRangeSumBenchmark.intListXMapFilterSum();

        System.out.println("mapFilterSum = " + mapFilterSum);

        assertAll(
                () -> assertEquals(mapFilterSum, mapFilterSum1),
                () -> assertEquals(mapFilterSum1, mapFilterSum2),
                () -> assertEquals(mapFilterSum2, mapFilterSum3),
                () -> assertEquals(mapFilterSum3, mapFilterSum4)
        );
    }

}
