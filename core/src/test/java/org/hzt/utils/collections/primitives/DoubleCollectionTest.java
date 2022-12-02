package org.hzt.utils.collections.primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleCollectionTest {

    @Test
    void testSkip() {
        final var doubles = DoubleList.of(1, 2, 3, 4, 5, 6, 7);
        final var skipped = doubles.skip(3);
        assertEquals(DoubleList.of(4, 5, 6, 7), skipped);
    }

}
