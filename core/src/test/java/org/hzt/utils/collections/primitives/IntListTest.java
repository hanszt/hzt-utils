package org.hzt.utils.collections.primitives;

import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntListTest {

    @Test
    void testIntListContains() {
        final IntList ints = IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 4, 3, 2, 5, 2, 2342, 3, 23);

        assertAll(
                () -> assertTrue(ints.contains(3)),
                () -> assertFalse(ints.containsAll(IntSequence.of(1, 4, 3, 5, 4, 3, 5, 32))),
                () -> assertTrue(ints.containsAll(IntSequence.of(1, 4, 3, 5, 4, 3, 5, 2342)))
        );
    }

    @Test
    void testLargeIntList() {
        final IntList ints = IntSequence.iterate(0, i -> ++i)
                .take(100_000)
                .toList();

        final List<Integer> ints1 = IntStream.iterate(0, i -> ++i)
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
        final IntList intListX = IntList.of(1, 2, 4, 3, 5, 4, 3);

        final int index = intListX.lastIndexOf(4);

        assertEquals(5, index);
    }

    @Test
    void testShuffled() {
        final IntList intListX = IntList.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        final IntList shuffled = intListX.shuffled(new Random(0));

        System.out.println("shuffled = " + shuffled);

        assertEquals(IntList.of(5, 9, 10, 7, 4, 6, 3, 2, 8, 1), shuffled);
        assertNotEquals(intListX, shuffled);
    }

    @Test
    void testBinarySearch() {
        final IntList sortedList = IntList.of(-1, 0, 1, 2, 3, 4, 5);

        final int valueToSearchFor = 2;

        final int indexInSortedList = sortedList.binarySearch(valueToSearchFor);

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testIndices() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");
        final IntList intList = IntList.of(2, 2, 2, 2, 2, 2, 3, 45, 1, 5);
        final int[] indices1 = new int[intList.size()];
        final IntRange indices = intList.indices();
        for (final PrimitiveIterator.OfInt iterator = indices.iterator(); iterator.hasNext(); ) {
            final int i = iterator.nextInt();
            indices1[i] = i;
        }
        final int[] indices2 = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            indices2[i] = i;
        }
        final int[] indices3 = intList.indicesAsStream().toArray();

        assertAll(
                () -> assertArrayEquals(indices1, indices2),
                () -> assertArrayEquals(indices1, indices3),
                () -> assertTrue(indices.contains(4)),
                () -> assertFalse(indices.contains(intList.size()))
        );
    }

    @Test
    void testMutableListAndImmutableListWithSameElementsAreEqualAccordingToEqualsMethod() {
        final IntList list1 = IntList.of(1, 2, 5, 4, 5, 6, 8);
        final IntList list2 = IntMutableList.of(1, 2, 5, 4, 5, 6, 8);

        assertAll(
                () -> assertEquals(list2, list1),
                () -> assertEquals(list1, list2)
        );
    }

    @Test
    void testMutableListAndImmutableListWithSameElementsHaveSameHashCode() {
        final IntList list1 = IntList.of(1, 2, 5, 4, 5, 6, 8);
        final IntList list2 = IntMutableList.of(1, 2, 5, 4, 5, 6, 8);

        assertEquals(list2.hashCode(), list1.hashCode());
    }
}
