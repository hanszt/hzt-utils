package hzt.ranges;

import hzt.collections.primitives.IntListX;
import hzt.collections.primitives.IntMutableListX;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class IntRangeTest {

    @Test
    void testIntRange() {
        final IntRange intRange = IntRange.of(1, 100);

        assertAll(
                () -> assertTrue(intRange.contains(3)),
                () -> assertFalse(intRange.contains(101))
        );
    }

    @Test
    void testIterateIntRange() {
        final IntRange range = IntRange.of(2, 10);
        IntMutableListX list = IntMutableListX.empty();
        for (int i : range) {
            list.add(i);
        }
        for (int i : range) {
            list.add(i);
        }
        assertEquals(IntListX.of(2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 7, 8, 9), list);
    }

    @Test
    void testRange() {
        final IntRange range = IntRange.of(2, 10);
        assertIterableEquals(IntListX.of(2, 3, 4, 5, 6, 7, 8, 9), range);
    }

    @Test
    void testSteppedRange() {
        final IntRange range = IntRange.of(2, 20, 2);

        It.println("range = " + range);
        range.forEach(It::println);

        assertIterableEquals(IntListX.of(2, 4, 6, 8, 10, 12, 14, 16, 18), range);
    }

    @Test
    void testRangeClosed() {
        final IntRange range = IntRange.closed(2, 10);
        assertIterableEquals(IntListX.of(2, 3, 4, 5, 6, 7, 8, 9, 10), range);
    }

    @Test
    void testSteppedRangeClosed() {
        final IntRange range = IntRange.closed(2, 20, 2);

        It.println("range = " + range);
        range.forEach(It::println);

        assertIterableEquals(IntListX.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20), range);
    }

    @Test
    void testRangeClosedToArray() {
        assertArrayEquals(
                IntStream.rangeClosed(5, 10).toArray(),
                IntRange.closed(5, 10).toArray());
    }

    @Test
    void emptyIntRange() {
        final IntRange empty = IntRange.empty();
        empty.forEach(It::println);
        assertTrue(empty.none());
    }

}
