package hzt.ranges;

import hzt.collections.primitives.IntListX;
import hzt.collections.primitives.IntMutableListX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void testIterateIntRange() {
        final var range = IntRange.of(2, 10);
        IntMutableListX list = IntMutableListX.empty();
        for (int i : range) {
            list.add(i);
        }
        for (int i : range) {
            list.add(i);
        }
        assertEquals(IntListX.of(2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5, 6, 7, 8, 9), list);
    }

}
