package org.hzt.utils.numbers;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BigDecimalXTest {

    @Test
    void BigDecimalXOfInt() {
        final var integer = 21232231;
        final var bigDecimal = BigDecimal.valueOf(integer);
        final var bigDecimalX = BigDecimalX.of(integer);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfLong() {
        final var longVal = 22342341352344L;
        final var bigDecimal = BigDecimal.valueOf(longVal);
        final var bigDecimalX = BigDecimalX.of(longVal);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfDouble() {
        final var val = 23.3453e5;
        final var bigDecimal = BigDecimal.valueOf(val);
        final var bigDecimalX = BigDecimalX.of(val);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfString() {
        final var val = "2342342134223522345263423246236235";
        final var bigDecimal = new BigDecimal(val);
        final var bigDecimalX = BigDecimalX.of(val);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfStringWithExponent() {
        final var val = "23423.2342e41";
        final var bigDecimal = new BigDecimal(val)
                .setScale(2, RoundingMode.HALF_UP);

        final var bigDecimalX = BigDecimalX.of(val)
                .setScale(2, RoundingMode.HALF_UP);

        It.println("bigDecimalX = " + bigDecimalX);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfNumberXWithExponent() {
        final var integer = 1234;
        final var nrOfAtoms = IntX.of(integer);
        final var bigDecimal = new BigDecimal(integer);
        final var bigDecimalX = nrOfAtoms.toBigDecimalX();

        assertEquals(bigDecimal, bigDecimalX);
    }

}
