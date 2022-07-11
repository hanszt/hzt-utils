package org.hzt.utils.collections.primitives;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.ranges.IntRange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntMutableSetXTest {

    @Test
    void testIntSetSize() {
        IntMutableSetX set = IntMutableSetX.empty();
        set.addAll(IntListX.of(-1, -2231, 0, 3, 3, 4, 5, 6, 5));

        assertEquals(7, set.size());
    }

    @Test
    void testCountElementsInIntSetThroughSequence() {
        IntMutableSetX set = IntMutableSetX.empty();
        set.addAll(IntListX.of(1, 2, 3, 3, 4, 5, 6, 5));

        final var count = set.asSequence().count();

        assertEquals(6L, count);
    }

    @Test
    void testSequenceToIntSet() {
        IntMutableSetX ints = IntMutableSetX.empty();
        ints.addAll(IntRange.of(-10_000, 9_000));
        ints.addAll(IntRange.of(1_000, 90_000));
        ints.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ints.add(100_000);

        final var evenInts = ints.asSequence()
                .filter(IntX::isEven)
                .collect(IntMutableSetX::empty, IntMutableSetX::add);

        assertAll(
                () -> assertTrue(ints.containsAll(evenInts)),
                () -> assertEquals(50_001, evenInts.size())
        );
    }

}
