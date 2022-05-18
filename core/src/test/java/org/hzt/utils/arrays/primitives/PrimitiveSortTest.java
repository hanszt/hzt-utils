package org.hzt.utils.arrays.primitives;

import org.hzt.utils.arrays.primitves.PrimitiveSort;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.primitive_comparators.LongComparator;
import org.hzt.utils.collections.primitives.DoubleListX;
import org.hzt.utils.collections.primitives.IntListX;
import org.hzt.utils.collections.primitives.LongListX;
import org.hzt.utils.numbers.DoubleX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.It;
import org.hzt.utils.primitive_comparators.DoubleComparator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveSortTest {

    @Test
    void testIntTimSort() {
        final var array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveSort.sort(array, Integer::compare);

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

        PrimitiveSort.sort(array, IntComparator.reverseOrder());

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

        PrimitiveSort.sort(array, Long::compare);

        It.println(Arrays.toString(array));

        assertEquals(LongListX.of(1, 2, 3, 4, 5, 6), LongSequence.of(array).take(6).toListX());
    }

    @Test
    void testLongTimSortReversed() {
        final var array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .toLongArray(It::asLong);

        It.println(Arrays.toString(array));

        PrimitiveSort.sort(array, LongComparator.reverseOrder());

        It.println(Arrays.toString(array));

        assertEquals(LongListX.of(999, 998, 997, 996, 995, 994), LongSequence.of(array).take(6).toListX());
    }

    @Test
    void testDoubleTimSort() {
        final var array = Sequence.generate(100.0, i -> i - .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        It.println(Arrays.toString(array));

        PrimitiveSort.sort(array, Double::compare);

        It.println(Arrays.toString(array));

        final var expected = DoubleListX.of(.1, .2, .3, .4, .5, .6).mapToObj(DoubleX::toRoundedString);
        final var actual = DoubleSequence.of(array).take(6).toListX().mapToObj(DoubleX::toRoundedString);

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

        PrimitiveSort.sort(array, DoubleComparator.reverseOrder());

        It.println(Arrays.toString(array));

        final var expected = DoubleListX.of(99.9, 99.8, 99.7, 99.6, 99.5, 99.4).mapToObj(DoubleX::toRoundedString);
        final var actual = DoubleSequence.of(array).take(6).toListX().mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

}
