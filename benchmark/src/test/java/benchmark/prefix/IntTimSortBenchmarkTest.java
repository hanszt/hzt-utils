package benchmark.prefix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class IntTimSortBenchmarkTest {

    private final IntTimSortBenchmark intTimSortBenchmark = new IntTimSortBenchmark();

    @Test
    void testIntListSortAndListSortYieldSameContent() {
        final var intMutableListX = intTimSortBenchmark.intListSort();
        final var mutableListX = intTimSortBenchmark.listSort();
        final var array = intTimSortBenchmark.arraySort();

        intMutableListX.take(10).forEachInt(System.out::println);

        assertAll(
                () -> assertNotEquals(intMutableListX, intTimSortBenchmark.primitiveList),
                () -> assertNotEquals(mutableListX, intTimSortBenchmark.inputList),
                () -> assertEquals(199_999, array.length),
                () -> assertEquals(199_999, intMutableListX.size()),
                () -> assertArrayEquals(intMutableListX.toArray(), array),
                () -> assertIterableEquals(intMutableListX, mutableListX)
        );
    }

    @Test
    void testIntListAndListReverseSortYieldSameContent() {
        final var intMutableListX = intTimSortBenchmark.intListSortReversed();
        final var mutableListX = intTimSortBenchmark.listSortReversed();

        intMutableListX.take(10).forEachInt(System.out::println);

        assertAll(
                () -> assertIterableEquals(intMutableListX, mutableListX),
                () -> assertEquals(199_999, intMutableListX.size())
        );
    }
}
