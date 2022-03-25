package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.primitives.IntListX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntIterableTest {

    @Test
    void testTripWire() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        final IntListX integers = IntListX.of(1, 2, 3, 4, 9, 5, 6, 6);
        integers.forEach(System.out::println);

        final String property = System.getProperty("org.openjdk.java.util.stream.tripwire");
        assertEquals("true", property);

        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
    }

}
