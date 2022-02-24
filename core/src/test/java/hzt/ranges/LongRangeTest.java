package hzt.ranges;

import hzt.sequences.primitives.LongSequence;
import hzt.statistics.LongStatistics;
import org.junit.jupiter.api.Test;

import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LongRangeTest {

    @Test
    void longRangeStatsGreaterThanIntegerMaxFasterThanSequentialStream() {
        final LongStatistics stats = LongRange.of(0, Integer.MAX_VALUE + 100_000L, 1000)
                .map(l -> l + 2 * l)
                .stats();

        assertAll(
                () -> assertEquals(6442749000L, stats.getMax()),
                () -> assertEquals(3.2213745E9, stats.getAverage())
        );
    }

    @Test
    void longStreamParallelStatsGreaterThanIntegerMax() {
        final LongSummaryStatistics stats = LongStream.range(0, Integer.MAX_VALUE + 100_000L)
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

        final long[] expected = LongStream.of(array)
                .filter(l -> l > 3)
                .toArray();

        final long[] longs = LongSequence.of(array)
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }

    @Test
    void longRangeFromUntil() {
        final long[] longs = LongRange.from(1).until(7).toArray();

        assertArrayEquals(new long[] {1, 2, 3, 4, 5, 6}, longs);
    }

}
