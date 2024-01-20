package org.hzt.utils.iterables;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IterableReductionsTest {

    @Test
    void testAny() {
        final var strings = List.of("This", "is", "a", "test");

        final var anyContainsS = IterableReductions.any(strings, s -> s.contains("s"));

        assertTrue(anyContainsS);
    }

}
