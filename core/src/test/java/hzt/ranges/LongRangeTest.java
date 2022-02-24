package hzt.ranges;

import hzt.numbers.LongX;
import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LongRangeTest {

    @Test
    void longRangeStatsGreaterThanIntegerMaxFasterThanSequentialStream() {
        final var stats = LongRange.of(0, Integer.MAX_VALUE + 100_000L, 1000)
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
    void testRangeWithInterval() {
        assertArrayEquals(
                LongStream.range(5, 10).filter(LongX::isOdd).toArray(),
                LongRange.of(5, 10, 2).toArray());
    }

    @Test
    void longRangeFromUntil() {
        final var longs = LongRange.from(1).until(7).toArray();

        assertArrayEquals(new long[] {1, 2, 3, 4, 5, 6}, longs);
    }

}
