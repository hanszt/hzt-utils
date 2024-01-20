package org.hzt.utils.sequences.primitives;

import org.hzt.test.ReplaceCamelCaseBySentence;
import org.hzt.utils.It;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.gatherers.Gatherers;
import org.hzt.utils.iterables.primitives.IntNumerable;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.primitive_comparators.IntComparator;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.statistics.IntStatistics;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.chrono.IsoChronology;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.hzt.utils.It.println;
import static org.hzt.utils.collectors.CollectorsX.intArrayOf;
import static org.hzt.utils.collectors.CollectorsX.longArrayOf;
import static org.hzt.utils.gatherers.primitives.IntGatherers.mapToObjNotNull;
import static org.hzt.utils.gatherers.primitives.IntGatherers.runningStatistics;
import static org.hzt.utils.gatherers.primitives.IntGatherers.windowSliding;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class IntSequenceTest {

    @Test
    void testSteppedIntRange() {
        final var list = IntRange.until(15)
                .step(4)
                .onEach(System.out::println)
                .toList();

        assertEquals(IntList.of(0, 4, 8, 12), list);
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
        final var array = new int[]{1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var result = IntSequence.of(1, 3, 2, 5, 4, 2)
                .filter(IntX::isEven)
                .plus(35, 76, 5)
                .plus(IntList.of(array))
                .toArray();

        println(Arrays.toString(result));

        assertAll(
                () -> assertEquals(18, result.length),
                () -> assertArrayEquals(new int[]{2, 4, 2, 35, 76, 5, 1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2}, result)
        );
    }

    @Test
    void testIntSequenceMinusArray() {
        final var result = IntSequence.of(1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2)
                .minus(2, 76, 5)
                .toArray();

        println(Arrays.toString(result));

        assertAll(
                () -> assertEquals(8, result.length),
                () -> assertArrayEquals(new int[]{1, 3, 4, 4, 6, 4, 3, 4}, result)
        );
    }

    @Test
    void testIntSequenceMinusIntList() {
        final var result = IntSequence.of(1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2)
                .minus(IntList.of(2, 76, 5))
                .toArray();

        println(Arrays.toString(result));

        assertAll(
                () -> assertEquals(8, result.length),
                () -> assertArrayEquals(new int[]{1, 3, 4, 4, 6, 4, 3, 4}, result)
        );
    }

    @Test
    void testIntSequencePlusIterable() {
        final var ints = IntSequence.of(1, 2, 3, 4, 5, 6, 7, 6);

        final var result = IntSequence.of(1, 3, 2, 5, 4, 2)
                .filter(IntX::isEven)
                .plus(35, 76, 5)
                .plus(ints)
                .toArray();

        println(Arrays.toString(result));

        assertAll(
                () -> assertEquals(14, result.length),
                () -> assertArrayEquals(new int[]{2, 4, 2, 35, 76, 5, 1, 2, 3, 4, 5, 6, 7, 6}, result)
        );
    }

    @Test
    void testDescendingSteppedIntRange() {
        final var list = MutableListX.<Integer>empty();
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

        println("actual = " + actual);

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

        println("actual = " + actual);

        assertAll(
                () -> assertEquals(expected.getCount(), actual.getCount()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getMax(), actual.getMax())
        );
    }

    @Test
    void intRangeFromIntArray() {
        final var array = new int[]{1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var expected = IntStream.of(array)
                .asLongStream()
                .filter(l -> l > 3)
                .toArray();


        final var longs = IntSequence.of(array)
                .asLongSequence()
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }

    @Test
    void testSortedDescending() {
        final var array = new int[]{1, 4, 5, 3, 6, 7, 4, 8, 5, 9, 4};

        final var sorted = IntSequence.of(array)
                .sortedDescending()
                .toArray();

        println("Arrays.toString(array) = " + Arrays.toString(sorted));

        assertArrayEquals(new int[]{9, 8, 7, 6, 5, 5, 4, 4, 4, 3, 1}, sorted);
    }

    @Test
    void testSortedThenComparingUnsignedUsingIntComparator() {
        final var array = new int[]{-1, 4, -5, 3, -6, 7, -4, 8, -5, 9, -4};

        final var sorted = IntSequence.of(array)
                .sorted(IntComparator.comparing(It::asInt)
                        .thenComparing(Integer::compareUnsigned))
                .toArray();

        println("Arrays.toString(array) = " + Arrays.toString(sorted));

        assertArrayEquals(new int[]{3, 4, 7, 8, 9, -6, -5, -5, -4, -4, -1}, sorted);
    }

    @Test
    void testParallelStreamFromIntSequence() {
        // a parallel stream does not maintain the sorted nature of the sequence
        // when using a sequential stream, then the order is maintained
        final var doubles = IntSequence.iterate(0, i -> i + 2)
                .filter(IntX.multipleOf(4))
                .take(10_000)
                .sortedDescending()
//                .onEach(e -> It.println(e + Thread.currentThread().getName()))
                .parallelStream()
                .mapToDouble(this::multiplyByPi)
                .toArray();

        assertEquals(10_000, doubles.length);
    }

    private double multiplyByPi(final int i) {
        return i * Math.E;
    }

    @Test
    void testToByteArray() {
        final var index = new AtomicInteger();
        final var LENGTH = 10_000_000;
        final var array = new byte[LENGTH];

        IntSequence.iterate(Byte.MIN_VALUE, i -> (i == Byte.MAX_VALUE) ? Byte.MIN_VALUE : ++i)
                .take(LENGTH)
                .forEachInt(i -> array[index.getAndIncrement()] = (byte) i);

        assertAll(
                () -> assertEquals(LENGTH, array.length),
                () -> assertEquals(Byte.MIN_VALUE, array[0])
        );
    }

    @Test
    void testChunked() {
        final var longs = IntSequence.iterate(0, i -> ++i)
                .take(500)
                .chunked(100)
                .mapToLong(IntList::sum)
                .toArray();

        assertArrayEquals(new long[]{4950, 14950, 24950, 34950, 44950}, longs);
    }

    @Test
    void testDistinctIntSequence() {
        final var distinctArray = IntSequence.of(1, 2, 3, 4, 3, -51, 2, 1, 5, 4, 6, 3, 7, -1, -100, -100, -50)
                .distinct()
                .toArray();

        assertArrayEquals(new int[]{1, 2, 3, 4, -51, 5, 6, 7, -1, -100, -50}, distinctArray);
    }

    @Test
    void testMapIndexed() {
        final var list = IntSequence.iterate(1, i -> i * 2)
                .mapIndexed(Integer::sum)
                .takeWhile(i -> i < 100)
                .toList();

        assertEquals(IntList.of(1, 3, 6, 11, 20, 37, 70), list);
    }

    @Nested
    class TakeSkipWhileInclusiveTests {

        /**
         * @see java.time.chrono.IsoChronology#isLeapYear(long)
         */
        @Test
        void testYear2000IsLeapYearAccordingIso8601Standard() {
            final var last = IntSequence.iterate(1900, year -> year + 1)
                    .filter(IsoChronology.INSTANCE::isLeapYear)
                    .takeWhileInclusive(year -> year % 100 != 0)
                    .last();

            assertEquals(2000, last);
        }

        /**
         * @see java.time.chrono.IsoChronology#isLeapYear(long)
         */
        @Test
        void testYear2000IssLeapYearAccordingIso8601Standard() {
            final var leapYears = IntSequence.iterate(1900, year -> year + 1)
                    .filter(IsoChronology.INSTANCE::isLeapYear);

            final var yearsSkipWhileInclusive = leapYears
                    .skipWhileInclusive(year -> year % 100 != 0)
                    .take(5)
                    .toList();

            final var yearsSkipWhile = leapYears
                    .skipWhile(year -> year % 100 != 0)
                    .take(5)
                    .toList();

            assertAll(
                    () -> assertEquals(IntList.of(2004, 2008, 2012, 2016, 2020), yearsSkipWhileInclusive),
                    () -> assertEquals(IntList.of(2000, 2004, 2008, 2012, 2016), yearsSkipWhile)
            );
        }
    }

    @Test
    void testFlatMapToInt() {
        final var ints = IntSequence.iterate(1, i -> i * 2)
                .take(20)
                .flatMap(i -> IntSequence.of(1, 2, 3).plus(i))
                .toArray();

        println(Arrays.toString(ints));

        assertEquals(80, ints.length);
    }

    @Nested
    class GathererTests {

        @Test
        void testNormalGathererInIntSequenceGatherer() {
            final var longs = IntSequence.iterate(0, i -> i + 1)
                    .gather(Gatherers.windowSliding(3))
                    .take(3)
                    .mapToLong(s -> IntSequence.of(s).sum())
                    .toList();

            assertEquals(LongList.of(3, 6, 9), longs);
        }

        @Test
        void testIntGathererInIntSequenceGatherer() {
            final var longs = IntSequence.iterate(0, i -> i + 1)
                    .gather(windowSliding(3))
                    .take(3)
                    .mapToLong(IntNumerable::sum)
                    .toList();

            assertEquals(LongList.of(3, 6, 9), longs);
        }
    }

    @Test
    void testRunningIntStatistics() {
        final var result = IntSequence.of(5, 6, 8, 4, 12, 15, 16, 4)
                .gather(runningStatistics())
                .teeing(intArrayOf(IntStatistics::getMax), longArrayOf(IntStatistics::getSum));

        final var expectedMaxes = new int[]{5, 6, 8, 8, 12, 15, 16, 16};
        final long[] expectedSums = {5, 11, 19, 23, 35, 50, 66, 70};

        assertAll(
                () -> assertArrayEquals(expectedMaxes, result.first()),
                () -> assertArrayEquals(expectedSums, result.second())
        );
    }

    @Test
    void testMapToObjNotNull() {
        final var courseRepo = Map.of(
                13423, "Math",
                3, "Science",
                5, "History",
                6, "Politics"
        );

        final var courseIds = new int[]{13423, 2, 3, 5, 3432, 32};

        final var result = IntSequence.of(courseIds)
                .gather(mapToObjNotNull(courseRepo::get))
                .toList();

        assertEquals(List.of("Math", "Science", "History"), result);
    }
}
