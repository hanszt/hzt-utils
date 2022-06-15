package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.IntListX;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.ranges.IntRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class IntSequenceTest {

    @Test
    void testSteppedIntRange() {
        var list = IntRange.until(15)
                .step(4)
                .onEach(System.out::println)
                .toListX();

        assertEquals(IntListX.of(0, 4, 8, 12), list);
    }

    @Test
    @DisplayName("Test sum stream yields wrong result for large int value sums")
    void testSumStreamYieldsWrongResultForLargeIntValueSums() {
        final var endExclusive = 100_000;

        final var sumIntStream = IntStream.range(0, endExclusive).sum();
        final var sumIntRange = IntRange.of(0, endExclusive).sum();
        final var sumIntStreamUsingCollector = IntStream.range(0, endExclusive)
                .summaryStatistics()
                .getSum();

        assertAll(
                () -> assertEquals(sumIntRange, sumIntStreamUsingCollector),
                () -> assertTrue(sumIntStream < sumIntRange)
        );
    }

    @Test
    void testIntSequencePlusArray() {
        int[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final int[] result = IntSequence.of(1, 3, 2, 5, 4, 2)
                .filter(IntX::isEven)
                .plus(35, 76, 5)
                .plus(IntListX.of(array))
                .toArray();

        It.println(Arrays.toString(result));

        assertAll(
                () -> assertEquals(18, result.length),
                () -> assertArrayEquals(new int[]{2, 4, 2, 35, 76, 5, 1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2}, result)
        );
    }

    @Test
    void testIntSequencePlusIterable() {
        final IntSequence ints = IntSequence.of(1, 2, 3, 4, 5, 6, 7, 6);

        final int[] result = IntSequence.of(1, 3, 2, 5, 4, 2)
                .filter(IntX::isEven)
                .plus(35, 76, 5)
                .plus(ints)
                .toArray();

        It.println(Arrays.toString(result));

        assertAll(
                () -> assertEquals(14, result.length),
                () -> assertArrayEquals(new int[]{2, 4, 2, 35, 76, 5, 1, 2, 3, 4, 5, 6, 7, 6}, result)
        );
    }

    @Test
    void testDescendingSteppedIntRange() {
        var list = MutableListX.<Integer>empty();
        IntRange.from(100).downTo(20).step(5)
                .onEach(System.out::println)
                .forEachInt(list::add);

        assertAll(
                () -> assertEquals(17, list.size()),
                () -> assertEquals(100, list.first())
        );
    }

    @Test
    void testDescendingIntRange() {
        final var integers = IntRange.from(100).downTo(20);
        assertAll(
                () -> assertEquals(81, integers.count()),
                () -> assertEquals(100, integers.first())
        );
    }

    @Test
    void testGetEmptyIntRangeWhenFromValueIsGreaterThanUntilValue() {
        assertEquals(0, IntRange.from(100).until(0).count());
    }

    @Test
    void testStats() {
        final var expected = IntStream.range(0, 100).summaryStatistics();

        final var actual = IntRange.of(0, 100).stats();

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
        int[] array = {1, 4, 5, 3, 6, 7, 4, 8, 5, 9, 4};

        final var sorted = IntSequence.of(array)
                .sortedDescending()
                .toArray();

        It.println("Arrays.toString(array) = " + Arrays.toString(sorted));

        assertArrayEquals(new int[]{9, 8, 7, 6, 5, 5, 4, 4, 4, 3, 1}, sorted);
    }

    @Test
    void testSortedThenComparingUnsignedUsingIntComparator() {
        int[] array = {-1, 4, -5, 3, -6, 7, -4, 8, -5, 9, -4};

        final var sorted = IntSequence.of(array)
                .sorted(IntComparator.comparing(It::asInt)
                        .thenComparing(Integer::compareUnsigned))
                .toArray();

        It.println("Arrays.toString(array) = " + Arrays.toString(sorted));

        assertArrayEquals(new int[]{3, 4, 7, 8, 9, -6, -5, -5, -4, -4, -1}, sorted);
    }

    @Test
    void testParallelStreamFromIntSequence() {
        // a parallel stream does not maintain the sorted nature of the sequence
        // when using a sequential stream, then the order is maintained
        final var doubles = IntSequence.generate(0, i -> i + 2)
                .filter(IntX.multipleOf(4))
                .take(10_000)
                .sortedDescending()
//                .onEach(e -> It.println(e + Thread.currentThread().getName()))
                .parallelStream()
                .mapToDouble(this::multiplyByPi)
                .toArray();

        assertEquals(10_000, doubles.length);
    }

    private double multiplyByPi(int i) {
//        It.println(Thread.currentThread().getName());
        return i * Math.E;
    }

    @Test
    void testToByteArray() {
        AtomicInteger index = new AtomicInteger();
        final var LENGTH = 10_000_000;
        byte[] array = new byte[LENGTH];

        IntSequence.generate(Byte.MIN_VALUE, i -> (i == Byte.MAX_VALUE) ? Byte.MIN_VALUE : ++i)
                .take(LENGTH)
                .forEachInt(i -> array[index.getAndIncrement()] = (byte) i);

        assertAll(
                () -> assertEquals(LENGTH, array.length),
                () -> assertEquals(Byte.MIN_VALUE, array[0])
        );
    }

    @Test
    void testChunked() {
        final long[] longs = IntSequence.generate(0, i -> ++i)
                .take(500)
                .chunked(100)
                .mapToLong(IntListX::sum)
                .toArray();

        assertArrayEquals(new long[]{4950, 14950, 24950, 34950, 44950}, longs);
    }

    @Test
    void testDistinctIntSequence() {
        final var distinctArray = IntSequence.of(1, 2, 3, 4, 3, -51, 2, 1, 5, 4, 6, 3, 7, -1, -100, -100, -50)
                .distinct()
                .toArray();

        assertArrayEquals(new int[] {1, 2, 3, 4, -51, 5, 6, 7, -1, -100, -50}, distinctArray);
    }
}
