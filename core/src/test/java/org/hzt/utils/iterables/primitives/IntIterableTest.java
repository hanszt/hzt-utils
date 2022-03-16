package org.hzt.utils.iterables.primitives;

import org.hzt.utils.collections.primitives.IntListX;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntIterableTest {

    @BeforeAll
    static void setup() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");
    }

    @Test
    void testTripWire() {
        final var integers = IntListX.of(1, 2, 3, 4, 9, 5, 6, 6);
        integers.forEach(System.out::println);

        final var property = System.getProperty("org.openjdk.java.util.stream.tripwire");
        assertEquals("true", property);
    }

}
