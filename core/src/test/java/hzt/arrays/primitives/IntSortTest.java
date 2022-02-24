package hzt.arrays.primitives;

import hzt.arrays.primitves.IntSort;
import hzt.collections.primitives.IntListX;
import hzt.sequences.Sequence;
import hzt.sequences.primitives.IntSequence;
import hzt.utils.It;
import hzt.utils.primitive_comparators.IntComparator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntSortTest {

    @Test
    void testTimSort() {
        final int[] array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        System.out.println(Arrays.toString(array));

        IntSort.sort(array, Integer::compare);

        System.out.println(Arrays.toString(array));

        assertEquals(IntListX.of(1, 2, 3, 4, 5, 6), IntSequence.of(array).take(6).toListX());
    }

    @Test
    void testTimSortReversed() {
        final int[] array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .mapToInt(It::asInt)
                .toArray();

        System.out.println(Arrays.toString(array));

        IntSort.sort(array, IntComparator.reverseOrder());

        System.out.println(Arrays.toString(array));

        assertEquals(IntListX.of(999, 998, 997, 996, 995, 994), IntSequence.of(array).take(6).toListX());
    }
}
