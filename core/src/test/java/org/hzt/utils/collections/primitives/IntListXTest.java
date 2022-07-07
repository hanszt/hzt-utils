package org.hzt.utils.collections.primitives;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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
                .toList();

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

    @Test
    void testShuffled() {
        final var intListX = IntListX.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        final var shuffled = intListX.shuffled();

        System.out.println("shuffled = " + shuffled);

        assertNotEquals(intListX, shuffled);
    }

    @Test
    void testBinarySearch() {
        final IntListX sortedList = IntListX.of(-1, 0, 1, 2, 3, 4, 5);

        int valueToSearchFor = 2;

        final int indexInSortedList = sortedList.binarySearch(value -> value - valueToSearchFor);

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testIndices() {
        final var intList = IntListX.of(2, 2, 2, 2, 2, 2, 3, 45, 1, 5);
        int[] indices1 = new int[intList.size()];
        final var indices = intList.indices();
        for (int i : indices) {
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
