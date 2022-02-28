package hzt.collections.primitives;

import hzt.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LongListXTest {

    @Test
    void testSortedDescending() {
        final var longs = LongSequence.generate(0, l -> l + 5)
                .take(1_000_000)
                .toListX();

        final var sorted = longs.sortedDescending();

        final var expected = LongListX.of(4999995, 4999990, 4999985, 4999980);
        final var actual = sorted.asSequence().take(4).toListX();

        assertAll(
                () -> assertEquals(1_000_000, longs.size()),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testGet() {
        LongListX list = LongListX.of(1, 2, 3, 4, 5, -1, 3, 6, 3, 2, 5);
        final var value = list.get(4);
        assertEquals(5L, value);
    }

    @Test
    void testGetOutsideRangeYieldsIndexOutOfBound() {
        LongListX list = LongListX.of(1, 2, 3, 4, 5, -1, 3, 6, 3, 2, 5);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(14));
    }

}
