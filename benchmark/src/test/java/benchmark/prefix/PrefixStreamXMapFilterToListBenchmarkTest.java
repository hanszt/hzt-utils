package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrefixStreamXMapFilterToListBenchmarkTest {

    private final PrefixStreamXMapFilterToListBenchmark streamXMapFilterToListBenchmark = new PrefixStreamXMapFilterToListBenchmark();

    @Test
    void testStreamAndStreamXYieldSameResult() {
        final var integers = streamXMapFilterToListBenchmark.streamMapFilterToList();
        final var integers1 = streamXMapFilterToListBenchmark.streamXMapFilterToList();
        final var integers2 = streamXMapFilterToListBenchmark.imperativeMapFilterToList();

        assertAll(
                () -> assertEquals(integers, integers1),
                () -> assertEquals(integers, integers2)
        );
    }

}
