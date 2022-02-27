package hzt.arrays.primitives;

import hzt.arrays.primitves.PrimitiveSort;
import hzt.collections.ListX;
import hzt.utils.primitive_comparators.IntComparator;
import hzt.utils.primitive_comparators.LongComparator;
import hzt.collections.primitives.DoubleListX;
import hzt.collections.primitives.IntListX;
import hzt.collections.primitives.LongListX;
import hzt.numbers.DoubleX;
import hzt.sequences.Sequence;
import hzt.sequences.primitives.DoubleSequence;
import hzt.sequences.primitives.IntSequence;
import hzt.sequences.primitives.LongSequence;
import hzt.utils.It;
import hzt.utils.primitive_comparators.DoubleComparator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveSortTest {

    @Test
    void testIntTimSort() {
        final int[] array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        System.out.println(Arrays.toString(array));

        PrimitiveSort.sort(array, Integer::compare);

        System.out.println(Arrays.toString(array));

        assertEquals(IntListX.of(1, 2, 3, 4, 5, 6), IntSequence.of(array).take(6).toListX());
    }

    @Test
    void testIntTimSortReversed() {
        final int[] array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        System.out.println(Arrays.toString(array));

        PrimitiveSort.sort(array, IntComparator.reverseOrder());

        System.out.println(Arrays.toString(array));

        assertEquals(IntListX.of(999, 998, 997, 996, 995, 994), IntSequence.of(array).take(6).toListX());
    }

    @Test
    void testLongTimSort() {
        final long[] array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        System.out.println(Arrays.toString(array));

        PrimitiveSort.sort(array, Long::compare);

        System.out.println(Arrays.toString(array));

        assertEquals(LongListX.of(1, 2, 3, 4, 5, 6), LongSequence.of(array).take(6).toListX());
    }

    @Test
    void testLongTimSortReversed() {
        final long[] array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        System.out.println(Arrays.toString(array));

        PrimitiveSort.sort(array, LongComparator.reverseOrder());

        System.out.println(Arrays.toString(array));

        assertEquals(LongListX.of(999, 998, 997, 996, 995, 994), LongSequence.of(array).take(6).toListX());
    }

    @Test
    void testDoubleTimSort() {
        final double[] array = Sequence.generate(100.0, i -> i - .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        System.out.println(Arrays.toString(array));

        PrimitiveSort.sort(array, Double::compare);

        System.out.println(Arrays.toString(array));

        final ListX<String> expected = DoubleListX.of(.1, .2, .3, .4, .5, .6).mapToObj(DoubleX::toRoundedString);
        final ListX<String> actual = DoubleSequence.of(array).take(6).toListX().mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

    @Test
    void testDoubleTimSortReversed() {
        final double[] array = Sequence.generate(0.0, i -> i + .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        System.out.println(Arrays.toString(array));

        PrimitiveSort.sort(array, DoubleComparator.reverseOrder());

        System.out.println(Arrays.toString(array));

        final ListX<String> expected = DoubleListX.of(99.9, 99.8, 99.7, 99.6, 99.5, 99.4).mapToObj(DoubleX::toRoundedString);
        final ListX<String> actual = DoubleSequence.of(array).take(6).toListX().mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

}
