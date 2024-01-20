package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongCollectionTest {

    @Test
    void longCollectionMap() {
        final var longs = LongList.of(1, 2, 3, 4, 5, 4, 3, 234).map(l -> l * 2);

        assertEquals(LongList.of(2, 4, 6, 8, 10, 8, 6, 468), longs);
    }

    @Test
    void longCollectionFilter() {
        final var longs = LongList.of(1, 2, 3, 4, 5, 4, 3, 234).filter(l -> l > 4);

        assertEquals(LongList.of(5, 234), longs);
    }

    @Test
    void longCollectionContainsAllInArray() {
        final var longs = LongSequence.iterate(0L, l -> l + 2L).take(1_000).toArray();

        final var longListX = LongSequence.iterate(0, l -> ++l).take(4_000).toList();

        assertTrue(longListX.containsAll(longs));
    }
}
