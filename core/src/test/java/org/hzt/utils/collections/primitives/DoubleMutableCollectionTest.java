package org.hzt.utils.collections.primitives;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleMutableCollectionTest {

    @Test
    void testRemoveAll() {
        final DoubleMutableList list = DoubleMutableList.of(1, 4, 5, 3, Math.PI, 7, 5, 8, 9);
        final boolean removedAll = list.removeAll( 3, 4, 5, 7, Math.PI);

        assertAll(
                () -> assertTrue(removedAll),
                () -> assertEquals(DoubleMutableList.of(1, 5, 8, 9), list)
        );
    }

    @Test
    void testAddAll() {
        final DoubleMutableList list = DoubleMutableList.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final boolean addedAll = list.addAll( 3, 4, 6, 5, 7, 8, Math.E);

        assertAll(
                () -> assertTrue(addedAll),
                () -> assertEquals(DoubleMutableList.of(1, 4, 5, 3, 6, 7, 5, 8, 9, 3, 4, 6, 5, 7, 8, Math.E), list)
        );
    }

    @Test
    void testStreamFromDoubleCollection() {
        final DoubleList listX = IntSequence.iterate(1, d -> d + 2)
                .mapToDouble(i -> 1. / i)
                .take(1_000_000)
                .toList();

        final double sum = listX.stream().sum();

        It.println("sum = " + sum);

        assertTrue(sum < 10);
    }

    @Test
    void testRemoveIf() {
        final DoubleMutableList list = DoubleMutableList.of(Math.PI, 1, 4, 5, 3, 6, Math.E, 7, 5, 8, 9);
        final boolean removed = list.removeIf(l -> l % 2 == 0);

        assertAll(
                () -> assertTrue(removed),
                () -> assertEquals(DoubleMutableList.of(Math.PI, 1, 5, 3, Math.E, 7, 5, 9), list)
        );
    }
}
