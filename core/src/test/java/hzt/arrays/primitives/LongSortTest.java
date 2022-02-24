package hzt.arrays.primitives;

import hzt.arrays.primitves.LongSort;
import hzt.collections.primitives.LongListX;
import hzt.sequences.Sequence;
import hzt.sequences.primitives.LongSequence;
import hzt.utils.It;
import hzt.utils.primitive_comparators.LongComparator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongSortTest {

    @Test
    void testTimSort() {
        final long[] array = Sequence.generate(1_000, i -> --i)
                .take(1_000)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        System.out.println(Arrays.toString(array));

        LongSort.sort(array, Long::compare);

        System.out.println(Arrays.toString(array));

        assertEquals(LongListX.of(1, 2, 3, 4, 5, 6), LongSequence.of(array).take(6).toListX());
    }

    @Test
    void testTimSortReversed() {
        final long[] array = Sequence.generate(0, i -> ++i)
                .take(1_000)
                .shuffled()
                .mapToLong(It::asLong)
                .toArray();

        System.out.println(Arrays.toString(array));

        LongSort.sort(array, LongComparator.reverseOrder());

        System.out.println(Arrays.toString(array));

        assertEquals(LongListX.of(999, 998, 997, 996, 995, 994), LongSequence.of(array).take(6).toListX());
    }
    
}
