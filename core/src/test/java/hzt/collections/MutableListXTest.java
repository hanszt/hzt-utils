package hzt.collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableListXTest {

    @Test
    void testMutableListRemoveFirst() {
        final MutableListX<Integer> integers = MutableListX.of(1, 2, 3, 4, 5);

        final boolean removed = integers.removeFirst();
        final boolean removed2 = integers.removeFirst();

        assertAll(
                () -> assertTrue(removed),
                () -> assertTrue(removed2),
                () -> assertEquals(Arrays.asList(3,4, 5), integers)
        );
    }

    @Test
    void testMutableListRemoveLast() {
        final MutableListX<Integer> integers = MutableListX.of(1, 2, 3, 4, 5);

        final boolean removed = integers.removeLast();

        assertAll(
                () -> assertTrue(removed),
                () -> assertEquals(Arrays.asList(1, 2, 3, 4), integers)
        );
    }
}
