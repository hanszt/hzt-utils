package org.hzt.utils.iterables.primitives;

import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import static org.hzt.utils.arrays.primitves.PrimitiveArrays.toBooleanArray;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntArrayableTest {


    @Test
    void testToBooleanArray() {
        final boolean[] booleans = IntSequence.of(1, 2, 3, 4).toBooleanArray(i -> i < 3);

        final boolean[] expected = new boolean[]{true, true, false, false};

        assertArrayEquals(expected, booleans);
    }

    @Test
    void testIntGridToBooleanGrid() {
        int[][] grid = {
                {1, 2, 3, 4},
                {4, 3, 2, 1},
                {9, 3, 5, 2},
                {3, 4, 2, 5}};

        final boolean[][] booleans = Sequence.of(grid)
                .toArrayOf(row -> toBooleanArray(i -> i < 4, row), boolean[][]::new);

        assertEquals(4, booleans.length);
    }

}
