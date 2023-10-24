package org.hzt.utils.streams;

import org.hzt.utils.It;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hzt.utils.streams.StreamExtensions.intWindowed;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntStreamXTest {

    @Test
    void testIntStreamX() {
        final var stats = IntSequence.iterate(0, i -> ++i)
                .skip(5)
                .take(100_000)
                .mapToLong(It::asLong)
                .stats();

        final var statistics = IntStreamX.iterate(0, i -> ++i)
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

    @Nested
    class IntStreamExtensionTests {

        @Test
        void testWindowedExtension() {
            final var windows = IntStreamX.iterate(0, i -> i + 1)
                    .then(intWindowed(4))
                    .limit(10)
                    .toList();

            final var expected = IntSequence.iterate(0, i -> i + 1)
                    .windowed(4)
                    .take(10)
                    .toList();

            assertEquals(expected, windows);
        }
    }

}
