package org.hzt.utils.ranges;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.DoubleSequence;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DoubleRangeTest {

    @Test
    void testRange() {
        final var stats = DoubleRange.closed(0, 100, .01).stats();

        assertAll(
                () -> assertEquals(10_000, stats.getCount()),
                () -> assertEquals("49.995", String.format(Locale.ENGLISH, "%.3f", stats.getAverage())),
                () -> assertEquals("99.99", String.format(Locale.ENGLISH, "%.2f", stats.getMax()))
        );
    }

    @Test
    void doubleRangeFromDoubleArray() {
        final double[] array = {1, 2, 3, 4, 5, 4, 6, 4, 3, 4, 2, 2};

        final var expected = DoubleStream.of(array)
                .mapToLong(d -> (long) d)
                .filter(l -> l > 3)
                .toArray();


        final var longs = DoubleSequence.of(array)
                .mapToLong(It::doubleAsLong)
                .filter(l -> l > 3)
                .toArray();

        assertAll(
                () -> assertArrayEquals(new long[]{4, 5, 4, 6, 4, 4}, longs),
                () -> assertArrayEquals(expected, longs)
        );
    }

    @Test
    void testClosedDoubleRange() {
        assertEquals(10.0, DoubleRange.closed(10, 10).boxed().single());
    }
}
