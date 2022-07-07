package org.hzt.utils.collections.primitives;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static java.util.function.Predicate.not;
import static org.hzt.utils.Patterns.commaPattern;
import static org.hzt.utils.primitive_comparators.DoubleComparator.reverseOrder;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubleListXTest {

    @Test
    void testContainsAll() {
        final DoubleListX doubles = DoubleListX.of(1, 2, 3, 4, 5, 4, 6, 7, 4, 5);

        assertAll(
                () -> assertTrue(doubles.containsAll(1, 4, 2, 6, 5)),
                () -> assertFalse(doubles.containsAll(2, 3, 4, 3, 12))
        );
    }

    @Test
    void testLastIndexOf() {
        final DoubleListX list = DoubleListX.of(1, 2, 3, 4, 5, 4, 6, 7, 4, 5);

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
    void testIsSorted(String string) {
        final double[] doubles = toDoubleArrayByCommaPattern(string);

        System.out.println(Arrays.toString(doubles));

        final var isSorted = DoubleListX.of(doubles).isSorted();

        assertTrue(isSorted);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1, 2, 3, 2.2, -1.0, 6, 8, 3, 8, 8, 9",
            "100, 2.71",
            "2e10,1,1,1,1,1,1.1,2"
    })
    void testIsNotSorted(String string) {
        final double[] doubles = toDoubleArrayByCommaPattern(string);

        System.out.println(Arrays.toString(doubles));

        final var isSorted = DoubleListX.of(doubles).isSorted();

        assertFalse(isSorted);
    }

    private double[] toDoubleArrayByCommaPattern(String string) {
        return commaPattern.splitAsStream(string)
                .filter(not(String::isEmpty))
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    @Test
    void testBinarySearch() {
        final DoubleListX reverseOrderSortedList = DoubleListX.of(Double.POSITIVE_INFINITY, 5, 4, Math.PI, Math.E, 1, 0, -1);

        double valueToSearchFor = Math.E;

        final int indexInSortedList = reverseOrderSortedList
                .binarySearch(value -> reverseOrder().compareDouble(value, valueToSearchFor));

        assertEquals(4, indexInSortedList);
    }
}
