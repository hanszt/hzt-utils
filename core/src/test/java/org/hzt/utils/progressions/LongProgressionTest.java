package org.hzt.utils.progressions;

import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

class LongProgressionTest {

    @Test
    void negativeLongProgressionStepped() {
        final var end = 10_000_000;
        final var expected = LongStream.iterate(0, i -> --i).limit(end).filter(l -> l % 4 == 0).toArray();

        final var longs = LongProgression.downTo(-end).step(4).toArray();

        assertArrayEquals(expected, longs);
    }
}
