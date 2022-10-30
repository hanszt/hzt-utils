package org.hzt.utils.iterables.primitives;

import org.hzt.utils.It;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveIterableTest {

    @Test
    void longStreamFromPrimitiveIterable() {
        PrimitiveIterable.OfLong longSequence = LongSequence.of(1, 2, 3, 4, 5).map(l -> l * 3);

        final long count = LongSequence.of(longSequence).count();

        It.println("count = " + count);

        final LongStream longStream = StreamSupport.longStream(longSequence.spliterator(), false);

        final long[] longs = longStream.toArray();

        It.println(Arrays.toString(longs));

        assertArrayEquals(new long[]{3, 6, 9, 12, 15}, longs);
    }

    @Test
    void testTripWire() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        final IntList integers = IntList.of(1, 2, 3, 4, 9, 5, 6, 6);
        integers.iterator().forEachRemaining((Consumer<? super Integer>) System.out::println);

        final String property = System.getProperty("org.openjdk.java.util.stream.tripwire");
        assertEquals("true", property);

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
    }
}
