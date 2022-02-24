package hzt.collections.primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleListXTest {

    @Test
    void testContainsAll() {
        final DoubleListX doubles = DoubleListX.of(1, 2, 3, 4, 5, 4, 6, 7, 4, 5);
        assertTrue(doubles.containsAll(1, 4, 2, 6, 5));
    }
}
