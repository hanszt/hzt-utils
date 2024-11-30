package org.hzt.utils.ranges;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class IntRangeTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntRangeTest.class);

    @Test
    void testIntRange() {
        final var intRange = IntRange.of(1, 100);

        assertAll(
                () -> assertTrue(intRange.contains(3)),
                () -> assertFalse(intRange.contains(101))
        );
    }

    @Test
    void testIterateIntRange() {
        final var range = IntRange.of(2, 10);
        final var list = IntMutableList.empty();
        range.forEachInt(list::add);
        range.forEachInt(list::add);

        assertEquals(IntMutableList.of(2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 7, 8, 9), list);
    }

    @Test
    void testRange() {
        final var range = IntRange.of(2, 10).toList();
        assertEquals(IntList.of(2, 3, 4, 5, 6, 7, 8, 9), range);
    }

    @Test
    void testSteppedRange() {
        final var range = IntRange.of(2, 20, 2).toList();

        LOGGER.atDebug().setMessage(() -> "range = " + range).log();
        range.forEachInt(it -> LOGGER.trace("{}", it));

        assertEquals(IntList.of(2, 4, 6, 8, 10, 12, 14, 16, 18), range);
    }

    @Test
    void testRangeClosed() {
        final var range = IntRange.closed(2, 10).toList();
        assertEquals(IntList.of(2, 3, 4, 5, 6, 7, 8, 9, 10), range);
    }

    @Test
    void testSteppedRangeClosed() {
        final var range = IntRange.closed(2, 20, 2);

        LOGGER.atDebug().setMessage(() -> "range = " + range).log();
        range.forEachInt(it -> LOGGER.trace("{}", it));

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
        assertIterableEquals(IntList.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20), range);
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
        final var empty = IntRange.empty();
        empty.forEach(it -> LOGGER.trace("{}", it));
        assertTrue(empty.none());
    }

}
