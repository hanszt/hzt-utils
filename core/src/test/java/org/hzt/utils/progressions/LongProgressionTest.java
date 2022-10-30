package org.hzt.utils.progressions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class LongProgressionTest {

    @Test
    @Timeout(value = 1_000, unit = TimeUnit.MILLISECONDS)
    void negativeLongProgressionStepped() {
        final int end = 10_000_000;

        final long[] expected = LongStream.iterate(0, i -> --i)
                .limit(end)
                .filter(l -> l % 4 == 0)
                .toArray();

        final long[] longs = LongProgression.downTo(-end).step(4).toArray();

        assertArrayEquals(expected, longs);
    }
}
