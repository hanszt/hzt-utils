package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableMapXTest {


    @Test
    void testComputeIfPresent() {
        final var map = MutableMapX.of("1", 1, "2", 2);

        final var result = map.computeIfPresent("1", (k, v) -> Integer.parseInt(k) + v);

        map.forEach(It::println);

        assertEquals(2, result);
    }
}
