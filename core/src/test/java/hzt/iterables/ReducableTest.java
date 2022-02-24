package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.SetX;
import hzt.sequences.Sequence;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReducableTest {

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

        final var localDate = Sequence.generate(1, It::self)
                .take(100)
                .fold(initDate, LocalDate::plusDays);

        final var expected = initDate.plusDays(100);

        assertAll(
                () -> assertEquals(LocalDate.of(2000, Month.APRIL, 10), localDate),
                () -> assertEquals(expected, localDate)
        );
    }

    @Test
    void testFoldTwoInOnePass() {
        final var dateSequence = Sequence.generate(LocalDate.EPOCH, d -> d.plusDays(1))
                .takeWhile(d -> d.getYear() <= 1980)
                .filter(LocalDate::isLeapYear);

        final var iterations1 = new AtomicInteger();

        final var expected = dateSequence
                .onEach(d -> iterations1.incrementAndGet())
                .toTwo(Numerable::count, Reducable::last);

        final var iterations2 = new AtomicInteger();

        final var actual = dateSequence
                .onEach(d -> iterations2.incrementAndGet())
                .foldTwo(0L, (acc, date) -> ++acc,
                        LocalDate.EPOCH, (first, second) -> second);

        It.println("pair = " + actual);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(iterations1.get(), iterations2.get() * 2)
        );
    }

    @Test
    void tesReduceTwoInOnePass() {
        final var dateSequence = Sequence.generate(LocalDate.EPOCH, d -> d.plusDays(1))
                .takeWhile(d -> d.getYear() <= 1980)
                .filter(LocalDate::isLeapYear);

        final var iterations1 = new AtomicInteger();

        final var expected = dateSequence
                .onEach(d -> iterations1.incrementAndGet())
                .toTwo(Reducable::last, Reducable::first);

        final var iterations2 = new AtomicInteger();

        final var actual = dateSequence
                .onEach(d -> iterations2.incrementAndGet())
                .reduceTwo((a, last) -> last, (first, b) -> first);

        final var pair = actual.orElseThrow();
        It.println("pair = " + pair);

        assertAll(
                () -> assertEquals(expected.first(), pair.first()),
                () -> assertEquals(expected.second(), pair.second()),
                () -> assertEquals(iterations1.get(), iterations2.get() + 1)
        );
    }

    @Test
    void testReduce() {
        final var result = Sequence.of(ZoneId.getAvailableZoneIds())
                .reduce("", (acc, s) -> acc.length() > s.length() ? acc : s);

        final var expected = Sequence.of(ZoneId.getAvailableZoneIds())
                .maxBy(String::length)
                .orElse("");

        final var expected2 = ZoneId.getAvailableZoneIds().stream()
                .max(Comparator.comparing(String::length))
                .orElse("");

        assertAll(
                () -> assertEquals("America/Argentina/ComodRivadavia", result),
                () -> assertEquals(expected, result),
                () -> assertEquals(expected2, result)
        );
    }

    @Test
    void testFindLast() {
        var list = ListX.of("hi", "hello", "this", "is", "a", "test");
        final var last = list.last(s -> s.contains("i"));

        assertEquals("is", last);
    }
}
