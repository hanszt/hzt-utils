package hzt.sequences;

import hzt.collections.MapX;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntrySequenceTest {

    @Test
    void testfilterValuesAndMapValues() {
        final var mapX = MapX.of("1", 1, "2", 2, "3", 3, "4", 4);

        final var resultMap = mapX.asSequence()
                .filterValues(value -> value <= 3)
                .mapValues(day -> LocalDate.of(2000, Month.JANUARY, day))
                .toMapX();

        assertAll(
                () -> assertEquals(3, resultMap.size()),
                () -> assertEquals(Set.of("1", "2", "3"), resultMap.keySet())
        );
    }

    @Test
    void testMapValuesBothByKeyAndValue() {
        final var map = MapX.of("1", 1, "2", 2, "3", 3, "4", 4);

        final var resultMap = map.asSequence()
                .filterValues(value -> value <= 3)
                .mapValues((month, day) -> LocalDate.of(2000, Month.of(Integer.parseInt(month)), day))
                .toMapX();

        assertAll(
                () -> assertEquals(3, resultMap.size()),
                () -> assertTrue(resultMap.containsValue(LocalDate.of(2000, Month.MARCH, 3)))
        );
    }

    @Test
    void testToEntrySequence() {
        final var yearStringMapX = Sequence.generate(1, i -> ++i)
                .asEntrySequence(It::self, BigDecimal::valueOf)
                .mapKeys(Year::of)
                .takeWhileKeys(year -> year.isBefore(Year.of(2001)))
                .skip(20)
                .toMutableMap();

        assertAll(
                () -> assertEquals(1980, yearStringMapX.size()),
                () -> assertTrue(yearStringMapX.last().getKey().isLeap())
        );
    }

    @Test
    void testToEntrySequenceFromPairSequence() {
        final var yearStringMapX = Sequence.generate(0, i -> ++i)
                .map(Year::of)
                .takeWhileInclusive(year -> year.isBefore(Year.of(2000)))
                .zipWithNext()
                .asEntrySequence(It::self)
                .skip(20)
                .toMutableMap();

        assertAll(
                () -> assertEquals(1980, yearStringMapX.size()),
                () -> assertTrue(yearStringMapX.last().getValue().isLeap())
        );
    }

}
