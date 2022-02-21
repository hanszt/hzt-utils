package hzt.sequences.primitives;

import hzt.collections.MutableListX;
import hzt.collections.primitives.LongListX;
import hzt.sequences.Sequence;
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
    void longRangeStatsGreaterThanIntegerMaxFasterThanSequentialStream() {
        final var stats = LongSequence.of(0, Integer.MAX_VALUE + 100_000L, 1000)
                .map(l -> l + 2 * l)
                .stats();

        assertAll(
                () -> assertEquals(6442749000L, stats.getMax()),
                () -> assertEquals(3.2213745E9, stats.getAverage())
        );
    }

    @Test
    void longStreamParallelStatsGreaterThanIntegerMax() {
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

    @Test
    void testWindowedLongSequence() {
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var windowed = LongSequence.of(array)
                .windowed(5)
                .map(LongListX::toArray)
                .toTypedArray(long[][]::new);

       Sequence.of(windowed).map(Arrays::toString).forEach(It::println);

        assertEquals(3, windowed.length);
    }

    @Test
    void testWindowedLongSequenceWindowReduced() {
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var sums = LongSequence.of(array)
                .windowed(3, LongListX::sum)
                .toArray();

        LongSequence.of(sums).forEachLong(It::println);

        assertArrayEquals(new long[] {6, 9, 12, 15, 18}, sums);
    }

    @Test
    void testPartialWindowedLongSequence() {
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var windows = LongSequence.of(array)
                .windowed(3, 2, true)
                .map(LongListX::toArray)
                .toTypedArray(long[][]::new);

        Sequence.of(windows).map(Arrays::toString).forEach(It::println);

        assertEquals(4, windows.length);
    }

    @Test
    void testPartialWindowedLongSequenceWindowReduced() {
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var sums = LongSequence.of(array)
                .windowed(3, 2, true, LongListX::sum)
                .toArray();

        LongSequence.of(sums).forEachLong(It::println);

        assertArrayEquals(new long[] {6, 12, 18, 7}, sums);
    }

    @Test
    void testChunkedTo3DArray() {
        final var cube = LongSequence.generate(0, l -> l + 2)
                .take(1_000)
                .chunked(10)
                .chunked(10)
                .map(chunk -> chunk
                        .map(LongListX::toArray)
                        .toTypedArray(long[][]::new))
                .toTypedArray(long[][][]::new);

        final String cubeAsString = Sequence.of(cube)
                .map(plane -> Sequence.of(plane)
                        .map(Arrays::toString))
                .map(s -> s.joinToString(System.lineSeparator()))
                        .joinToString(String.format("%n%n"));

        System.out.println(cubeAsString);

        assertEquals(10, cube.length);
    }

    @Test
    void testWindowedLargeLongSequence() {
        final var sums = LongSequence.generate(0, l -> ++l)
                .take(1_000_000)
                .windowed(1_000, 50, LongListX::sum)
                .toArray();

        final var sums2 = Sequence.generate(0, l -> ++l)
                .take(1_000_000)
                .windowed(1_000, 50, s -> s.sumOfLongs(It::asLong))
                .mapToLong(It::asLong)
                .toArray();

        assertAll(
                () -> assertEquals(19981, sums.length),
                () -> assertArrayEquals(sums, sums2)
        );

    }

}
