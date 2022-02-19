package hzt.sequences.primitives;

import hzt.collections.MutableListX;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongSequenceTest {

    @Test
    void longRangeStatsGreaterThanIntegerMax() {
        final var stats = LongSequence.of(0, Integer.MAX_VALUE + 100_000L)
                .filter(l -> l % 1_000 == 0)
                .map(l -> l + 2 * l)
                .stats();

        assertAll(
                () -> assertEquals(6442749000L, stats.getMax()),
                () -> assertEquals(3.2213745E9, stats.getAverage())
        );
    }

    @Test
    void longStreamRangeStatsGreaterThanIntegerMax() {
        final var stats = LongStream.range(0, Integer.MAX_VALUE + 100_000L)
                .parallel()
                .filter(l -> l % 1_000 == 0)
                .map(l -> l + 2 * l)
                .summaryStatistics();


        assertAll(
                () -> assertEquals(6442749000L, stats.getMax()),
                () -> assertEquals(3.2213745E9, stats.getAverage())
        );
    }

    @Test
    void longRangeFromLongArray() {
        long[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var expected = LongStream.of(array)
                .filter(l -> l > 3)
                .toArray();
        
        final var longs = LongSequence.of(array)
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }

    @Test
    void positiveLongRangeStepped() {
        final var end = 10_000_000;
        final var expected = LongStream.range(0, end).filter(l -> l % 4 == 0).toArray();

        final var longs = LongSequence.until(end).step(4).toArray();

        assertArrayEquals(expected, longs);
    }

    @Test
    void negativeLongRangeStepped() {
        final var end = 10_000_000;
        final var expected = LongStream.iterate(0, i -> --i).limit(end).filter(l -> l % 4 == 0).toArray();

        final var longs = LongSequence.until(-end).step(4).toArray();

        assertArrayEquals(expected, longs);
    }

    @Test
    void longRangeForEachLong() {
        long[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        LongSequence.of(array)
                .filter(l -> l > 3)
                .onEach(It::println)
                .forEachLong(l -> assertTrue(l > 3));
    }

    @Test
    void testLongRangeMapMulti() {
        long[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var expected = LongStream.of(array)
                .flatMap((v) -> LongStream.of(array).map(l -> v + l))
                .toArray();

        final var result = LongSequence.of(array)
                .mapMulti((v, c) -> LongSequence.of(array).forEachLong(l -> c.accept(v + l)))
                .toArray();

        It.println("Arrays.toString(result) = " + Arrays.toString(result));

        assertAll(
                () -> assertEquals(array.length * array.length, result.length),
                () -> assertArrayEquals(expected, result)
        );
    }

    @Test
    void longSequenceFilterNot() {
        final var longs = LongSequence.generate(1, i -> i + 2)
                .take(10_000_000)
                .filterNot(l -> l % 5 == 0 && l % 7 == 0)
                .toArray();

        assertEquals(9714286, longs.length);
    }

    @Test
    void testSortedDescending() {
        long[] array = {1, 4, 5, 3, 6, 7, 4, 8, 5, 9, 4};

        final var sorted = LongSequence.of(array)
                .sortedDescending()
                .toArray();

        System.out.println("Arrays.toString(array) = " + Arrays.toString(sorted));

        assertArrayEquals(new long[]{9, 8, 7, 6, 5, 5, 4, 4, 4, 3, 1}, sorted);
    }

    @Test
    void testZipLongSequenceWithLongArray() {
        long[] array = {1, 2, 3, 4, 5, 6};

        final var zipped = LongSequence.of(array)
                .zip(Long::sum, 1, 2, 3, 4)
                .toArray();

        System.out.println("Arrays.toString(array) = " + Arrays.toString(zipped));

        assertArrayEquals(new long[]{2, 4, 6, 8}, zipped);
    }

    @Test
    void testZipLongSequenceWithIterableOfLong() {
        List<Long> list = MutableListX.of(1L, 2L, 3L, 4L, 5L, 6L);
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var zipped = LongSequence.of(array)
                .zip(Long::sum, list)
                .toArray();

        System.out.println("Arrays.toString(array) = " + Arrays.toString(zipped));

        assertArrayEquals(new long[]{2, 4, 6, 8, 10, 12}, zipped);
    }

}
