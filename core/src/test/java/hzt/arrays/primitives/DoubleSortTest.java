package hzt.arrays.primitives;

import hzt.arrays.primitves.DoubleSort;
import hzt.collections.ListX;
import hzt.collections.primitives.DoubleListX;
import hzt.numbers.DoubleX;
import hzt.sequences.Sequence;
import hzt.sequences.primitives.DoubleSequence;
import hzt.utils.It;
import hzt.utils.primitive_comparators.DoubleComparator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleSortTest {

    @Test
    void testTimSort() {
        final double[] array = Sequence.generate(100.0, i -> i - .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        System.out.println(Arrays.toString(array));

        DoubleSort.sort(array, Double::compare);

        System.out.println(Arrays.toString(array));

        final ListX<String> expected = DoubleListX.of(.1, .2, .3, .4, .5, .6).mapToObj(DoubleX::toRoundedString);
        final ListX<String> actual = DoubleSequence.of(array).take(6).toListX().mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

    @Test
    void testTimSortReversed() {
        final double[] array = Sequence.generate(0.0, i -> i + .1)
                .take(1_000)
                .shuffled()
                .mapToDouble(It::asDouble)
                .toArray();

        System.out.println(Arrays.toString(array));

        DoubleSort.sort(array, DoubleComparator.reverseOrder());

        System.out.println(Arrays.toString(array));

        final ListX<String> expected = DoubleListX.of(99.9, 99.8, 99.7, 99.6, 99.5, 99.4).mapToObj(DoubleX::toRoundedString);
        final ListX<String> actual = DoubleSequence.of(array).take(6).toListX().mapToObj(DoubleX::toRoundedString);

        assertEquals(expected, actual);
    }

}
