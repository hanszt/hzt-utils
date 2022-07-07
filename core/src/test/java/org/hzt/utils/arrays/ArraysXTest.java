package org.hzt.utils.arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArraysXTest {

    @Test
    void testToBooleanArray() {
        String[] strings = {"This", "is", "a", "test"};

        boolean[] array = ArraysX.toBooleanArray(s -> s.contains("i"), strings);

        assertArrayEquals(new boolean[] {true, true, false, false}, array);
    }

}
