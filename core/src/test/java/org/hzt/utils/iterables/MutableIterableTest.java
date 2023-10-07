package org.hzt.utils.iterables;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableIterableTest {

    @Test
    void removeIf() {
        final var strings = new ArrayList<>(List.of("This", "is", "a", "test"));

        final var mutableIterable = MutableIterable.of(strings);

        final var removed = mutableIterable.removeIf(s -> s.length() == 2);

        assertAll(
                () -> assertTrue(removed),
                () -> assertIterableEquals(List.of("This", "a", "test"), mutableIterable)
        );
    }

}