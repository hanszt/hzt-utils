package org.hzt.utils.numbers;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalXTest {

    @Test
    void BigDecimalXOfInt() {
        final int integer = 21232231;
        final BigDecimal bigDecimal = BigDecimal.valueOf(integer);
        final BigDecimalX bigDecimalX = BigDecimalX.of(integer);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfLong() {
        final long longVal = 22342341352344L;
        final BigDecimal bigDecimal = BigDecimal.valueOf(longVal);
        final BigDecimalX bigDecimalX = BigDecimalX.of(longVal);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfDouble() {
        final double val = 23.3453e5;
        final BigDecimal bigDecimal = BigDecimal.valueOf(val);
        final BigDecimalX bigDecimalX = BigDecimalX.of(val);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfString() {
        final String val = "2342342134223522345263423246236235";
        final BigDecimal bigDecimal = new BigDecimal(val);
        final BigDecimalX bigDecimalX = BigDecimalX.of(val);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfStringWithExponent() {
        final String val = "23423.2342e41";
        final BigDecimal bigDecimal = new BigDecimal(val)
                .setScale(2, RoundingMode.HALF_UP);

        final BigDecimalX bigDecimalX = BigDecimalX.of(val)
                .setScale(2, RoundingMode.HALF_UP);

        It.println("bigDecimalX = " + bigDecimalX);

        assertEquals(bigDecimal, bigDecimalX);
    }

    @Test
    void BigDecimalXOfNumberXWithExponent() {
        final int integer = 1234;
        final IntX nrOfAtoms = IntX.of(integer);
        final BigDecimal bigDecimal = new BigDecimal(integer);
        final BigDecimalX bigDecimalX = nrOfAtoms.toBigDecimalX();

        assertEquals(bigDecimal, bigDecimalX);
    }

}
