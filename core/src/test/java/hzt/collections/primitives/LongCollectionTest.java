package hzt.collections.primitives;

import hzt.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LongCollectionTest {

    @Test
    void longCollectionMap() {
        final LongListX longs = LongListX.of(1, 2, 3, 4, 5, 4, 3, 234).map(l -> l * 2);

        assertEquals(LongListX.of(2, 4, 6, 8, 10, 8, 6, 468), longs);
    }

    @Test
    void longCollectionFilter() {
        final LongListX longs = LongListX.of(1, 2, 3, 4, 5, 4, 3, 234).filter(l -> l > 4);

        assertEquals(LongListX.of(5, 234), longs);
    }

    @Test
    void longCollectionContainsAllInArray() {
        final long[] longs = LongSequence.generate(0L, l -> l + 2L).take(1_000).toArray();

        final LongListX longListX = LongSequence.generate(0, l -> ++l).take(4_000).toListX();

        assertTrue(longListX.containsAll(longs));
    }
}
