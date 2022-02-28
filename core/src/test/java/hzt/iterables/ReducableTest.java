package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MutableListX;
import hzt.collections.SetX;
import hzt.sequences.Sequence;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import hzt.utils.It;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReducableTest {

    @Test
    void testSingle() {
        final SetX<Integer> singleton = SetX.of(10);
        final Integer single = singleton.single();
        assertEquals(10, single);
    }

    @Test
    void testSingleConditional() {
        final SetX<Integer> set = SetX.of(10, 3, 6, 2);
        final Integer single = set.single(i -> i < 3);
        assertEquals(2, single);
    }

    @Test
    void testSingleCallOnEmptyIterableYieldsNoSuchElementException() {
        final SetX<Object> set = SetX.empty();
        assertThrows(NoSuchElementException.class, set::single);
    }

    @Test
    void testSingleCallOnIterableHavingMoreThanOneElementYieldsIllegalArgumentException() {
        final SetX<Integer> set = SetX.of(10, 9);
        assertThrows(IllegalArgumentException.class, set::single);
    }

    @Test
    void foldYearDayAddition() {
        final LocalDate initDate = LocalDate.of(2000, Month.JANUARY, 1);

        final LocalDate localDate = Sequence.generate(1, It::self)
                .take(100)
                .fold(initDate, LocalDate::plusDays);

        final LocalDate expected = initDate.plusDays(100);

        assertAll(
                () -> assertEquals(LocalDate.of(2000, Month.APRIL, 10), localDate),
                () -> assertEquals(expected, localDate)
        );
    }

    @Test
    void testFoldTwoInOnePass() {
        final Sequence<LocalDate> dateSequence = Sequence.generate(LocalDate.ofEpochDay(0), d -> d.plusDays(1))
                .takeWhile(d -> d.getYear() <= 1980)
                .filter(LocalDate::isLeapYear);

        final AtomicInteger iterations1 = new AtomicInteger();

        final Pair<Long, LocalDate> expected = dateSequence
                .onEach(d -> iterations1.incrementAndGet())
                .toTwo(Numerable::count, Reducable::last);

        final AtomicInteger iterations2 = new AtomicInteger();

        final Pair<Long, LocalDate> actual = dateSequence
                .onEach(d -> iterations2.incrementAndGet())
                .foldTwo(0L, (acc, date) -> ++acc,
                        LocalDate.ofEpochDay(0), (first, second) -> second);

        It.println("pair = " + actual);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(iterations1.get(), iterations2.get() * 2)
        );
    }

    @Test
    void testFoldThreeInOnePass() {
        final Sequence<LocalDate> dateSequence = Sequence.generate(LocalDate.ofEpochDay(0), d -> d.plusDays(1))
                .takeWhile(d -> d.getYear() <= 1980)
                .filter(LocalDate::isLeapYear);

        final AtomicInteger iterations1 = new AtomicInteger();

        final Triple<MutableListX<LocalDate>, Long, LocalDate> expected = dateSequence
                .onEach(d -> iterations1.incrementAndGet())
                .toThree(Sequence::toMutableList, Numerable::count, Reducable::last);

        final AtomicInteger iterations2 = new AtomicInteger();

        final Triple<MutableListX<Object>, Long, LocalDate> actual = dateSequence
                .onEach(d -> iterations2.incrementAndGet())
                .foldThree(MutableListX.empty(), MutableListX::plus,
                        0L, (a, b) -> ++a,
                        LocalDate.ofEpochDay(0), (first, second) -> second);

        It.println("pair = " + actual);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(iterations1.get(), iterations2.get() * 3)
        );
    }

    @Test
    void tesReduceTwoInOnePass() {
        final Sequence<LocalDate> dateSequence = Sequence.generate(LocalDate.ofEpochDay(0), d -> d.plusDays(1))
                .takeWhile(d -> d.getYear() <= 1980)
                .filter(LocalDate::isLeapYear);

        final AtomicInteger iterations1 = new AtomicInteger();

        final Pair<LocalDate, LocalDate> expected = dateSequence
                .onEach(d -> iterations1.incrementAndGet())
                .toTwo(Reducable::last, Reducable::first);

        final AtomicInteger iterations2 = new AtomicInteger();

        final Optional<Pair<LocalDate, LocalDate>> actual = dateSequence
                .onEach(d -> iterations2.incrementAndGet())
                .reduceTwo((a, last) -> last, (first, b) -> first);

        final Pair<LocalDate, LocalDate> pair = actual.orElseThrow(NoSuchElementException::new);
        It.println("pair = " + pair);

        assertAll(
                () -> assertEquals(expected.first(), pair.first()),
                () -> assertEquals(expected.second(), pair.second()),
                () -> assertEquals(iterations1.get(), iterations2.get() + 1)
        );
    }

    @Test
    void testReduce() {
        final String result = Sequence.of(ZoneId.getAvailableZoneIds())
                .reduce("", (acc, s) -> acc.length() > s.length() ? acc : s);

        final String expected = Sequence.of(ZoneId.getAvailableZoneIds())
                .maxBy(String::length)
                .orElse("");

        final String expected2 = ZoneId.getAvailableZoneIds().stream()
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
        ListX<String> list = ListX.of("hi", "hello", "this", "is", "a", "test");
        final String last = list.last(s -> s.contains("i"));

        assertEquals("is", last);
    }
}
