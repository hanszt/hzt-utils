package hzt.ranges;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntRangeTest {

    @Test
    void testIntRange() {
        final var intRange = IntRange.of(1, 100);

        assertAll(
                () -> assertTrue(intRange.contains(3)),
                () -> assertFalse(intRange.contains(101))
        );
    }

}
