package org.hzt.utils.collections;

import org.hzt.utils.It;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MutableMapXTest {


    @Test
    void testComputeIfPresent() {
        final var map = MutableMapX.of("1", 1, "2", 2);

        final var result = map.computeIfPresent("1", (k, v) -> Integer.parseInt(k) + v);

        map.forEach(It::println);

        assertEquals(2, result);
    }

    @Nested
    class EqualsTests {

        @Test
        void testMutableMapXEquals() {
            final var map1 = MutableMapX.of("This", "is", "a", "test");
            final var map2 = MutableMapX.of("This", "is", "a", "test");

            assertAll(
                    () -> assertEquals(map2, map1),
                    () -> assertEquals(map1, map2)
            );
        }

        @Test
        void testMutableMapXAndMapDoAlsoEqual() {
            final var mapX = MutableMapX.of("This", "is", "a", "test");
            final var map = Map.of("This", "is", "a", "test");

            assertAll(
                    () -> assertEquals(map, mapX),
                    () -> assertEquals(mapX, map)
            );
        }
    }
}
