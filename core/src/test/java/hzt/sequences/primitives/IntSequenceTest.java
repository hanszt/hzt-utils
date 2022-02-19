package hzt.sequences.primitives;

import hzt.collections.MutableListX;
import hzt.numbers.IntX;
import hzt.sequences.Sequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static hzt.utils.primitive_comparators.IntComparatorX.comparing;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntSequenceTest {

    @Test
    void testSteppedIntRange() {
        var list = MutableListX.<Integer>empty();
        for (int i : IntSequence.until(15).step(4)) {
            It.println(i);
            list.add(i);
        }
        assertEquals(List.of(0, 4, 8, 12), list);
    }

    @Test
    void testDescendingSteppedIntRange() {
        var list = MutableListX.<Integer>empty();
        for (int i : IntSequence.from(100).downTo(20).step(5)) {
            It.println(i);
            list.add(i);
        }
        assertAll(
                () -> assertEquals(17, list.size()),
                () -> assertEquals(100, list.first())
        );
    }

    @Test
    void testDescendingIntRange() {
        final var integers = IntSequence.from(100).downTo(20);
        assertAll(
                () -> assertEquals(81, integers.count()),
                () -> assertEquals(100, integers.first())
        );
    }

    @Test
    void testGetEmptyIntRangeWhenFromValueIsGreaterThanUntilValue() {
        assertEquals(0 , IntSequence.from(100).until(0).count());
    }

    @Test
    void testStats() {
        final var expected = IntStream.range(0, 100).summaryStatistics();

        final var actual = IntSequence.of(0, 100).stats();

        It.println("actual = " + actual);

        assertAll(
                () -> assertEquals(expected.getCount(), actual.getCount()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getMax(), actual.getMax())
        );
    }

    @Test
    void testIntRangeFromIntStream() {
        final var expected = IntStream.range(0, 100).summaryStatistics();

        final var actual = IntSequence.of(IntStream.range(0, 100)).stats();

        It.println("actual = " + actual);

        assertAll(
                () -> assertEquals(expected.getCount(), actual.getCount()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getMax(), actual.getMax())
        );
    }

    @Test
    void intRangeFromIntArray() {
        int[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var expected = IntStream.of(array)
                .mapToLong(It::asLong)
                .filter(l -> l > 3)
                .toArray();


        final var longs = IntSequence.of(array)
                .mapToLong(It::asLong)
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }

    @Test
    void testSortedDescending() {
        int [] array = {1, 4, 5, 3, 6, 7, 4, 8, 5, 9, 4};

        final var sorted = IntSequence.of(array)
                .sortedDescending()
                .toArray();

        System.out.println("Arrays.toString(array) = " + Arrays.toString(sorted));

        assertArrayEquals(new int[]{9, 8, 7, 6, 5, 5, 4, 4, 4, 3, 1}, sorted);
    }

    @Test
    void testSortedThenComparingUnsignedUsingIntComparator() {
        int [] array = {-1, 4, -5, 3, -6, 7, -4, 8, -5, 9, -4};

        final var sorted = IntSequence.of(array)
                .sorted(comparing(Integer::compare)
                        .thenComparing(Integer::compareUnsigned))
                .toArray();

        System.out.println("Arrays.toString(array) = " + Arrays.toString(sorted));

        assertArrayEquals(new int[]{3, 4, 7, 8, 9, -6, -5, -5, -4, -4, -1}, sorted);
    }

    @Test
    void testParallelStreamFromIntSequence() {
        // a parallel stream does not maintain the sorted nature of the sequence
        // when going to sequential stream, then the order is maintained
        final var doubles = IntSequence.generate(0, i -> i + 2)
                .filter(IntX.multipleOf(4))
                .take(10000)
                .sortedDescending()
                .parallelStream()
                .mapToDouble(i -> i * Math.E)
                .peek(It::println)
                .toArray();

        assertEquals(10_000, doubles.length);
    }
}
