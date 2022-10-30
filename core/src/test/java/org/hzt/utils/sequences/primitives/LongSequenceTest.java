package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.primitives.LongList;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.LongRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.test.Generator;
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
    void testLongSequencePlusArray() {
        long[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, Long.MAX_VALUE};

        final var result = LongSequence.of(1, 3, 2, 5, 4, 2)
                .filter(LongX::isEven)
                .plus(35, 76, 5)
                .plus(LongList.of(array))
                .toArray();

        It.println(Arrays.toString(result));

        assertAll(
                () -> assertEquals(18, result.length),
                () -> assertArrayEquals(new long[]{2, 4, 2, 35, 76, 5, 1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, Long.MAX_VALUE}, result)
        );
    }

    @Test
    void testLongSequenceMinusArray() {
        final var result = LongSequence.of(Long.MAX_VALUE, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2)
                .minus(2, 76, 5)
                .toArray();

        It.println(Arrays.toString(result));

        assertAll(
                () -> assertEquals(8, result.length),
                () -> assertArrayEquals(new long[]{Long.MAX_VALUE, 3, 4, 4, 6, 4, 3, 4}, result)
        );
    }

    @Test
    void positiveLongRangeStepped() {
        final var end = 10_000_000;
        final var expected = LongStream.range(0, end).filter(l -> l % 4 == 0).toArray();

        final var longs = LongRange.until(end).step(4).toArray();

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

        It.println("Arrays.toString(array) = " + Arrays.toString(sorted));

        assertArrayEquals(new long[]{9, 8, 7, 6, 5, 5, 4, 4, 4, 3, 1}, sorted);
    }

    @Test
    void testZipLongSequenceWithLongArray() {
        long[] array = {1, 2, 3, 4, 5, 6};

        final var zipped = LongSequence.of(array)
                .zip(Long::sum, 1, 2, 3, 4)
                .toArray();

        It.println("Arrays.toString(array) = " + Arrays.toString(zipped));

        assertArrayEquals(new long[]{2, 4, 6, 8}, zipped);
    }

    @Test
    void testZipLongSequenceWithIterableOfLong() {
        List<Long> list = MutableListX.of(1L, 2L, 3L, 4L, 5L, 6L);
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var zipped = LongSequence.of(array)
                .zip(Long::sum, list)
                .toArray();

        It.println("Arrays.toString(array) = " + Arrays.toString(zipped));

        assertArrayEquals(new long[]{2, 4, 6, 8, 10, 12}, zipped);
    }

    @Test
    void testWindowedLongSequence() {
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var windowed = LongSequence.of(array)
                .windowed(5)
                .map(LongList::toArray)
                .toTypedArray(long[][]::new);

       Sequence.of(windowed).map(Arrays::toString).forEach(It::println);

        assertEquals(3, windowed.length);
    }

    @Test
    void testWindowedLongSequenceWindowReduced() {
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var sums = LongSequence.of(array)
                .windowed(3, LongList::sum)
                .toArray();

        LongSequence.of(sums).forEachLong(It::println);

        assertArrayEquals(new long[] {6, 9, 12, 15, 18}, sums);
    }

    @Test
    void testPartialWindowedLongSequence() {
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var windows = LongSequence.of(array)
                .windowed(3, 2, true)
                .map(LongList::toArray)
                .toTypedArray(long[][]::new);

        Sequence.of(windows).map(Arrays::toString).forEach(It::println);

        assertEquals(4, windows.length);
    }

    @Test
    void testPartialWindowedLongSequenceWindowReduced() {
        long[] array = {1, 2, 3, 4, 5, 6, 7};

        final var sums = LongSequence.of(array)
                .windowed(3, 2, true, LongList::sum)
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
                        .map(LongList::toArray)
                        .toTypedArray(long[][]::new))
                .toTypedArray(long[][][]::new);

        final var cubeAsString = Sequence.of(cube)
                .map(plane -> Sequence.of(plane)
                        .map(Arrays::toString))
                .map(s -> s.joinToString(System.lineSeparator()))
                        .joinToString(String.format("%n%n"));

        It.println(cubeAsString);

        assertEquals(10, cube.length);
    }

    @Test
    void testWindowedLargeLongSequence() {
        final var sums = LongSequence.generate(0, l -> ++l)
                .take(1_000_000)
                .windowed(1_000, 50, LongList::sum)
                .toList();

        assertAll(
                () -> assertEquals(19981, sums.size()),
                () -> assertEquals(999499500L, sums.last())
        );

    }

    @Test
    void testSkipWhile() {
        final var longs = LongSequence.generate(0L, l -> ++l)
                .map(Generator::fib)
                .skipWhile(l -> l < 3)
                .takeWhileInclusive(l -> l < 55)
                .toList();

        longs.forEachLong(It::println);

        assertEquals(LongList.of(3, 5, 8, 13, 21, 34, 55), longs);
    }

    @Test
    void testSkipWhileInclusive() {
        final var longs = LongSequence.generate(0L, l -> ++l)
                .map(Generator::fib)
                .skipWhileInclusive(l -> l < 3)
                .takeWhileInclusive(l -> l < 55)
                .toList();

        longs.forEachLong(It::println);

        assertEquals(LongList.of(5, 8, 13, 21, 34, 55), longs);
    }

}
