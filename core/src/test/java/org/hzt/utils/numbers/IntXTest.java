package org.hzt.utils.numbers;

import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IntXTest {

    @Test
    void testToDouble() {
        final DoubleX doubleX = IntX.of(10).toDoubleX()
                .toStringX().toDoubleX();

        assertEquals(DoubleX.of(10), doubleX);
    }

    @Test
    void testMapEachDigit() {
        final var count = StringX.of(1_003_293_342)
                .filter(Character::isDigit)
                .count();

        assertEquals(10, count);
    }

}
