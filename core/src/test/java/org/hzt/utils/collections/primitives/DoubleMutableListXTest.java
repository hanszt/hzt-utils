package org.hzt.utils.collections.primitives;

import org.hzt.utils.It;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleMutableListXTest {

    @Test
    void testDifferentMethods() {
        final var doubles = DoubleMutableList.empty();

        assertTrue(doubles.isEmpty());
        doubles.add(Math.PI);
        assertAll(
                () -> assertFalse(doubles.isEmpty()),
                () -> assertEquals(1, doubles.size())
        );
        doubles.clear();
        doubles.add(2);
        doubles.add(7);
        doubles.add(Math.E);
        final var l = doubles.removeFirst();
        assertAll(
                () -> assertFalse(doubles.isEmpty()),
                () -> assertEquals(2L, l),
                () -> assertEquals(2, doubles.size())
        );
        It.println("doubles = " + doubles);
        final var l2 = doubles.removeLast();
        assertAll(
                () -> assertFalse(doubles.isEmpty()),
                () -> assertEquals(Math.E, l2),
                () -> assertEquals(1, doubles.size())
        );
    }

    @Test
    void testCopyConstructor() {
        final var doubles = DoubleMutableList.empty();
        doubles.add(2);
        doubles.add(-43);
        doubles.add(Math.E);
        DoubleSequence.iterate(0, l -> l + 5)
                .take(10_000_000)
                .forEachDouble(doubles::add);

        final var doublesCopy = DoubleMutableList.of(doubles);

        assertAll(
                () -> assertEquals(10_000_003, doubles.size()),
                () -> assertEquals(doubles, doublesCopy)
        );
    }

    @Test
    void testRemove() {
        final var list = DoubleMutableList.of(1, 2, 3, 4, 5, 6, 7, 8);

        final var remove = list.remove(7);
        It.println("list = " + list);

        assertAll(
                () -> assertTrue(remove),
                () -> assertEquals(DoubleMutableList.of(1, 2, 3, 4, 5, 6, 8), list)
        );
    }

    @Test
    void testSortSmallDoubleList() {
        final var doubles = DoubleSequence.iterate(0, i -> --i)
                .take(10)
                .toMutableList();

        doubles.sort();

        doubles.forEachDouble(System.out::println);

        assertEquals(DoubleMutableList.of(-9.0, -8.0, -7.0, -6.0, -5.0, -4.0, -3.0, -2.0, -1.0, 0.0), doubles);
    }
    @Test
    void testSortDoubleList() {
        final var doubles = DoubleSequence.iterate(1_000_000, i -> --i)
                .take(1_000_000)
                .toMutableList();

        doubles.sort();

        assertEquals(DoubleList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), doubles.take(10));
    }

    @Test
    void testSortReversedDoubleList() {
        final var doubles = DoubleSequence.iterate(-1_000_000, i -> ++i)
                .take(1_000_000)
                .toMutableList();

        doubles.sort(DoubleComparator.reverseOrder());

        assertEquals(DoubleList.of(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10), doubles.take(10));
    }
}
