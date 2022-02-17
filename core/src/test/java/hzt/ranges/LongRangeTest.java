package hzt.ranges;

import hzt.statistics.LongStatistics;
import org.junit.jupiter.api.Test;

import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

class LongRangeTest {

    @Test
    void longRangeStatsGreaterThanIntegerMax() {
        final LongStatistics stats = LongRange.of(0, Integer.MAX_VALUE * 10L, 1_000L).stats();

        assertAll(
                () -> assertEquals(21474836000L, stats.getMax()),
                () -> assertEquals(1.0737418E10, stats.getAverage()));
    }

    @Test
    void longStreamRangeStatsGreaterThanIntegerMax() {
        final LongSummaryStatistics stats = LongStream.range(0, Integer.MAX_VALUE + 100_000L)
                .parallel()
                .summaryStatistics();

        assertAll(
                () -> assertEquals(Integer.MAX_VALUE + 99_999L, stats.getMax()),
                () -> assertEquals(1.073791823E9, stats.getAverage())
        );
    }

    @Test
    void longRangeFromLongArray() {
        long[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var expected = LongStream.of(array)
                .filter(l -> l > 3)
                .toArray();

        final var longs = LongRange.of(array)
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }

    @Test
    void longRangeForEachLong() {
        long[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        LongRange.of(array)
                .filter(l -> l > 3)
                .onEach(It::println)
                .forEachLong(l -> assertTrue(l > 3));
    }

}
