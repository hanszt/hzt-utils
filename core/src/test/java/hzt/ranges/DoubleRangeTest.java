package hzt.ranges;

import hzt.statistics.DoubleStatistics;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleRangeTest {

    @Test
    void testRange() {
        final DoubleStatistics stats = DoubleRange.closed(0, 100, .01).stats();

        assertAll(
                () -> assertEquals(10_000, stats.getCount()),
                () -> assertEquals("49.995", String.format(Locale.ENGLISH, "%.3f", stats.getAverage())),
                () -> assertEquals("99.99", String.format(Locale.ENGLISH, "%.2f", stats.getMax()))
        );
    }

    @Test
    void doubleRangeFromDoubleArray() {
        double[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final long[] expected = DoubleStream.of(array)
                .mapToLong(d -> (long) d)
                .filter(l -> l > 3)
                .toArray();


        final long[] longs = DoubleRange.of(array)
                .asLongRange(Double::longValue)
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }
}
