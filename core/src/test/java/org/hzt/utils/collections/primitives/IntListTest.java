package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class IntListTest {

    @Test
    void testIntListContains() {
        final var ints = IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 4, 3, 2, 5, 2, 2342, 3, 23);

        assertAll(
                () -> assertTrue(ints.contains(3)),
                () -> assertFalse(ints.containsAll(IntSequence.of(1, 4, 3, 5, 4, 3, 5, 32))),
                () -> assertTrue(ints.containsAll(IntSequence.of(1, 4, 3, 5, 4, 3, 5, 2342)))
        );
    }

    @Test
    void testLargeIntList() {
        final var ints = IntSequence.iterate(0, i -> ++i)
                .take(100_000)
                .toList();

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
        final var intListX = IntList.of(1, 2, 4, 3, 5, 4, 3);

        final var index = intListX.lastIndexOf(4);

        assertEquals(5, index);
    }

    @Test
    void testShuffled() {
        final var intListX = IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        final var shuffled = intListX.shuffled();

        System.out.println("shuffled = " + shuffled);

        assertNotEquals(intListX, shuffled);
    }

    @Test
    void testBinarySearch() {
        final var sortedList = IntList.of(-1, 0, 1, 2, 3, 4, 5);

        var valueToSearchFor = 2;

        final var indexInSortedList = sortedList.binarySearch(valueToSearchFor);

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testReversed() {
        final var intList = IntList.of(1, 2, 4, 3, 5, 4, 3);

        final var index = intList.reversed();

        assertEquals(IntList.of(3, 4, 5, 3, 4, 2, 1), index);
    }

    @Test
    void testIndices() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");
        final var intList = IntList.of(2, 2, 2, 2, 2, 2, 3, 45, 1, 5);
        var indices1 = new int[intList.size()];
        final var indices = intList.indices();
        for (final var iterator = indices.iterator(); iterator.hasNext(); ) {
            var i = iterator.nextInt();
            indices1[i] = i;
        }
        var indices2 = new int[intList.size()];
        for (var i = 0; i < intList.size(); i++) {
            indices2[i] = i;
        }
        var indices3 = intList.indicesAsStream().toArray();

        assertAll(
                () -> assertArrayEquals(indices1, indices2),
                () -> assertArrayEquals(indices1, indices3),
                () -> assertTrue(indices.contains(4)),
                () -> assertFalse(indices.contains(intList.size()))
        );
    }

    @Test
    void testMutableListAndImmutableListWithSameElementsAreEqualAccordingToEqualsMethod() {
        IntList list1 = IntList.of(1, 2, 5, 4, 5, 6, 8);
        IntList list2 = IntMutableList.of(1, 2, 5, 4, 5, 6, 8);

        assertAll(
                () -> assertEquals(list2, list1),
                () -> assertEquals(list1, list2)
        );
    }

    @Test
    void testMutableListAndImmutableListWithSameElementsHaveSameHashCode() {
        IntList list1 = IntList.of(1, 2, 5, 4, 5, 6, 8);
        IntList list2 = IntMutableList.of(1, 2, 5, 4, 5, 6, 8);

        assertEquals(list2.hashCode(), list1.hashCode());
    }
}
