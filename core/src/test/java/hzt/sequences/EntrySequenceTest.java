package hzt.sequences;

import hzt.collections.MapView;
import hzt.tuples.Pair;
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
        final var mapView = MapView.of("1", 1, "2", 2, "3", 3, "4", 4);

        final var resultMap = mapView.asSequence()
                .filterValues(value -> value <= 3)
                .mapValues(day -> LocalDate.of(2000, Month.JANUARY, day))
                .toMapView();

        assertAll(
                () -> assertEquals(3, resultMap.size()),
                () -> assertEquals(Set.of("1", "2", "3"), resultMap.keySet())
        );
    }

    @Test
    void testMapValuesBothByKeyAndValue() {
        final var map = MapView.of("1", 1, "2", 2, "3", 3, "4", 4);

        final var resultMap = map.asSequence()
                .filterValues(value -> value <= 3)
                .mapValues((month, day) -> LocalDate.of(2000, Month.of(Integer.parseInt(month)), day))
                .toMapView();

        assertAll(
                () -> assertEquals(3, resultMap.size()),
                () -> assertTrue(resultMap.containsValue(LocalDate.of(2000, Month.MARCH, 3)))
        );
    }

    @Test
    void testToEntrySequence() {
        final var yearStringMap = Sequence.generate(1, i -> ++i)
                .asEntrySequence(It::self, BigDecimal::valueOf)
                .mapKeys(Year::of)
                .takeWhileKeys(year -> year.isBefore(Year.of(2001)))
                .skip(20)
                .toMutableMap();

        assertAll(
                () -> assertEquals(1980, yearStringMap.size()),
                () -> assertTrue(yearStringMap.last().getKey().isLeap())
        );
    }

    @Test
    void testToEntrySequenceFromPairSequence() {
        final var yearStringMap = Sequence.generate(1, i -> ++i)
                .zipWithNext(Pair::of)
                .asEntrySequence(It::self)
                .mapKeys(Year::of)
                .takeWhileKeys(year -> year.isBefore(Year.of(2001)))
                .skip(20)
                .toMutableMap();

        assertAll(
                () -> assertEquals(1980, yearStringMap.size()),
                () -> assertTrue(yearStringMap.last().getKey().isLeap())
        );
    }

    @Test
    void testToEntrySequenceByZipWithNext() {
        final var yearStringMap = Sequence.generate(0, i -> ++i)
                .map(Year::of)
                .takeWhileInclusive(year -> year.isBefore(Year.of(2000)))
                .zipWithNext()
                .skip(20)
                .toMutableMap();

        assertAll(
                () -> assertEquals(1980, yearStringMap.size()),
                () -> assertTrue(yearStringMap.last().getValue().isLeap())
        );
    }

}
