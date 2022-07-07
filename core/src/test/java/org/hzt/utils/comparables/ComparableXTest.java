package org.hzt.utils.comparables;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComparableXTest {

    @Test
    void testSmallerOrEqual() {
        final var isSortedInNaturalOrder = Sequence.of(IntX.of(1), IntX.of(2), IntX.of(3))
                .zipWithNext()
                .all(IntX::smallerOrEqual);

        assertTrue(isSortedInNaturalOrder);
    }

    @Test
    void testGreaterOrEqual() {
        final var isSortedInReverseOrder = Sequence.of(IntX.of(3), IntX.of(2), IntX.of(1))
                .zipWithNext()
                .all(IntX::greaterOrEqual);

        assertTrue(isSortedInReverseOrder);
    }

}
