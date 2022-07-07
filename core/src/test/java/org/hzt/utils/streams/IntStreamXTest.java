package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntStreamXTest {

    @Test
    void testIntStreamX() {
        final var stats = IntSequence.generate(0, i -> ++i)
                .skip(5)
                .take(100_000)
                .mapToLong(It::asLong)
                .stats();

        final var statistics = IntStreamX.generate(0, i -> ++i)
                .skip(5)
                .limit(100_000)
                .mapToLong(It::asLong)
                .statistics();

        assertAll(
                () -> assertEquals(100_000L, statistics.getCount()),
                () -> assertEquals(statistics.getCount(), stats.getCount()),
                () -> assertEquals(statistics.getStandardDeviation(), stats.getStandardDeviation()),
                () -> assertEquals(statistics.getMax(), stats.getMax())
        );
    }

}
