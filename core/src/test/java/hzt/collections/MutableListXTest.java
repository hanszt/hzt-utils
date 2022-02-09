package hzt.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutableListXTest {

    @Test
    void testMutableListRemoveLast() {
        final var integers = MutableListX.of(1, 2, 3, 4, 5);

        final var removed = integers.removeLast();

        assertAll(
                () -> assertTrue(removed),
                () -> assertEquals(List.of(1,2,3,4), integers)
        );
    }
}
