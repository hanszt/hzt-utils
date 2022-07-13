package org.hzt.utils.numbers;

import org.hzt.utils.collections.primitives.LongList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LongXTest {

    @Test
    void testGenerateFibonacciNrs() {
        final LongList longs = LongX.fibonacciSequence()
                .take(10)
                .toList();

        assertEquals(LongList.of(0, 1, 1, 2, 3, 5, 8, 13, 21, 34), longs);
    }

    @Test
    void testNthFibNr() {
        final long fibNr = LongX.nthFibonacciNumber(7);

        assertEquals(8, fibNr);
    }

}
