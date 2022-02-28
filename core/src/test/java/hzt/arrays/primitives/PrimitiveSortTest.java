package hzt.arrays.primitives;

import hzt.arrays.primitves.PrimitiveSort;
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
                .mapToLong(It::asLong)
                .toArray();

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
