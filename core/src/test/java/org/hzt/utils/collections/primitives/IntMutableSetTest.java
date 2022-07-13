package org.hzt.utils.collections.primitives;

import org.hzt.utils.numbers.IntX;
import org.hzt.utils.ranges.IntRange;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntMutableSetTest {

    @Test
    void testIntSetSize() {
        IntMutableSet set = IntMutableSet.empty();
        set.addAll(IntList.of(-1, -2231, 0, 3, 3, 4, 5, 6, 5));

        assertEquals(7, set.size());
    }

    @Test
    void testCountElementsInIntSetThroughSequence() {
        IntMutableSet set = IntMutableSet.of(1, 2, 3, 3, 4, 5, 6, 5);

        final long count = set.asSequence().count();

        assertEquals(6L, count);
    }

    @Test
    void testSequenceToIntSet() {
        IntMutableSet ints = IntMutableSet.empty();
        ints.addAll(IntRange.of(-10_000, 9_000));
        ints.addAll(IntRange.of(1_000, 90_000));
        ints.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ints.add(100_000);

        final IntMutableSet evenInts = ints.asSequence()
                .filter(IntX::isEven)
                .collect(IntMutableSet::empty, IntMutableSet::add);

        assertAll(
                () -> assertTrue(ints.containsAll(evenInts)),
                () -> assertEquals(50_001, evenInts.size())
        );
    }

    @Test
    void testThrowsWhenIteratedOverEmptySet() {
        IntMutableSet set = IntMutableSet.empty();
        final PrimitiveIterator.OfInt iterator = set.iterator();
        assertThrows(NoSuchElementException.class, iterator::next);
    }

}
