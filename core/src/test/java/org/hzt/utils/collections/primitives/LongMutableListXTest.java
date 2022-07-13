package org.hzt.utils.collections.primitives;

import org.hzt.utils.It;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongMutableListXTest {

    @Test
    void testDifferentMethods() {
        final var longs = LongMutableList.empty();
        assertTrue(longs.isEmpty());
        longs.add(1);
        assertAll(
                () -> assertFalse(longs.isEmpty()),
                () -> assertEquals(1, longs.size())
        );
        longs.clear();
        longs.add(2);
        longs.add(5);
        longs.add(7);
        final var l = longs.removeFirst();
        assertAll(
                () -> assertFalse(longs.isEmpty()),
                () -> assertEquals(2L, l),
                () -> assertEquals(2, longs.size())
        );
        It.println("longs = " + longs);
        final var l2 = longs.removeAt(0);
        assertAll(
                () -> assertFalse(longs.isEmpty()),
                () -> assertEquals(5L, l2),
                () -> assertEquals(1, longs.size())
        );
    }

    @Test
    void testCopyConstructor() {
        final var longs = LongMutableList.empty();
        longs.add(2);
        longs.add(-43);
        longs.add(1231);
        LongSequence.generate(0, l -> l + 5)
                .take(10_000_000)
                .forEachLong(longs::add);

        final var longsCopy = LongMutableList.of(longs);

        assertAll(
                () -> assertEquals(10_000_003, longs.size()),
                () -> assertEquals(longs, longsCopy)
        );
    }

    @Test
    void testSet() {
        final var list = LongMutableList.of(1, 2, 3, 4, 5, -1, 3, 6, 3, 2, 5);
        list.set(4, 14);
        final var value = list.get(4);
        assertEquals(14L, value);
    }

    @Test
    void testSortLongList() {
        final var longs = LongSequence.generate(1_000_000, i -> --i)
                .take(1_000_000)
                .toMutableList();

        longs.sort();

        assertEquals(LongList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), longs.take(10));
    }

    @Test
    void testSortReversedLongList() {
        final var longs = LongSequence.generate(-1_000_000, i -> ++i)
                .take(1_000_000)
                .toMutableList();

        longs.sort(LongComparator.reverseOrder());

        assertEquals(LongList.of(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10), longs.take(10));
    }
}
