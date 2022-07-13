package org.hzt.utils.collections.primitives;

import org.hzt.utils.It;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntMutableListXTest {

    @Test
    void testDifferentMethods() {
        final IntMutableList ints = IntMutableList.empty();
        assertTrue(ints.isEmpty());
        ints.add(1);
        assertAll(
                () -> assertFalse(ints.isEmpty()),
                () -> assertEquals(1, ints.size())
        );
        ints.clear();
        ints.add(2);
        ints.add(5);
        ints.add(7);
        final int l = ints.removeFirst();
        assertAll(
                () -> assertFalse(ints.isEmpty()),
                () -> assertEquals(2L, l),
                () -> assertEquals(2, ints.size())
        );
        It.println("ints = " + ints);
        final int l2 = ints.removeAt(0);
        assertAll(
                () -> assertFalse(ints.isEmpty()),
                () -> assertEquals(5L, l2),
                () -> assertEquals(1, ints.size())
        );

        ints.forEachInt(It::println);
    }

    @Test
    void testCopyConstructor() {
        final IntMutableList ints = IntMutableList.empty();
        ints.add(2);
        ints.add(-43);
        ints.add(1231);
        IntSequence.generate(0, l -> l + 5)
                .take(10_000_000)
                .forEachInt(ints::add);

        final IntMutableList intsCopy = IntMutableList.of(ints);

        assertAll(
                () -> assertEquals(10_000_003, ints.size()),
                () -> assertEquals(ints, intsCopy)
        );
    }

    @Test
    void testSortIntList() {
        final IntMutableList ints = IntSequence.generate(10_000, i -> --i)
                .take(10_000)
                .shuffled()
                .toMutableList();

        ints.sort();

        assertEquals(IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), ints.take(10));
    }

    @Test
    void testSortReversedIntList() {
        final IntMutableList ints = IntSequence.generate(-10_000, i -> ++i)
                .take(10_000)
                .shuffled()
                .toMutableList();

        ints.sort(IntComparator.reverseOrder());

        assertEquals(IntList.of(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10), ints.take(10));
    }

}
