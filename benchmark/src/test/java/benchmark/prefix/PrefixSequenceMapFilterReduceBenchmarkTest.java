package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrefixSequenceMapFilterReduceBenchmarkTest {

    private final PrefixSequenceMapFilterReduceBenchmark reduceBenchmark = new PrefixSequenceMapFilterReduceBenchmark();

    @Test
    void testMapFilterReduceSameResult() {
        final int result1 = reduceBenchmark.imperativeMapFilterReduce();
        final int result2 = reduceBenchmark.sequenceOfListMapFilterReduce();
        final int result3 = reduceBenchmark.parallelStreamMapFilterReduce();
        final int result4 = reduceBenchmark.streamMapFilterReduce();

        assertAll(
                () -> assertEquals(result1, result2),
                () -> assertEquals(result1, result3),
                () -> assertEquals(result1, result4)
        );
    }

}
