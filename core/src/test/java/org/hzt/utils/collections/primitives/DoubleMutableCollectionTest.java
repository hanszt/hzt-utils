package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class DoubleMutableCollectionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoubleMutableCollectionTest.class);

    @Test
    void testRemoveAll() {
        final var list = DoubleMutableList.of(1, 4, 5, 3, Math.PI, 7, 5, 8, 9);
        final var removedAll = list.removeAll( 3, 4, 5, 7, Math.PI);

        assertAll(
                () -> assertTrue(removedAll),
                () -> assertEquals(DoubleMutableList.of(1, 5, 8, 9), list)
        );
    }

    @Test
    void testAddAll() {
        final var list = DoubleMutableList.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final var addedAll = list.addAll( 3, 4, 6, 5, 7, 8, Math.E);

        assertAll(
                () -> assertTrue(addedAll),
                () -> assertEquals(DoubleMutableList.of(1, 4, 5, 3, 6, 7, 5, 8, 9, 3, 4, 6, 5, 7, 8, Math.E), list)
        );
    }

    @Test
    void testStreamFromDoubleCollection() {
        final var listX = IntSequence.iterate(1, d -> d + 2)
                .mapToDouble(i -> 1. / i)
                .take(1_000_000)
                .toList();

        final var sum = listX.stream().sum();

        LOGGER.atDebug().setMessage(() -> "sum = " + sum).log();

        assertTrue(sum < 10);
    }

    @Test
    void testRemoveIf() {
        final var list = DoubleMutableList.of(Math.PI, 1, 4, 5, 3, 6, Math.E, 7, 5, 8, 9);
        final var removed = list.removeIf(l -> l % 2 == 0);

        assertAll(
                () -> assertTrue(removed),
                () -> assertEquals(DoubleMutableList.of(Math.PI, 1, 5, 3, Math.E, 7, 5, 9), list)
        );
    }
}
