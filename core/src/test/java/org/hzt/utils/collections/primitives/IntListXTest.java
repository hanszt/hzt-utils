package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntListXTest {

    @Test
    void testIntListContains() {
        final var ints = IntListX.of(1, 2, 3, 4, 5, 6, 7, 8, 4, 3, 2, 5, 2, 2342, 3, 23);

        assertAll(
                () -> assertTrue(ints.contains(3)),
                () -> assertFalse(ints.containsAll(IntSequence.of(1, 4, 3, 5, 4, 3, 5, 32))),
                () -> assertTrue(ints.containsAll(IntSequence.of(1, 4, 3, 5, 4, 3, 5, 2342)))
        );
    }

    @Test
    void testLargeIntList() {
        final var ints = IntSequence.generate(0, i -> ++i)
                .take(100_000)
                .toListX();

        final var ints1 = IntStream.iterate(0, i -> ++i)
                .limit(100_000)
                .boxed()
                .collect(Collectors.toList());

        assertAll(
                () -> assertEquals(ints1.size(), ints.size()),
                () -> assertTrue(ints.contains(23435))
        );
    }

    @Test
    void testLastIndexOf() {
        final var intListX = IntListX.of(1, 2, 4, 3, 5, 4, 3);

        final var index = intListX.lastIndexOf(4);

        assertEquals(5, index);
    }
}
