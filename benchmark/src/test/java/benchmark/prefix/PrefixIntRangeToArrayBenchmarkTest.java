package benchmark.prefix;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PrefixIntRangeToArrayBenchmarkTest {

    private final PrefixIntRangeToArrayBenchmark intRangeToArrayBenchmark = new PrefixIntRangeToArrayBenchmark();

    @Test
    void testIntRangeAndIntStreamRangeMapFilterToArrayYieldSameResult() {
        final var intsFromIntSequence = intRangeToArrayBenchmark.intRangeMapFilterToArray();
        final var intsFromIntStream = intRangeToArrayBenchmark.streamMapFilterToArray();
        final var intsFromLoop = intRangeToArrayBenchmark.loopMapFilterToArray();

        System.out.println("Arrays.toString(intsFromIntSequence) = " + Arrays.toString(intsFromIntSequence));

        assertAll(
                () -> assertArrayEquals(intsFromIntSequence, intsFromIntStream),
                () -> assertArrayEquals(intsFromIntSequence, intsFromLoop)
        );
    }
}
