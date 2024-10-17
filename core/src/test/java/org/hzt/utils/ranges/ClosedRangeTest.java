package org.hzt.utils.ranges;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ClosedRangeTest {

    @Test
    void testClosedRangeOfDatesContains() {
        final var dateRange = new LocalDateRange(LocalDate.EPOCH, LocalDate.of(2020, Month.JANUARY, 1));

        assertAll(
                () -> assertTrue(dateRange.contains(LocalDate.ofEpochDay(100))),
                () -> assertFalse(dateRange.contains(LocalDate.parse("2022-10-23")))
        );
    }

    @TestFactory
    List<DynamicTest> closedRangeOverlapsTests() {
        BiFunction<IntRange, IntRange, DynamicTest> shouldOverlap = (interval, other) ->
                dynamicTest(interval + " should overlap " + other,
                        () -> assertTrue(interval.overlaps(other)));

        return List.of(
                shouldOverlap.apply(IntRange.closed(0, 4), IntRange.closed(0, 4)),
                shouldOverlap.apply(IntRange.closed(-12345, 4), IntRange.closed(4, 10_000)),
                shouldOverlap.apply(IntRange.closed(4, 8), IntRange.closed(8, 12)),
                shouldOverlap.apply(IntRange.closed(8, 12), IntRange.closed(4, 8))
        );
    }


    @TestFactory
    List<DynamicTest> closedRangeDoesNotOverlapTests() {
        BiFunction<IntRange, IntRange, DynamicTest> shouldNotOverlap = (interval, other) ->
                dynamicTest(interval + " should not overlap " + other,
                        () -> assertFalse(interval.overlaps(other)));

        return List.of(
                shouldNotOverlap.apply(IntRange.closed(-12345, 4), IntRange.closed(5, 10_000)),
                shouldNotOverlap.apply(IntRange.closed(5, 10_000), IntRange.closed(-12345, 4)),
                shouldNotOverlap.apply(IntRange.closed(4, 4), IntRange.closed(5, 5)),
                shouldNotOverlap.apply(IntRange.closed(-12, 12), IntRange.closed(13, 10_000))
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
        public LocalDate start() {
            return start;
        }

        @Override
        public LocalDate endInclusive() {
            return endInclusive;
        }
    }

}
