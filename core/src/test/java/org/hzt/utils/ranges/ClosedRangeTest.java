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
        final LocalDateRange dateRange = new LocalDateRange(LocalDate.ofEpochDay(0), LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(dateRange.contains(LocalDate.ofEpochDay(100))),
                () -> assertFalse(dateRange.contains(LocalDate.parse("2022-10-23")))
        );
    }

    @Test
    void testClosedRangeOfDatesIsEmpty() {
        final LocalDateRange dateRange = new LocalDateRange(LocalDate.ofEpochDay(0), LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(new LocalDateRange(LocalDate.now(), LocalDate.ofEpochDay(0)).isEmpty()),
                () -> assertTrue(dateRange.isNotEmpty())
        );
    }

    private static final class LocalDateRange implements ClosedRange<LocalDate> {
        private final LocalDate start;
        private final LocalDate endInclusive;

        public LocalDateRange(final LocalDate start, final LocalDate endInclusive) {
            this.start = start;
            this.endInclusive = endInclusive;
        }

        @Override
        public LocalDate start() {
            return start;
        }

        @Override
        public LocalDate endInclusive() {
            return endInclusive;
        }
    }

}
