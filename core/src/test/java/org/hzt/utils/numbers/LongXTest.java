package org.hzt.utils.numbers;

import org.hzt.utils.collections.primitives.LongList;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LongXTest {

    @Test
    void testGenerateFibonacciNrs() {
        final var longs = LongX.fibonacciSequence()
                .take(10)
                .toList();

        assertEquals(LongList.of(0, 1, 1, 2, 3, 5, 8, 13, 21, 34), longs);
    }

    @Test
    void testNthFibNr() {
        final var fibNr = LongX.nthFibonacciNumber(7);

        assertEquals(8, fibNr);
    }

    @Test
    void testNthFibNrNoSuchElement() {
        final var n = 94;
        final var exception = assertThrows(NoSuchElementException.class, () -> LongX.nthFibonacciNumber(n));
        assertEquals("term n>=94 would yield value larger than Long.MAX_VALUE", exception.getMessage());
    }

    @Test
    void testUnBoundedFibSequenceThrowsNoSuchElement() {
        final var fibonacciSequence = LongX.fibonacciSequence();
        final var exception = assertThrows(NoSuchElementException.class, fibonacciSequence::sum);
        assertEquals("term n>=94 would yield value larger than Long.MAX_VALUE", exception.getMessage());
    }

}
