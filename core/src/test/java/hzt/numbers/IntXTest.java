package hzt.numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntXTest {

    @Test
    void testToDouble() {
        final DoubleX doubleX = IntX.of(10).toDoubleX();

        assertEquals(DoubleX.of(10), doubleX);
    }

}
