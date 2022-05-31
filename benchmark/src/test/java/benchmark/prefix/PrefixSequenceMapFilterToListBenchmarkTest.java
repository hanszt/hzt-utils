package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrefixSequenceMapFilterToListBenchmarkTest {

    private final PrefixSequenceMapFilterToListBenchmark prefixSequenceMapFilterToListBenchmark = new PrefixSequenceMapFilterToListBenchmark();

    @Test
    void testSequenceLoopAndStreamMapFilterToListYieldSameResult() {
        final var sequenceResult = prefixSequenceMapFilterToListBenchmark.sequenceOfListMapFilterToList();
        final var streamResult = prefixSequenceMapFilterToListBenchmark.streamMapFilterToList();
        final var loopResult = prefixSequenceMapFilterToListBenchmark.imperativeMapFilterToList();

        System.out.println("streamResult = " + streamResult);

        assertAll(
                () -> assertEquals(sequenceResult, streamResult),
                () -> assertEquals(loopResult, sequenceResult)
        );
    }

}
