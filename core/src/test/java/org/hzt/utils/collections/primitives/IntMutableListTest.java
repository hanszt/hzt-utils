package org.hzt.utils.collections.primitives;

import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class IntMutableListTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntMutableListTest.class);

    private static final Random random = new Random();

    @Test
    void testDifferentMethods() {
        final var ints = IntMutableList.empty();
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
        final var l = ints.removeFirst();
        assertAll(
                () -> assertFalse(ints.isEmpty()),
                () -> assertEquals(2L, l),
                () -> assertEquals(2, ints.size())
        );
        LOGGER.atDebug().setMessage(() -> "ints = " + ints).log();
        final var l2 = ints.removeAt(0);
        assertAll(
                () -> assertFalse(ints.isEmpty()),
                () -> assertEquals(5L, l2),
                () -> assertEquals(1, ints.size())
        );

        ints.forEachInt(it -> LOGGER.trace("{}", it));
    }

    @Test
    void testAddAtIndex() {
        final var list = IntMutableList.of(1, 2, 5);
        list.add(list.size(), 3);
        final List<Integer> refList = new ArrayList<>(List.of(1, 2, 5));
        refList.add(refList.size(), 3);

        LOGGER.atDebug().setMessage(() -> "list = " + list).log();
        assertAll(
                () -> assertEquals(IntMutableList.of(1, 2, 5, 3), list),
                () -> assertEquals(List.of(1, 2, 5, 3), refList)
        );
    }

    @Test
    void testCopyConstructor() {
        final var ints = IntMutableList.empty();
        ints.add(2);
        ints.add(-43);
        ints.add(1231);
        IntSequence.iterate(0, l -> l + 5)
                .take(10_000_000)
                .forEachInt(ints::add);

        final var intsCopy = IntMutableList.of(ints);

        assertAll(
                () -> assertEquals(10_000_003, ints.size()),
                () -> assertEquals(ints, intsCopy)
        );
    }

    @Test
    void testSortIntList() {
        final var ints = IntSequence.iterate(10_000, i -> --i)
                .take(10_000)
                .shuffled(new Random(0))
                .toMutableList();

        ints.sort();

        assertEquals(IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), ints.take(10));
    }

    @Test
    void testSortReversedIntList() {
        final var ints = IntSequence.iterate(-10_000, i -> ++i)
                .take(10_000)
                .shuffled(random)
                .toMutableList();

        ints.sort(IntComparator.reverseOrder());

        assertEquals(IntList.of(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10), ints.take(10));
    }

    @Nested
    class AddTests {

        @Test
        void testAddAllAtIndex() {
            final var list = IntMutableList.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            list.addAll(4, IntList.of(1, 2, 3));

            assertEquals(IntList.of(1, 2, 3, 4, 1, 2, 3, 5, 6, 7, 8, 9), list);
        }

        @Test
        void testAddAll() {
            final var list = IntMutableList.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
            list.addAll(IntList.of(1, 2, 3));

            assertEquals(IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3), list);
        }
    }

}
