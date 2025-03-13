package org.hzt.utils.iterables;

import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.tuples.IndexedValue;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReducableTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReducableTest.class);

    @Test
    void testSingle() {
        final var singleton = SetX.of(10);
        final var single = singleton.single();
        assertEquals(10, single);
    }

    @Test
    void testSingleConditional() {
        final var set = SetX.of(10, 3, 6, 2);
        final var single = set.single(i -> i < 3);
        assertEquals(2, single);
    }

    @Test
    void testSingleCallOnEmptyIterableYieldsNoSuchElementException() {
        final var set = SetX.empty();
        assertThrows(NoSuchElementException.class, set::single);
    }

    @Test
    void testSingleCallOnIterableHavingMoreThanOneElementYieldsIllegalArgumentException() {
        final var set = SetX.of(10, 9);
        assertThrows(IllegalArgumentException.class, set::single);
    }

    @Test
    void foldYearDayAddition() {
        final var initDate = LocalDate.of(2000, Month.JANUARY, 1);

        final var localDate = Sequence.iterate(1, It::self)
                .take(100)
                .fold(initDate, LocalDate::plusDays);

        final var expected = initDate.plusDays(100);

        assertAll(
                () -> assertEquals(LocalDate.of(2000, Month.APRIL, 10), localDate),
                () -> assertEquals(expected, localDate)
        );
    }

    @Test
    void testFoldToMutableList() {
        final var listX = Sequence.of(1, 2, 3, 4, 5)
                .fold(MutableListX.<Integer>empty(), (ints, value) -> {
                    ints.add(value);
                    return ints;
                });

        assertEquals(MutableListX.of(1, 2, 3, 4, 5), listX);
    }

    @Test
    void testFoldIndexed() {
        final MutableListX<IndexedValue<String>> listX = Sequence.of(1, 2, 3, 4, 5)
                .foldIndexed(MutableListX.empty(), (index, acc, val) -> acc.plus(new IndexedValue<>(index, val.toString())));

        assertEquals(ListX.of(1, 2, 3, 4, 5).map(Object::toString).withIndex().toMutableList(), listX);
    }

    @Test
    void testFoldTwoInOnePass() {
        final var dateSequence = generateLeapYearDateSequence();

        final var iterations1 = new Counter();

        final var expected = dateSequence
                .onEach(_ -> iterations1.value++)
                .toTwo(Numerable::count, Reducable::last);

        final var iterations2 = new Counter();

        final var actual = dateSequence
                .onEach(_ -> iterations2.value++)
                .foldTwo(0L, (acc, _) -> ++acc,
                        LocalDate.EPOCH, (_, second) -> second);

        LOGGER.atDebug().setMessage(() -> "pair = " + actual).log();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(iterations1.value, iterations2.value++ * 2)
        );
    }

    @Test
    void testFoldThreeInOnePass() {
        final var dateSequence = generateLeapYearDateSequence();

        final var iterations1 = new Counter();

        final var expected = dateSequence
                .onEach(_ -> iterations1.value++)
                .toThree(Sequence::toMutableList, Numerable::count, Reducable::last);

        final var iterations2 = new Counter();

        final var actual = dateSequence
                .onEach(_ -> iterations2.value++)
                .foldThree(MutableListX.empty(), MutableListX::plus,
                        0L, (a, _) -> ++a,
                        LocalDate.EPOCH, (_, second) -> second);

        LOGGER.atDebug().setMessage(() -> "pair = " + actual).log();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(iterations1.value, iterations2.value * 3)
        );
    }

    @Test
    void tesReduceTwoInOnePass() {
        final var dateSequence = generateLeapYearDateSequence();

        final var iterations1 = new Counter();

        final var expected = dateSequence
                .onEach(_ -> iterations1.value++)
                .toTwo(Reducable::last, Reducable::first);

        final var iterations2 = new Counter();

        final var actual = dateSequence
                .onEach(_ -> iterations2.value++)
                .reduceTwo((_, last) -> last, (first, _) -> first);

        final var pair = actual.orElseThrow();
        LOGGER.atDebug().setMessage(() -> "pair = " + pair).log();

        assertAll(
                () -> assertEquals(expected.first(), pair.first()),
                () -> assertEquals(expected.second(), pair.second()),
                () -> assertEquals(iterations1.value, iterations2.value + 1)
        );
    }

    private static Sequence<LocalDate> generateLeapYearDateSequence() {
        return Sequence.iterate(LocalDate.EPOCH, d -> d.plusDays(1))
                .takeWhile(d -> d.getYear() <= 1980)
                .filter(LocalDate::isLeapYear);
    }

    @Test
    void testReduce() {
        final var result = Sequence.of(ZoneId.getAvailableZoneIds())
                .reduce("", (acc, s) -> acc.length() > s.length() ? acc : s);

        final var expected = Sequence.of(ZoneId.getAvailableZoneIds())
                .maxBy(String::length)
                .orElseThrow();

        final var expected2 = ZoneId.getAvailableZoneIds().stream()
                .max(Comparator.comparing(String::length))
                .orElseThrow();

        assertAll(
                () -> assertEquals("America/Argentina/ComodRivadavia", result),
                () -> assertEquals(expected, result),
                () -> assertEquals(expected2, result)
        );
    }

    @Test
    void testFindLast() {
        final var list = ListX.of("hi", "hello", "this", "is", "a", "test");
        final var last = list.last(s -> s.contains("i"));

        assertEquals("is", last);
    }
    
    private static final class Counter {
        int value = 0;
    }
}
