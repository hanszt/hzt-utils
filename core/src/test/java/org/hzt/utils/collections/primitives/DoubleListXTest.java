package org.hzt.utils.collections.primitives;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleListXTest {

    @Test
    void testContainsAll() {
        final var doubles = DoubleListX.of(1, 2, 3, 4, 5, 4, 6, 7, 4, 5);

        assertAll(
                () -> assertTrue(doubles.containsAll(1, 4, 2, 6, 5)),
                () -> assertFalse(doubles.containsAll(2, 3, 4, 3, 12))
        );
    }

    @Test
    void testLastIndexOf() {
        final var list = DoubleListX.of(1, 2, 3, 4, 5, 4, 6, 7, 4, 5);

        final var index = list.lastIndexOf(4);

        assertEquals(8, index);
    }
}
