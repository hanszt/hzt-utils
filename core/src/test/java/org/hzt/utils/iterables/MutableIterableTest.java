package org.hzt.utils.iterables;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableIterableTest {

    @Test
    void removeIf() {
        final List<String> strings = new ArrayList<>(Arrays.asList("This", "is", "a", "test"));

        final MutableIterable<String> mutableIterable = MutableIterable.of(strings);

        final boolean removed = mutableIterable.removeIf(s -> s.length() == 2);

        assertAll(
                () -> assertTrue(removed),
                () -> assertIterableEquals(Arrays.asList("This", "a", "test"), mutableIterable)
        );
    }

}