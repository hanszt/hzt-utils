package org.hzt.utils.arrays.primitives;

import org.hzt.utils.It;
import org.hzt.utils.arrays.primitves.PrimitiveArrays;
import org.hzt.utils.collections.primitives.DoubleListX;
import org.hzt.utils.collections.primitives.IntListX;
import org.hzt.utils.collections.primitives.LongListX;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimitiveArraysTest {

    @Test
    void testIntTimSort() {
        final var array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(Integer::compare, array);

        It.println(Arrays.toString(array));

        assertEquals(IntListX.of(1, 2, 3, 4, 5, 6), IntSequence.of(array).take(6).toListX());
    }

    @Test
    void testIntTimSortReversed() {
        final var array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(IntComparator.reverseOrder(), array);

        It.println(Arrays.toString(array));

        assertEquals(IntListX.of(999, 998, 997, 996, 995, 994), IntSequence.of(array).take(6).toListX());
    }

    @Test
    void testLongTimSort() {
        final var array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(Long::compare, array);

        It.println(Arrays.toString(array));

        assertEquals(LongListX.of(1, 2, 3, 4, 5, 6), LongSequence.of(array).take(6).toListX());
    }

    @Test
    void testLongTimSortReversed() {
        final var array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(LongComparator.reverseOrder(), array);

        It.println(Arrays.toString(array));

        assertEquals(LongListX.of(999, 998, 997, 996, 995, 994), LongSequence.of(array).take(6).toListX());
    }

    @Test
    void testLongTimSortReversedSmall() {
        final var array = Sequence.generate(0, i -> ++i)
                .take(10)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(LongComparator.reverseOrder(), array);

        It.println(Arrays.toString(array));

        assertArrayEquals(new long[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, array);
    }

    @Test
    void testDoubleTimSort() {
        final var array = Sequence.generate(100.0, i -> i - .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(Double::compare, array);

        It.println(Arrays.toString(array));

        final var expected = DoubleListX.of(.1, .2, .3, .4, .5, .6)
                .mapToObj(DoubleX::toRoundedString);

        final var actual = DoubleSequence.of(array)
                .take(6)
                .toListX()
                .mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

    @Test
    void testDoubleTimSortReversed() {
        final var array = Sequence.generate(0.0, i -> i + .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(DoubleComparator.reverseOrder(), array);

        It.println(Arrays.toString(array));

        final var expected = DoubleListX.of(99.9, 99.8, 99.7, 99.6, 99.5, 99.4).mapToObj(DoubleX::toRoundedString);
        final var actual = DoubleSequence.of(array).take(6).toListX().mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

    @Test
    void testIntArrayToBooleanArray() {
        int[] input = {1, 4, 5, 3, 6, 7, 2, 7};

        final var booleans = PrimitiveArrays.toBooleanArray(i -> i < 4, input);

        final boolean[] expected = {true, false, false, true, false, false, true, false};

        assertArrayEquals(expected, booleans);
    }
    @Test
    void testLongArrayToBooleanArray() {
        long[] input = {1, 4, 5, 3, 6, 7, 2, Long.MAX_VALUE};

        final var booleans = PrimitiveArrays.toBooleanArray(l -> l < 4, input);

        final boolean[] expected = {true, false, false, true, false, false, true, false};

        assertArrayEquals(expected, booleans);
    }

    @Test
    void testDoubleArrayToBooleanArray() {
        double[] input = {Double.NEGATIVE_INFINITY, 1, 4, 5, Math.PI, 6, 7, Math.E, 7e1, Double.POSITIVE_INFINITY};

        final var booleans = PrimitiveArrays.toBooleanArray(d -> d < 4, input);

        final boolean[] expected = {true, true, false, false, true, false, false, true, false, false};

        assertArrayEquals(expected, booleans);
    }

    @Test
    void testIsSorted() {
        final var sorted = PrimitiveArrays.isSorted(IntComparator.reverseOrder(), 10, 9, 7, 7, 7, 6, 5, 2, -1);

        assertTrue(sorted);
    }

    @Test
    void testIsNotSorted() {
        final var sorted = PrimitiveArrays.isSorted(IntComparator.reverseOrder(), 10, 9, 7, 8, 7, 6, 4, 2, 5);

        assertFalse(sorted);
    }

}
