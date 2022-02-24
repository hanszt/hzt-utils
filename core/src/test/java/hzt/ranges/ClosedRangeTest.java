package hzt.ranges;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClosedRangeTest {

    @Test
    void testClosedRangeOfDatesContains() {
        final DateRange dateRange = new DateRange(LocalDate.ofEpochDay(0), LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(dateRange.contains(LocalDate.ofEpochDay(100))),
                () -> assertFalse(dateRange.contains(LocalDate.now()))
        );
    }

    @Test
    void testClosedRangeOfDatesIsEmpty() {
        final DateRange dateRange = new DateRange(LocalDate.ofEpochDay(0), LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(new DateRange(LocalDate.now(), LocalDate.ofEpochDay(0)).isEmpty()),
                () -> assertTrue(dateRange.isNotEmpty())
        );
    }

    private static final class DateRange implements ClosedRange<LocalDate> {
        private final LocalDate start;
        private final LocalDate endInclusive;

        public DateRange(LocalDate start, LocalDate endInclusive) {
            this.start = start;
            this.endInclusive = endInclusive;
        }

        @Override
        public @NotNull LocalDate start() {
            return start;
        }

        @Override
        public @NotNull LocalDate endInclusive() {
            return endInclusive;
        }
    }

}
