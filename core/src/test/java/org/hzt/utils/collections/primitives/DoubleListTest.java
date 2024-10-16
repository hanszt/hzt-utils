package org.hzt.utils.collections.primitives;

import org.hzt.utils.arrays.ArraysX;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.hzt.utils.Patterns.commaPattern;
import static org.hzt.utils.function.Functions.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleListTest {

    @Test
    void testContainsAll() {
        final DoubleList doubles = DoubleList.of(1, 2, 3, 4, 5, 4, 6, 7, 4, 5);

        assertAll(
                () -> assertTrue(doubles.containsAll(1, 4, 2, 6, 5)),
                () -> assertFalse(doubles.containsAll(2, 3, 4, 3, 12))
        );
    }

    @Test
    void testLastIndexOf() {
        final DoubleList list = DoubleList.of(1, 2, 3, 4, 5, 4, 6, 7, 4, 5);

        final int index = list.lastIndexOf(4);

        assertEquals(8, index);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1.62, 2, 3.14159, 4, 5, 6, 7, 8, 8, 8, 9e2",
            "",
            "100.4",
            "1,1,1,1,1,1,1.1,2"
    })
    void testIsSorted(final String string) {
        final double[] doubles = toDoubleArrayByCommaPattern(string);

        System.out.println(Arrays.toString(doubles));

        final boolean isSorted = ArraysX.isSorted(DoubleList.of(doubles).toArray());

        assertTrue(isSorted);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1, 2, 3, 2.2, -1.0, 6, 8, 3, 8, 8, 9",
            "100, 2.71",
            "2e10,1,1,1,1,1,1.1,2"
    })
    void testIsNotSorted(final String string) {
        final double[] doubles = toDoubleArrayByCommaPattern(string);

        System.out.println(Arrays.toString(doubles));

        final boolean isSorted = ArraysX.isSorted(DoubleList.of(doubles).toArray());

        assertFalse(isSorted);
    }

    private double[] toDoubleArrayByCommaPattern(final String string) {
        return commaPattern.splitAsStream(string)
                .filter(not(String::isEmpty))
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    @Test
    void testBinarySearch() {
        final DoubleList sortedList = DoubleList.of(-1, 0, 1, Math.E, Math.PI, 4, 5, Double.POSITIVE_INFINITY);

        final int indexInSortedList = sortedList.binarySearch(Math.E);

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testBinarySearchReversedSortedList() {
        final DoubleList reverseOrderSortedList = DoubleList.of(Double.POSITIVE_INFINITY, 5, 4, Math.PI, Math.E, 1, 0, -1);

        final int indexInSortedList = reverseOrderSortedList.binarySearch(value -> Double.compare(Math.E, value));

        assertEquals(4, indexInSortedList);
    }
}
