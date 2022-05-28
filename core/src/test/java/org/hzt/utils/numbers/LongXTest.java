package org.hzt.utils.numbers;

import org.hzt.utils.collections.primitives.LongListX;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongXTest {

    @Test
    void testGenerateFibonacciNrs() {
        final var longs = LongX.fibonacciSequence()
                .take(10)
                .toListX();

        assertEquals(LongListX.of(0, 1, 1, 2, 3, 5, 8, 13, 21, 34), longs);
    }

    @Test
    void testNthFibNr() {
        final var fibNr = LongX.nthFibonacciNumber(7);

        assertEquals(8, fibNr);
    }

}