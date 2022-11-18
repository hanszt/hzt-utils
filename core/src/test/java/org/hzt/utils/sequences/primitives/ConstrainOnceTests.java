package org.hzt.utils.sequences.primitives;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.time.chrono.IsoChronology;

import static org.junit.jupiter.api.Assertions.*;

class ConstrainOnceTests {

    @Test
    void testIntSequenceConstrainOnce() {
        final IntSequence integers = IntSequence.iterate(0, i -> i + 3)
                .take(100)
                .map(i -> i / 4)
                .constrainOnce();

        assertAll(
                () -> assertEquals(74, integers.last()),
                () -> assertThrows(IllegalStateException.class, integers::first)
        );
    }

    @Test
    void testDoubleSequenceConstrainOnce() {
        final DoubleSequence doubles = DoubleSequence.iterate(0, i -> i + Math.PI)
                .take(20)
                .map(i -> i / 4)
                .onEach(It::println)
                .constrainOnce();

        assertAll(
                () -> assertTrue(doubles.any()),
                () -> assertThrows(IllegalStateException.class, doubles::first)
        );
    }

    @Test
    void testLongSequenceConstrainOnce() {
        final IntSequence integers = IntSequence.iterate(-1, i -> i + 7)
                .take(100)
                .onEach(It::println)
                .constrainOnce();

        assertAll(
                () -> assertTrue(integers.any(IsoChronology.INSTANCE::isLeapYear)),
                () -> assertThrowsAndAssertMessage(integers)
        );
    }

    private static void assertThrowsAndAssertMessage(IntSequence integers) {
        final IllegalStateException exception = assertThrows(IllegalStateException.class, integers::any);
        assertEquals("Sequence is already consumed", exception.getMessage());
    }
}
