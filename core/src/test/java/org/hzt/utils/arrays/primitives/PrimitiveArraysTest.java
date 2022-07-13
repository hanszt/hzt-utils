package org.hzt.utils.arrays.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.primitives.DoubleList;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrimitiveArraysTest {

    @Test
    void testIntTimSort() {
        final int[] array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(Integer::compare, array);

        It.println(Arrays.toString(array));

        assertEquals(IntList.of(1, 2, 3, 4, 5, 6), IntSequence.of(array).take(6).toList());
    }

    @Test
    void testIntTimSortReversed() {
        final int[] array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(IntComparator.reverseOrder(), array);

        It.println(Arrays.toString(array));

        assertEquals(IntList.of(999, 998, 997, 996, 995, 994), IntSequence.of(array).take(6).toList());
    }

    @Test
    void testLongTimSort() {
        final long[] array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(Long::compare, array);

        It.println(Arrays.toString(array));

        assertEquals(LongList.of(1, 2, 3, 4, 5, 6), LongSequence.of(array).take(6).toList());
    }

    @Test
    void testLongTimSortReversed() {
        final long[] array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(LongComparator.reverseOrder(), array);

        It.println(Arrays.toString(array));

        assertEquals(LongList.of(999, 998, 997, 996, 995, 994), LongSequence.of(array).take(6).toList());
    }

    @Test
    void testLongTimSortReversedSmall() {
        final long[] array = Sequence.generate(0, i -> ++i)
                .take(10)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(LongComparator.reverseOrder(), array);

        It.println(Arrays.toString(array));

        assertArrayEquals(new long[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, array);
    }

    @Test
    void testDoubleTimSort() {
        final double[] array = Sequence.generate(100.0, i -> i - .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(Double::compare, array);

        It.println(Arrays.toString(array));

        final ListX<String> expected = DoubleList.of(.1, .2, .3, .4, .5, .6)
                .mapToObj(DoubleX::toRoundedString);

        final ListX<String> actual = DoubleSequence.of(array)
                .take(6)
                .toList()
                .mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

    @Test
    void testDoubleTimSortReversed() {
        final double[] array = Sequence.generate(0.0, i -> i + .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveArrays.sort(DoubleComparator.reverseOrder(), array);

        It.println(Arrays.toString(array));

        final ListX<String> expected = DoubleList.of(99.9, 99.8, 99.7, 99.6, 99.5, 99.4).mapToObj(DoubleX::toRoundedString);
        final ListX<String> actual = DoubleSequence.of(array).take(6).toList().mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

    @Test
    void testIntArrayToBooleanArray() {
        int[] input = {1, 4, 5, 3, 6, 7, 2, 7};

        final boolean[] booleans = PrimitiveArrays.toBooleanArray(i -> i < 4, input);

        final boolean[] expected = {true, false, false, true, false, false, true, false};

        assertArrayEquals(expected, booleans);
    }

    @Test
    void testLongArrayToBooleanArray() {
        long[] input = {1, 4, 5, 3, 6, 7, 2, Long.MAX_VALUE};

        final boolean[] booleans = PrimitiveArrays.toBooleanArray(l -> l < 4, input);

        final boolean[] expected = {true, false, false, true, false, false, true, false};

        assertArrayEquals(expected, booleans);
    }

    @Test
    void testDoubleArrayToBooleanArray() {
        double[] input = {Double.NEGATIVE_INFINITY, 1, 4, 5, Math.PI, 6, 7, Math.E, 7e1, Double.POSITIVE_INFINITY};

        final boolean[] booleans = PrimitiveArrays.toBooleanArray(d -> d < 4, input);

        final boolean[] expected = {true, true, false, false, true, false, false, true, false, false};

        assertArrayEquals(expected, booleans);
    }

    @Test
    void testIsSorted() {
        final boolean sorted = PrimitiveArrays.isSorted(IntComparator.reverseOrder(), 10, 9, 7, 7, 7, 6, 5, 2, -1);

        assertTrue(sorted);
    }

    @Test
    void testIsNotSorted() {
        final boolean sorted = PrimitiveArrays.isSorted(IntComparator.reverseOrder(), 10, 9, 7, 8, 7, 6, 4, 2, 5);

        assertFalse(sorted);
    }

    @Nested
    class ReverseMethodTests {

        @Test
        void testSortAndReverseInts() {
            int[] array = {1, 3, 4, 2, 5, 6, 7};
            Arrays.sort(array);
            PrimitiveArrays.reverse(array);
            assertArrayEquals(new int[]{7, 6, 5, 4, 3, 2, 1}, array);
        }

        @Test
        void testSortAndReverseLongs() {
            long[] array = {1, 3, Long.MAX_VALUE, 4, 2, 5, 6, 7};
            Arrays.sort(array);
            PrimitiveArrays.reverse(array);
            assertArrayEquals(new long[]{Long.MAX_VALUE, 7, 6, 5, 4, 3, 2, 1}, array);
        }

        @Test
        void testSortAndReverseDoubles() {
            double[] array = {1, 3, 4, Math.PI, 2, 5, DoubleX.GOLDEN_RATIO};
            Arrays.sort(array);
            PrimitiveArrays.reverse(array);
            assertArrayEquals(new double[]{5, 4, Math.PI, 3, 2, DoubleX.GOLDEN_RATIO, 1}, array);
        }
    }
}
