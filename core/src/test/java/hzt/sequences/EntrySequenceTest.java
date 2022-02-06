package hzt.sequences;

import hzt.collections.MapX;
import hzt.collections.MutableMapX;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntrySequenceTest {

    @Test
    @Disabled("Needs to be fixed in java 8")
    void testEntrySequence() {
//        final MapX<String, Integer> mapX = MapX.of("1", 1, "2", 2, "3", 3, "4", 4);
//
//        final MapX<LocalDate, String> resultMap = mapX.asSequence()
//                .map(this::reversed)
//                .filterByKeys(value -> value <= 3)
//                .mapKeys(day -> LocalDate.of(2000, Month.JANUARY, day))
//                .toMapX();
//
//        assertAll(
//                () -> assertEquals(3, resultMap.size()),
//                () -> assertFalse(resultMap.containsKey(LocalDate.of(2000, Month.JANUARY, 4)))
//        );
    }

    @NotNull
    private Pair<Integer, String> reversed(String k, Integer v) {
        return Pair.of(v, k);
    }

    @Test
    void testToEntrySequence() {
        final MutableMapX<Year, BigDecimal> yearStringMapX = Sequence.generate(1, i -> ++i)
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

}
