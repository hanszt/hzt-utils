package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class PrefixSequenceGroupingBenchmarkTest {

    private final PrefixSequenceGroupingBenchmark groupingBenchmark = new PrefixSequenceGroupingBenchmark();

    @Test
    void testIntRangeLoopAndIntStreamGroupingYieldsSameResult() {
        final var sequenceGrouping = groupingBenchmark.intRangeFilterGroup();
        final var streamGrouping = groupingBenchmark.intStreamRangeFilterGroup();
        final var imperativeGrouping = groupingBenchmark.groupByImperative();

        System.out.println("intsFromIntStream = " + streamGrouping);

        assertAll(
                () -> assertIterableEquals(sequenceGrouping.get(0), streamGrouping.get(0)),
                () -> assertIterableEquals(sequenceGrouping.get(0), imperativeGrouping.get(0))
        );
    }

}
