package org.hzt.utils.numbers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberXTest {

    @Test
    void testToInt() {
        final var integer = DoubleX.of(Math.PI).toInt();
        assertEquals(3, integer);
    }

}
