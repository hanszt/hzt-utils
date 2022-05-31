package benchmark.prefix;

import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.DoubleMutableListX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleTimSortBenchmarkTest {

    private final DoubleTimSortBenchmark doubleTimSortBenchmark = new DoubleTimSortBenchmark();

    @Test
    void testDoubleListSortAndListSortYieldSameContent() {
        final DoubleMutableListX doubleMutableListX = doubleTimSortBenchmark.doubleListSort();
        final MutableListX<Double> mutableListX = doubleTimSortBenchmark.listSort();
        final double[] array = doubleTimSortBenchmark.arraySort();

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
        final DoubleMutableListX doubleMutableListX = doubleTimSortBenchmark.doubleListSortReversed();
        final MutableListX<Double> mutableListX = doubleTimSortBenchmark.listSortReversed();

        doubleMutableListX.take(10).forEachDouble(System.out::println);

        assertAll(
                () -> assertIterableEquals(doubleMutableListX, mutableListX),
                () -> assertEquals(199_999, doubleMutableListX.size())
        );
    }
}
