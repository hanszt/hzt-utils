package org.hzt.utils.arrays;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ArraysXTest {

    @Test
    void testToBooleanArray() {
        String[] strings = {"This", "is", "a", "test"};

        boolean[] array = ArraysX.toBooleanArray(s -> s.contains("i"), strings);

        assertArrayEquals(new boolean[] {true, true, false, false}, array);
    }

    @Test
    void testReverseArray() {
        String[] strings = {"this", "is", "a", "test"};
        final String[] strings2 = ArraysX.copyOf(strings);

        Arrays.sort(strings, Comparator.reverseOrder());
        Arrays.sort(strings2);
        ArraysX.reverse(strings2);

        System.out.println(Arrays.toString(strings));

        assertArrayEquals(strings, strings2);
    }

}
