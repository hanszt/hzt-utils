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
        final var dateRange = new DateRange(LocalDate.EPOCH, LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(dateRange.contains(LocalDate.ofEpochDay(100))),
                () -> assertFalse(dateRange.contains(LocalDate.now()))
        );
    }

    @Test
    void testClosedRangeOfDatesIsEmpty() {
        final var dateRange = new DateRange(LocalDate.EPOCH, LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(new DateRange(LocalDate.now(), LocalDate.EPOCH).isEmpty()),
                () -> assertTrue(dateRange.isNotEmpty())
        );
    }

    private record DateRange(LocalDate start, LocalDate endInclusive) implements ClosedRange<LocalDate> {

        @Override
        public @NotNull
        LocalDate start() {
            return start;
        }

        @Override
        public @NotNull
        LocalDate endInclusive() {
            return endInclusive;
        }
    }

}
