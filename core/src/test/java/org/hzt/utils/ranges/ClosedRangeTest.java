package org.hzt.utils.ranges;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClosedRangeTest {

    @Test
    void testClosedRangeOfDatesContains() {
        final var dateRange = new LocalDateRange(LocalDate.EPOCH, LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(dateRange.contains(LocalDate.ofEpochDay(100))),
                () -> assertFalse(dateRange.contains(LocalDate.parse("2022-10-23")))
        );
    }

    @Test
    void testClosedRangeOfDatesIsEmpty() {
        final var dateRange = new LocalDateRange(LocalDate.EPOCH, LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(new LocalDateRange(LocalDate.now(), LocalDate.EPOCH).isEmpty()),
                () -> assertTrue(dateRange.isNotEmpty())
        );
    }

    private record LocalDateRange(LocalDate start, LocalDate endInclusive) implements ClosedRange<LocalDate> {

        @Override
        public
        LocalDate start() {
            return start;
        }

        @Override
        public
        LocalDate endInclusive() {
            return endInclusive;
        }
    }

}
