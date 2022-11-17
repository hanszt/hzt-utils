package org.hzt.utils.collections.primitives;

import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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

        final IntList shuffled = intListX.shuffled();

        System.out.println("shuffled = " + shuffled);

        assertNotEquals(intListX, shuffled);
    }

    @Test
    void testBinarySearch() {
        final IntList sortedList = IntList.of(-1, 0, 1, 2, 3, 4, 5);

        int valueToSearchFor = 2;

        final int indexInSortedList = sortedList.binarySearch(valueToSearchFor);

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testIndices() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");
        final IntList intList = IntList.of(2, 2, 2, 2, 2, 2, 3, 45, 1, 5);
        int[] indices1 = new int[intList.size()];
        final IntRange indices = intList.indices();
        for (PrimitiveIterator.OfInt iterator = indices.iterator(); iterator.hasNext(); ) {
            int i = iterator.nextInt();
            indices1[i] = i;
        }
        int[] indices2 = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            indices2[i] = i;
        }
        int[] indices3 = intList.indicesAsStream().toArray();

        assertAll(
                () -> assertArrayEquals(indices1, indices2),
                () -> assertArrayEquals(indices1, indices3),
                () -> assertTrue(indices.contains(4)),
                () -> assertFalse(indices.contains(intList.size()))
        );
    }
}
