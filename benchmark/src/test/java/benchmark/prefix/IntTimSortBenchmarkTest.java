package benchmark.prefix;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.IntMutableListX;
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
        final IntMutableListX intMutableListX = intTimSortBenchmark.intListSort();
        final MutableListX<Integer> mutableListX = intTimSortBenchmark.listSort();
        final int[] array = intTimSortBenchmark.arraySort();

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
        final IntMutableListX intMutableListX = intTimSortBenchmark.intListSortReversed();
        final MutableListX<Integer> mutableListX = intTimSortBenchmark.listSortReversed();

        intMutableListX.take(10).forEachInt(System.out::println);

        assertAll(
                () -> assertIterableEquals(intMutableListX, mutableListX),
                () -> assertEquals(199_999, intMutableListX.size())
        );
    }
}
