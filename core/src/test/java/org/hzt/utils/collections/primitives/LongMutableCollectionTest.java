package org.hzt.utils.collections.primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongMutableCollectionTest {

    @Test
    void testRemoveAllLongs() {
        final LongMutableListX list = LongMutableListX.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final boolean removedAll = list.removeAll( 3, 4, 6, 5, 7, 8);

        assertAll(
                () -> assertTrue(removedAll),
                () -> assertEquals(LongListX.of(1, 5, 9), list)
        );
    }

    @Test
    void testAddAllLongs() {
        final LongMutableListX list = LongMutableListX.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final boolean addedAll = list.addAll( 3, 4, 6, 5, 7, 8);

        assertAll(
                () -> assertTrue(addedAll),
                () -> assertEquals(LongListX.of(1, 4, 5, 3, 6, 7, 5, 8, 9, 3, 4, 6, 5, 7, 8), list)
        );
    }

    @Test
    void testRemoveIf() {
        final LongMutableListX list = LongMutableListX.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final boolean removed = list.removeIf(l -> l % 2 == 0);

        assertAll(
                () -> assertTrue(removed),
                () -> assertEquals(LongListX.of(1, 5, 3, 7, 5, 9), list)
        );
    }
}
