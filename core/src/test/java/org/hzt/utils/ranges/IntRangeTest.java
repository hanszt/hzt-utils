package org.hzt.utils.ranges;

import org.hzt.utils.collections.primitives.IntListX;
import org.hzt.utils.collections.primitives.IntMutableListX;
import org.hzt.utils.It;
import org.junit.jupiter.api.BeforeAll;
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
        range.forEachInt(list::add);
        range.forEachInt(list::add);
        assertEquals(IntListX.of(2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 7, 8, 9), list);
    }

    @Test
    void testRange() {
        final IntListX range = IntRange.of(2, 10).toListX();
        assertEquals(IntListX.of(2, 3, 4, 5, 6, 7, 8, 9), range);
    }

    @Test
    void testSteppedRange() {
        final IntListX range = IntRange.of(2, 20, 2).toListX();

        It.println("range = " + range);
        range.forEach(It::println);

        assertEquals(IntListX.of(2, 4, 6, 8, 10, 12, 14, 16, 18), range);
    }

    @Test
    void testRangeClosed() {
        final IntListX range = IntRange.closed(2, 10).toListX();
        assertEquals(IntListX.of(2, 3, 4, 5, 6, 7, 8, 9, 10), range);
    }

    @Test
    void testSteppedRangeClosed() {
        final IntRange range = IntRange.closed(2, 20, 2);

        It.println("range = " + range);
        range.forEachInt(It::println);

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
        assertIterableEquals(IntListX.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20), range);
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");
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
