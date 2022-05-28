package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrefixSequenceBenchmarkTest {

    private final PrefixSequenceBenchmark prefixSequenceBenchmark = new PrefixSequenceBenchmark();

    @Test
    void testSequenceLoopAndStreamMapFilterToListYieldSameResult() {
        final var sequenceResult = prefixSequenceBenchmark.sequenceOfListMapFilterToList();
        final var streamResult = prefixSequenceBenchmark.streamMapFilterToList();
        final var loopResult = prefixSequenceBenchmark.imperativeMapFilterToList();

        System.out.println("streamResult = " + streamResult);

        assertAll(
                () -> assertEquals(sequenceResult, streamResult),
                () -> assertEquals(loopResult, sequenceResult)
        );
    }

}
