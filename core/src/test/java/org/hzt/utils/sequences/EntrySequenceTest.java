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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
    void testEntrySequenceOfMap() {
        final Map<Integer, String> map = MapX.of(1, "hallo", 2, "This", 3, "is", 4, "a", 5, "test").toMap();

        final Map<Integer, String> integerStringMap = EntrySequence.ofMap(map)
                .filterKeys(key -> key % 2 == 0)
                .toMap();

        assertEquals(2, integerStringMap.size());
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
        final MutableMapX<Year, BigDecimal> yearStringMap = Sequence.iterate(1, i -> ++i)
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
        //noinspection Convert2MethodRef
        final MutableMapX<Year, Integer> yearStringMap = Sequence.iterate(1, i -> ++i)
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
        final MutableMapX<Year, Year> yearStringMap = Sequence.iterate(0, i -> ++i)
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
        final List<Integer> mergedZipWithNext = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8)
                .zipWithNext()
                .merge()
                .toList();

        assertEquals(Arrays.asList(1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8), mergedZipWithNext);
    }

    @Test
    void testMergeValues() {
        final List<Integer> mergedZipWithNext = Sequence.of(1, 2, 3, 4, 5, 6, 7, 8)
                .associateWith(String::valueOf)
                .mergeValues(String::length)
                .toList();

        assertEquals(Arrays.asList(1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 1), mergedZipWithNext);
    }

    @Test
    void testTerminalOppMergeOfTwoDifferentTypesThrowsException() {
        final MapX<Integer, String> map = MapX.of(1, "2", 2, "2", 3, "3");
        final Sequence<String> stringSequence = EntrySequence.of(map).merge();

        final IllegalStateException exception = assertThrows(IllegalStateException.class, stringSequence::toList);
        assertEquals("Key and value not of same type. Merge not allowed", exception.getMessage());
    }

}
