package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleMutableCollectionTest {

    @Test
    void testRemoveAll() {
        final DoubleMutableListX list = DoubleMutableListX.of(1, 4, 5, 3, Math.PI, 7, 5, 8, 9);
        final boolean removedAll = list.removeAll( 3, 4, 5, 7, Math.PI);

        assertAll(
                () -> assertTrue(removedAll),
                () -> assertEquals(DoubleListX.of(1, 5, 8, 9), list)
        );
    }

    @Test
    void testAddAll() {
        final DoubleMutableListX list = DoubleMutableListX.of(1, 4, 5, 3, 6, 7, 5, 8, 9);
        final boolean addedAll = list.addAll( 3, 4, 6, 5, 7, 8, Math.E);

        assertAll(
                () -> assertTrue(addedAll),
                () -> assertEquals(DoubleListX.of(1, 4, 5, 3, 6, 7, 5, 8, 9, 3, 4, 6, 5, 7, 8, Math.E), list)
        );
    }

    @Test
    void streamFromDoubleCollection() {
        DoubleListX listX = IntSequence.generate(1, d -> d + 2)
                .mapToDouble(i -> 1. / i)
                .take(1_000_000)
                .toListX();

        final double sum = listX.stream().sum();

        It.println("sum = " + sum);

        assertTrue(sum < 10);
    }
}
