package hzt.ranges;

import hzt.statistics.LongStatistics;
import org.junit.jupiter.api.Test;

import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

}
