package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleTimSortBenchmarkTest {

    private final DoubleTimSortBenchmark doubleTimSortBenchmark = new DoubleTimSortBenchmark();

    @Test
    void testDoubleListSortAndListSortYieldSameContent() {
        final var doubleMutableListX = doubleTimSortBenchmark.doubleListSort();
        final var mutableListX = doubleTimSortBenchmark.listSort();
        final var array = doubleTimSortBenchmark.arraySort();

        doubleMutableListX.take(10).forEachDouble(System.out::println);

        assertAll(
                () -> assertNotEquals(doubleMutableListX, doubleTimSortBenchmark.primitiveList),
                () -> assertNotEquals(mutableListX, doubleTimSortBenchmark.inputList),
                () -> assertEquals(199_999, array.length),
                () -> assertEquals(199_999, doubleMutableListX.size()),
                () -> assertArrayEquals(doubleMutableListX.toArray(), array),
                () -> assertIterableEquals(doubleMutableListX, mutableListX)
        );
    }

    @Test
    void testDoubleListAndListReverseSortYieldSameContent() {
        final var doubleMutableListX = doubleTimSortBenchmark.doubleListSortReversed();
        final var mutableListX = doubleTimSortBenchmark.listSortReversed();

        doubleMutableListX.take(10).forEachDouble(System.out::println);

        assertAll(
                () -> assertIterableEquals(doubleMutableListX, mutableListX),
                () -> assertEquals(199_999, doubleMutableListX.size())
        );
    }
}
