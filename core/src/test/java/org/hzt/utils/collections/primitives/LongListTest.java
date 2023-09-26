package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Test;

import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LongListTest {

    @Test
    void testSortedDescending() {
        final var longs = LongSequence.iterate(0, l -> l + 5)
                .take(1_000_000)
                .toList();

        final var sorted = longs.sortedDescending();

        final var expected = LongList.of(4999995, 4999990, 4999985, 4999980);
        final var actual = sorted.asSequence().take(4).toList();

        assertAll(
                () -> assertEquals(1_000_000, longs.size()),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testGet() {
        final var list = LongList.of(1, 2, 3, 4, 5, -1, 3, 6, 3, 2, 5);
        final var value = list.get(4);
        assertEquals(5L, value);
    }

    @Test
    void testGetOutsideRangeYieldsIndexOutOfBound() {
        final var list = LongList.of(1, 2, 3, 4, 5, -1, 3, 6, 3, 2, 5);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(14));
    }

    @Test
    void testBinarySearch() {
        final var sortedList = LongList.of(-1, 0, 1, 2, 3, 4, 5);

        final var valueToSearchFor = 2;

        final var indexInSortedList = sortedList.binarySearch(valueToSearchFor);

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testFindLast() {
        final var longs = LongList.of(1, 2, 3, 4, 3, 5, 2, 5, 4, 3, 8, 2, 3, 4);

        final var last = longs.findLast(l -> l > 4);

        assertEquals(OptionalLong.of(8), last);
    }

}
