package org.hzt.utils.iterables;

import org.hzt.utils.iterables.primitives.LongIterable;
import org.hzt.utils.sequences.primitives.LongSequence;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class PrimitiveIterableTest {

    @Test
    void longStreamFromPrimitiveIterable() {
        LongIterable longSequence = LongSequence.of(1, 2, 3, 4, 5).map(l -> l * 3);

        final var count = LongSequence.of(longSequence).count();

        It.println("count = " + count);

        final var longStream = StreamSupport.longStream(longSequence.spliterator(), false);

        final var longs = longStream.toArray();

        It.println(Arrays.toString(longs));

        assertArrayEquals(new long[]{3, 6, 9, 12, 15}, longs);
    }
}
