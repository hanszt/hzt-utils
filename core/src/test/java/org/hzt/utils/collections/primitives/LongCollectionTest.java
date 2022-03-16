package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.LongSequence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongCollectionTest {

    @BeforeAll
    static void setup() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");
    }

    @Test
    void longCollectionMap() {
        final var longs = LongListX.of(1, 2, 3, 4, 5, 4, 3, 234).map(l -> l * 2);

        assertEquals(LongListX.of(2, 4, 6, 8, 10, 8, 6, 468), longs);
    }

    @Test
    void longCollectionFilter() {
        final var longs = LongListX.of(1, 2, 3, 4, 5, 4, 3, 234).filter(l -> l > 4);

        assertEquals(LongListX.of(5, 234), longs);
    }

    @Test
    void longCollectionContainsAllInArray() {
        final var longs = LongSequence.generate(0L, l -> l + 2L).take(1_000).toArray();

        final var longListX = LongSequence.generate(0, l -> ++l).take(4_000).toListX();

        assertTrue(longListX.containsAll(longs));
    }
}
