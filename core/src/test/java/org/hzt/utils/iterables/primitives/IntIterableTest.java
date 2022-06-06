package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.primitives.IntListX;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntIterableTest {

    @Test
    void testTripWire() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        final var integers = IntListX.of(1, 2, 3, 4, 9, 5, 6, 6);
        integers.iterator().forEachRemaining((Consumer<? super Integer>) System.out::println);

        final var property = System.getProperty("org.openjdk.java.util.stream.tripwire");
        assertEquals("true", property);

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
    }

}
