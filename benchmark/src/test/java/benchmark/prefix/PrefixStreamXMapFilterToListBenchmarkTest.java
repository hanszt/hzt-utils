package benchmark.prefix;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrefixStreamXMapFilterToListBenchmarkTest {

    private final PrefixStreamXMapFilterToListBenchmark streamXMapFilterToListBenchmark = new PrefixStreamXMapFilterToListBenchmark();

    @Test
    void testStreamAndStreamXYieldSameResult() {
        final List<Integer> integers = streamXMapFilterToListBenchmark.streamMapFilterToList();
        final List<Integer> integers1 = streamXMapFilterToListBenchmark.streamXMapFilterToList();
        final List<Integer> integers2 = streamXMapFilterToListBenchmark.imperativeMapFilterToList();

        assertAll(
                () -> assertEquals(integers, integers1),
                () -> assertEquals(integers, integers2)
        );
    }

}
