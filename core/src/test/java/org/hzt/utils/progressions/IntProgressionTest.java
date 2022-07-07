package org.hzt.utils.progressions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntProgressionTest {

    @Test
    void testDownTo() {
        final var integers = IntProgression.downTo(-100_000);

        assertAll(
                () -> assertEquals(0, integers.first()),
                () -> assertEquals(-100_000, integers.last()),
                () -> assertEquals(100_001, integers.count())
        );
    }

}
