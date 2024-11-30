package org.hzt.utils.sequences.primitives;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.chrono.IsoChronology;

import static org.junit.jupiter.api.Assertions.*;

class ConstrainOnceTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstrainOnceTests.class);

    @Test
    void testIntSequenceConstrainOnce() {
        final var integers = IntSequence.iterate(0, i -> i + 3)
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
        final var doubles = DoubleSequence.iterate(0, i -> i + Math.PI)
                .take(20)
                .map(i -> i / 4)
                .onEach(it -> LOGGER.trace("{}", it))
                .constrainOnce();

        assertAll(
                () -> assertTrue(doubles.any()),
                () -> assertThrows(IllegalStateException.class, doubles::first)
        );
    }

    @Test
    void testLongSequenceConstrainOnce() {
        final var integers = IntSequence.iterate(-1, i -> i + 7)
                .take(100)
                .onEach(it -> LOGGER.trace("{}", it))
                .constrainOnce();

        assertAll(
                () -> assertTrue(integers.any(IsoChronology.INSTANCE::isLeapYear)),
                () -> assertThrowsAndAssertMessage(integers)
        );
    }

    private static void assertThrowsAndAssertMessage(final IntSequence integers) {
        final var exception = assertThrows(IllegalStateException.class, integers::any);
        assertEquals("Sequence is already consumed", exception.getMessage());
    }
}
