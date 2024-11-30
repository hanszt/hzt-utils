package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveIterableTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrimitiveIterableTest.class);

    @Test
    void longStreamFromPrimitiveIterable() {
        final PrimitiveIterable.OfLong longSequence = LongSequence.of(1, 2, 3, 4, 5).map(l -> l * 3);

        final var count = LongSequence.of(longSequence).count();

        LOGGER.atDebug().setMessage(() -> "count = " + count).log();

        final var longStream = StreamSupport.longStream(longSequence.spliterator(), false);

        final var longs = longStream.toArray();

        LOGGER.atDebug().setMessage(() -> Arrays.toString(longs)).log();

        assertArrayEquals(new long[]{3, 6, 9, 12, 15}, longs);
    }

    @Test
    void testTripWire() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        final var integers = IntList.of(1, 2, 3, 4, 9, 5, 6, 6);
        integers.iterator().forEachRemaining((Consumer<? super Integer>) it -> LOGGER.trace("{}", it));

        final var property = System.getProperty("org.openjdk.java.util.stream.tripwire");
        assertEquals("true", property);

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
    }
}
