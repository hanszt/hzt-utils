package org.hzt.utils.sequences;

import org.hzt.utils.collections.MapX;
import org.hzt.utils.tuples.Pair;
import org.hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntrySequenceTest {

    @Test
    void testfilterValuesAndMapValues() {
        final MapX<String, Integer> mapX = MapX.of("1", 1, "2", 2, "3", 3, "4", 4);

        final MapX<String, LocalDate> resultMap = mapX.asSequence()
                .filterValues(value -> value <= 3)
                .onEach((k, v) -> It.println("key: " + k + ", value: " + v))
                .mapValues(day -> LocalDate.of(2000, Month.JANUARY, day))
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
                .mapKeys(Year::of)
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

}
