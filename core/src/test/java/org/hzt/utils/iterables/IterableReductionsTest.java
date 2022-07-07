package org.hzt.utils.iterables;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IterableReductionsTest {

    @Test
    void testAny() {
        final List<String> strings = Arrays.asList("This", "is", "a", "test");

        final boolean anyContainsS = IterableReductions.any(strings, s -> s.contains("s"));

        assertTrue(anyContainsS);
    }

}
