package org.hzt.utils.sequences;

import org.hzt.utils.It;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntrySequenceTest {

    @Test
    void testfilterValuesAndMapValues() {
        final MapX<String, Integer> mapX = MapX.of("1", 1, "2", 2, "3", 3, "4", 4);

        final MapX<String, LocalDate> resultMap = mapX.asSequence()
                .filterValues(value -> value <= 3)
                .onEach((k, v) -> It.println("key: " + k + ", value: " + v))
                .mapByValues(day -> LocalDate.of(2000, Month.JANUARY, day))
                .toMapX();

        assertAll(
                () -> assertEquals(3, resultMap.size()),
                () -> assertEquals(new HashSet<>(Arrays.asList("1", "2", "3")), resultMap.keySet())
        );
    }

    @Test
    void testMapValuesBothByKeyAndValue() {
        final MapX<String, Integer> map = MapX.of("1", 1, "2", 2, "3", 3, "4", 4);

        final MapX<String, LocalDate> resultMap = map.asSequence()
                .filterValues(value -> value <= 3)
                .mapValues((month, day) -> LocalDate.of(2000, Month.of(Integer.parseInt(month)), day))
                .onEachValue(It::println)
                .toMapX();

        assertAll(
                () -> assertEquals(3, resultMap.size()),
                () -> assertTrue(resultMap.containsValue(LocalDate.of(2000, Month.MARCH, 3)))
        );
    }

    @Test
    void testToEntrySequence() {
        final MutableMapX<Year, BigDecimal> yearStringMap = Sequence.generate(1, i -> ++i)
                .asEntrySequence(It::self, BigDecimal::valueOf)
                .mapByKeys(Year::of)
                .takeWhileKeys(year -> year.isBefore(Year.of(2001)))
                .onEachKey(It::println)
                .skip(20)
                .toMutableMap();

        assertAll(
                () -> assertEquals(1980, yearStringMap.size()),
                () -> assertTrue(yearStringMap.last().getKey().isLeap())
        );
    }

    @Test
    void testToEntrySequenceFromPairSequence() {
        final MutableMapX<Year, Integer> yearStringMap = Sequence.generate(1, i -> ++i)
                .zipWithNext(Pair::of)
                .asEntrySequence(t -> It.self(t))
                .mapByKeys(Year::of)
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
        final MutableMapX<Year, Year> yearStringMap = Sequence.generate(0, i -> ++i)
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

    @Test
    void testMerge() {
        final var mergedZipWithNext = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8)
                .zipWithNext()
                .merge()
                .toList();

        assertEquals(List.of(1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8), mergedZipWithNext);
    }

    @Test
    void testMergeValues() {
        final var mergedZipWithNext = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8)
                .associateWith(String::valueOf)
                .mergeValues(String::length)
                .toList();

        assertEquals(List.of(1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 1), mergedZipWithNext);
    }

    @Test
    void testTerminalOppMergeOfTwoDifferentTypesThrowsException() {
        final var map = Map.of(1, "2", 2, "2", 3, "3");
        final var stringSequence = EntrySequence.ofMap(map).merge();

        final var exception = assertThrows(IllegalStateException.class, stringSequence::toList);
        assertEquals("Key and value not of same type. Merge not allowed", exception.getMessage());
    }

}
