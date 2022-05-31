package benchmark.prefix;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class PrefixSequenceMapFilterToListBenchmarkTest {

    private final PrefixSequenceMapFilterToListBenchmark prefixSequenceMapFilterToListBenchmark = new PrefixSequenceMapFilterToListBenchmark();

    @Test
    void testSequenceLoopAndStreamMapFilterToListYieldSameResult() {
        final List<Integer> sequenceResult = prefixSequenceMapFilterToListBenchmark.sequenceOfListMapFilterToList();
        final List<Integer> streamResult = prefixSequenceMapFilterToListBenchmark.streamMapFilterToList();
        final List<Integer> loopResult = prefixSequenceMapFilterToListBenchmark.imperativeMapFilterToList();

        System.out.println("streamResult = " + streamResult);

        assertAll(
                () -> assertIterableEquals(sequenceResult, streamResult),
                () -> assertEquals(loopResult, sequenceResult)
        );
    }

}
