package hzt.collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableListTest {

    @Test
    void testMutableListRemoveFirst() {
        final var integers = MutableList.of(1, 2, 3, 4, 5);

        final var removed = integers.removeFirst();
        final var removed2 = integers.removeFirst();

        assertAll(
                () -> assertTrue(removed),
                () -> assertTrue(removed2),
                () -> assertEquals(List.of(3,4, 5), integers)
        );
    }

    @Test
    void testMutableListRemoveLast() {
        final MutableListX<Integer> integers = MutableList.of(1, 2, 3, 4, 5);

        final boolean removed = integers.removeLast();

        assertAll(
                () -> assertTrue(removed),
                () -> assertEquals(Arrays.asList(1, 2, 3, 4), integers)
        );
    }
}
