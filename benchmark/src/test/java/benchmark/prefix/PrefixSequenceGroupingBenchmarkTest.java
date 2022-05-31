package benchmark.prefix;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.primitives.IntMutableListX;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class PrefixSequenceGroupingBenchmarkTest {

    private final PrefixSequenceGroupingBenchmark groupingBenchmark = new PrefixSequenceGroupingBenchmark();

    @Test
    void testIntRangeLoopAndIntStreamGroupingYieldsSameResult() {
        final MapX<Integer, IntMutableListX> sequenceGrouping = groupingBenchmark.intRangeFilterGroup();
        final Map<Integer, List<Integer>> streamGrouping = groupingBenchmark.intStreamRangeFilterGroup();
        final Map<Integer, List<Integer>> imperativeGrouping = groupingBenchmark.groupByImperative();

        System.out.println("intsFromIntStream = " + streamGrouping);

        assertAll(
                () -> assertIterableEquals(sequenceGrouping.get(0), streamGrouping.get(0)),
                () -> assertIterableEquals(sequenceGrouping.get(0), imperativeGrouping.get(0))
        );
    }

}
