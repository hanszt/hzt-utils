package org.hzt.utils.collections.primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntMutableCollectionTest {

    @Test
    void testRemoveAll() {
        final var list = IntMutableList.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final var removedAll = list.removeAll( 3, 4, 6, 5, 7, 8);

        assertAll(
                () -> assertTrue(removedAll),
                () -> assertEquals(IntList.of(1, 5, 9), list)
        );
    }

    @Test
    void testAddAll() {
        final var list = IntMutableList.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final var addedAll = list.addAll( 3, 4, 6, 5, 7, 8);

        assertAll(
                () -> assertTrue(addedAll),
                () -> assertEquals(IntList.of(1, 4, 5, 3, 6, 7, 5, 8, 9, 3, 4, 6, 5, 7, 8), list)
        );
    }

    @Test
    void testRemoveIf() {
        final var list = IntMutableList.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final var removed = list.removeIf(i -> i % 2 == 0);

        assertAll(
                () -> assertTrue(removed),
                () -> assertEquals(IntList.of(1, 5, 3, 7, 5, 9), list)
        );
    }
}
