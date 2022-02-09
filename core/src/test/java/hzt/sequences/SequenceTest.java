package hzt.sequences;

import hzt.collections.ArrayX;
import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.SetX;
import hzt.iterables.IterableX;
import hzt.numbers.IntX;
import hzt.numbers.LongX;
import hzt.ranges.IntRange;
import hzt.strings.StringX;
import hzt.test.Generator;
import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SequenceTest {

    @Test
    void testSimpleStreamWithMapYieldsIteratorWithNext() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final Sequence<Integer> sequence = Sequence.of(list)
                .map(SequenceTest::lengthMappingNotCalledWhenNotConsumed);

        assertTrue(sequence.iterator().hasNext());
    }

    private static int lengthMappingNotCalledWhenNotConsumed(String s) {
        fail("Should only be called when consumed with terminal operation");
        return s.length();
    }

    @Test
    void testMapReduce() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final double sum = Sequence.of(list)
                .map(String::length)
                .map(Double::valueOf)
                .reduce(Double::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(17, sum);
    }

    @Test
    void testSimpleStreamWithFilterYieldsIteratorWithNext() {
        final ListX<Integer> list = ListX.of(1, 2, 3, 4, 5, 6);
        final Sequence<Integer> sequence = Sequence.of(list)
                .filter(SequenceTest::filterNotCalledWhenNotConsumed);

        assertNotNull(sequence);
    }

    private static boolean filterNotCalledWhenNotConsumed(int i) {
        fail("Should only be called when consumed with terminal operation");
        return i < 3;
    }

    @Test
    void testFilterReduce() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final int sum = Sequence.of(list)
                .map(String::length)
                .filter(l -> l > 3)
                .reduce(Integer::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(9, sum);
    }

    @Test
    void testMapFilterReduce() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final double sum = Sequence.of(list)
                .map(String::length)
                .map(Double::valueOf)
                .reduce(Double::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(17, sum);
    }

    @Test
    void testFilterIndexed() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final var sum = Sequence.of(list)
                .map(String::length)
                .filterIndexed((index, length) -> length > 2 && index % 2 == 1)
                .sumOfLongs(It::self);

        assertEquals(6, sum);
    }

    @Test
    void testMapNotNull() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final var sum = Sequence.of(list)
                .mapNotNull(BankAccount::getBalance)
                .toListX();

        assertFalse(sum.contains(null));
    }

    @Test
    void testMapFilterReduceToArrayX() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final ArrayX<Integer> result = list.asSequence()
                .map(String::length)
                .filter(l -> l > 3)
                .toArrayX(Integer[]::new);

        assertEquals(ArrayX.of(5, 4), result);
    }

    @Test
    void testMapFilterReduceToList() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final ListX<Integer> result = list.asSequence()
                .map(String::length)
                .filter(l -> l > 3)
                .toListX();

        assertEquals(ListX.of(5, 4), result);
    }

    @Test
    void testMapFilterReduceToSet() {
        var list = ListX.of("Hallo", "dit", "is", "een", "test");
        final var result = list.asSequence()
                .map(String::length)
                .toSetX();

        assertEquals(SetX.of(2, 3, 4, 5), result);
    }

    @Test
    void testFlatMapToList() {
        final ListX<Iterable<String>> list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        final var result = list.asSequence()
                .flatMap(It::self)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toListX();

        It.println("result = " + result);

        assertIterableEquals(ListX.of("Hallo", "test"), result);
    }

    @Test
    void testCollectGroupingBy() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final Map<Painter, List<Painting>> expected = museumList.stream()
                .flatMap(m -> m.getPaintings().stream())
                .collect(Collectors.groupingBy(Painting::painter));

        final Map<Painter, List<Painting>> actual = Sequence.of(museumList)
                .flatMap(Museum::getPaintings)
                .collect(Collectors.groupingBy(Painting::painter));

        final MapX<Painter, MutableListX<Painting>> actual2 = Sequence.of(museumList)
                .flatMap(Museum::getPaintings)
                .groupBy(Painting::painter);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertEquals(expected, actual2)
        );
    }

    @Test
    void testGenerateSequence() {
        final var strings = Sequence.generate(0, i -> ++i)
                .map(Generator::fib)
                .filter(LongX::isOdd)
                .take(12)
                .map(Long::intValue)
                .map(Generator::toStringIn100Millis)
                .onEach(It::println)
                .toListX();

        assertEquals(12, strings.size());
    }

    @Test
    void testLargeSequence() {
        final ListX<BigDecimal> bigDecimals = IntRange.of(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .map(BigDecimal::valueOf)
                .sortedDescending();

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testTakeWhile() {
        final ListX<String> strings = Sequence.generate(0, i -> ++i)
                .takeWhile(i -> i < 10)
                .filter(LongX::isEven)
                .onEach(It::println)
                .map(Generator::toStringIn100Millis)
                .map(String::trim)
                .toListX();

        assertEquals(5, strings.size());
    }

    @Test
    void testTakeWhileInclusive() {
        final ListX<String> strings = Sequence.generate(1, i -> i + 2)
                .map(String::valueOf)
                .takeWhileInclusive(s -> !s.contains("0"))
                .onEach(It::println)
                .map(String::trim)
                .toListX();

        assertEquals(51, strings.size());
    }

    @Test
    void testSkipWhile() {
        ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final var integers = list.asSequence()
                .skipWhile(i -> i != 5)
                .toList();

        It.println("integers = " + integers);

        assertEquals(List.of(5, 10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testSkipWhileInclusive() {
        ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final var integers = list.asSequence()
                .skipWhileInclusive(i -> i != 5)
                .toList();

        It.println("integers = " + integers);

        assertEquals(List.of(10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testRange() {
        assertArrayEquals(
                IntStream.range(5, 10).toArray(),
                IntRange.of(5, 10).toIntArrayOf(Integer::intValue));
    }

    @Test
    void testRangeWithInterval() {
        assertArrayEquals(
                IntStream.range(5, 10).filter(IntX::isEven).toArray(),
                IntRange.of(5, 10).filter(IntX::isEven).toIntArrayOf(Integer::intValue));
    }

    @Test
    void testRangeClosed() {
        assertArrayEquals(
                IntStream.rangeClosed(5, 10).toArray(),
                IntRange.closed(5, 10).toIntArrayOf(Integer::intValue));
    }

    @Test
    void testIterateRandomWithinBound() {
        final ListX<Integer> integers = ListX.of(1, 2, 3, 4, 5);

        final MapX<Integer, MutableListX<Integer>> group = Sequence
                .generate(integers::random)
                .take(10_000_000)
                .group();

        group.values().forEach(List::size, It::println);

        assertAll(
                () -> assertEquals(integers.size(), group.size()),
                () -> group.values().forEach(ListX::isNotEmpty, Assertions::assertTrue)
        );
    }

    @Test
    void testDistinctBy() {
        final var integers = Sequence.of("hallo", "hoe", "is", "het");

        final var strings = integers
                .distinctBy(String::length)
                .map(StringX::of)
                .map(StringX::reversed)
                .toListOf(StringX::toString);

        It.println("strings = " + strings);

        assertEquals(List.of("ollah", "eoh", "si"), strings);
    }

    @Test
    void testWindowedThrowsExceptionWhenStepSizeNegative() {
        final var range = IntRange.of(0, 9);
        assertThrows(IllegalArgumentException.class, () -> range.windowed(-4));
    }

    @Test
    void testWindowedStepGreaterThanWindowSizeWithPartialWindow() {
        final var windows = IntRange.of(0, 98)
                .windowed(5, 6, true)
                .toListX();

        It.println("windows = " + windows);

        assertAll(
                () -> assertEquals(ListX.of(0, 1, 2, 3, 4), windows.first()),
                () -> assertEquals(ListX.of(96, 97), windows.last())
        );
    }

    @Test
    void testWindowedStepGreaterThanWindowSizeNoPartialWindow() {
        final var windows = IntRange.of(0, 98)
                .windowed(5, 6)
                .toListX();

        It.println("windows = " + windows);

        assertAll(
                () -> assertEquals(16, windows.size()),
                () -> assertEquals(ListX.of(0, 1, 2, 3, 4), windows.first()),
                () -> assertEquals(ListX.of(90, 91, 92, 93, 94), windows.last())
        );
    }

    @Test
    void testWindowedStepSmallerThanWindowSizeWithPartialWindow() {
        final var windows = IntRange.closed(0, 10)
                .windowed(5, 2, true)
                .toListX();

        It.println("windows = " + windows);

        assertAll(
                () -> assertEquals(6, windows.size()),
                () -> assertEquals(ListX.of(0, 1, 2, 3, 4), windows.first()),
                () -> assertEquals(ListX.of(10), windows.last())
        );
    }

    @Test
    void testWindowedStepSmallerThanWindowSizeNoPartialWindow() {
        final var windows = IntRange.of(0, 9)
                .windowed(4, 2)
                .toListX();

        It.println("windows = " + windows);

        assertAll(
                () -> assertEquals(3, windows.size()),
                () -> assertEquals(ListX.of(0, 1, 2, 3), windows.first()),
                () -> assertEquals(ListX.of(4, 5, 6, 7), windows.last())
        );
    }

    @Test
    void testWindowedSizeGreaterThanSequenceSizeNoPartialWindowGivesEmptyList() {
        final var windows = IntRange.of(0, 8)
                .windowed(10)
                .toListX();

        It.println("windows = " + windows);

        assertTrue(windows.isEmpty());
    }

    @Test
    void testLargeWindowedSequence() {
        final var windows = IntRange.of(0, 1_000_000)
                .windowed(2_001, 23, true)
                .toListX();

        final var lastWindow = windows.last();

        final var tail = windows.tailFrom(windows.size() - 2);

        It.println("tail = " + tail);

        assertAll(
                () -> assertEquals(43479, windows.size()),
                () -> assertEquals(999_994, lastWindow.first()),
                () -> assertEquals(999_999, lastWindow.last())
        );
    }

    @Test
    void testSequenceWindowedTransformed() {
        final var sizes = IntRange.of(0, 1_000)
                .filter(IntX.multipleOf(5))
                .windowed(51, 7, ListX::size)
                .toListX();

        It.println("sizes = " + sizes);

        It.println("windows.first() = " + sizes.findFirst());
        It.println("windows.last() = " + sizes.findLast());

        assertEquals(22, sizes.size());
    }

    @Test
    void testZipWithNext() {
        final var sums = IntRange.of(0, 1_000)
                .filter(IntX.multipleOf(10))
                .onEach(i -> It.print(i + ", "))
                .zipWithNext(Integer::sum)
                .toListX();

        It.println("\nsums = " + sums);

        It.println("windows.first() = " + sums.findFirst());
        It.println("windows.last() = " + sums.findLast());

        assertEquals(99, sums.size());
    }

    @Test
    void testSequenceOfMap() {
        final var map = Map.of(1, "a", 2, "b", 3, "c", 4, "d");

        final var mapX = Sequence.of(map)
                .mapValues(s -> StringX.of(s).first())
                .filterValues(Character::isLetter)
                .filterKeys(IntX::isEven)
                .toMapX();

        assertEquals(2, mapX.size());
    }

    @Test
    void testSequenceAssociateWith() {
        final var listX = ListX.of(1, 2, 3, 4);

        final var mapX = Sequence.of(listX)
                .associateWith(String::valueOf)
                .onEach(System.out::println)
                .mapValues(s -> StringX.of(s).first())
                .filterKeys(IntX::isEven)
                .onEachKey(System.out::println)
                .toMapX();

        assertEquals(2, mapX.size());
    }

    @Test
    void testEmpty() {
        final var list = Sequence.empty().toList();
        assertTrue(list.isEmpty());
    }

    @Test
    void testSequenceFromStream() {
        final var stream = IntStream.range(0, 100).boxed();

        final var list = Sequence.of(stream)
                .filter(IntX::isEven)
                .sorted()
                .windowed(3, true)
                .flatMap(It::self)
                .toList();

        It.println("list = " + list);

        assertEquals(147, list.size());
    }

    @Test
    void testGeneratedSequenceCanBeConsumedMultipleTimes() {
        var leapYears = IntRange.from(1900).upTo(2000).step(2)
                .map(Year::of)
                .filter(Year::isLeap);

        final var first = leapYears.first();
        final var last = leapYears.last();

        It.println("first = " + first);
        It.println("last = " + last);
        final var stats = leapYears.statsOfInts(Year::getValue);

        assertAll(
                () -> assertEquals(Year.of(1904), first),
                () -> assertEquals(Year.of(2000), last),
                () -> assertEquals(48800, stats.getSum())
        );
    }

    @Test
    void testSequenceCanBeConsumedMultipleTimes() {
        var names = Sequence.of(List.of(1, 2, 3, 4, 5, 3, -1, 6, 12))
                .onEach(It::println)
                .mapNotNull(Year::of)
                .sorted();

        final var first = names.first();
        It.println("first = " + first);
        It.println("first = " + names.first());
        final var nameList = names.toList();
        final var last = names.last();


        It.println("last = " + last);

        assertAll(
                () -> assertEquals(Year.of(-1), first),
                () -> assertEquals(Year.of(12), last),
                () -> assertEquals(9, nameList.size()),
                () -> assertEquals(3, names.windowed(2, 3).count())
        );
    }

    @Test
    void testSequenceAsIntRange() {
        final int year = 2020;

        final var daysOfYear = Sequence
                .generate(LocalDate.of(year, Month.JANUARY, 1), date -> date.plusDays(1))
                .takeWhile(date -> date.getYear() < year + 1)
                .asIntRange(LocalDate::getDayOfMonth);

        It.println(daysOfYear.joinToString());

        assertAll(
                () -> assertTrue(Year.of(year).isLeap()),
                () -> assertEquals(7, daysOfYear.filter(i -> i == 31).count()),
                () -> assertEquals(12, daysOfYear.filter(i -> i == 29).count()),
                () -> assertEquals(366, daysOfYear.count())
        );
    }

    @Test
    void testBranchSequence() {
        final var leepYearResult = Sequence
                .generate(LocalDate.of(1950, Month.JANUARY, 1), date -> date.plusDays(1))
                .takeWhileInclusive(date -> date.getYear() <= 2000)
                .filter(LocalDate::isLeapYear)
                .toTwo(Sequence::toList, IterableX::last);

        assertAll(
                () -> assertEquals(4758, leepYearResult.first().size()),
                () -> assertEquals(LocalDate.of(2000, Month.DECEMBER, 31), leepYearResult.second())
        );
    }

    @Test
    void testBranchSequenceToThree() {
        final var triple = IntRange.from(0).until(100)
                .toThree(Sequence::toList, s -> s.filter(IntX::isEven), s -> s.statsOfInts(It::self));

        assertAll(
                () -> assertEquals(100, triple.first().size()),
                () -> assertEquals(Year.of(0), triple.second().map(Year::of).first()),
                () -> assertEquals(49.5, triple.third().getAverage())
        );
    }

    @Test
    void testBranchSequenceToFour() {
        final var actual = IntRange.from(0).until(100)
                .toFour(Sequence::count,
                        s -> s.minOf(It::self),
                        s -> s.maxOf(It::self),
                        s -> s.sumOfInts(It::self),
                        IntSummaryStatistics::new);

        assertAll(
                () -> assertEquals(100, actual.getCount()),
                () -> assertEquals(4950, actual.getSum()),
                () -> assertEquals(49.5, actual.getAverage()),
                () -> assertEquals(99, actual.getMax())
        );
    }

    @Test
    void testStreamCanOnlyBeConsumedOnce() {
        final var yearStream = Stream.of(1, 2, 3, 4, 5, 3, -1, 6, 12)
                .filter(IntX::isEven)
                .map(Year::of);

        final var years = yearStream.collect(Collectors.toList());

        assertAll(
                () -> assertEquals(4, years.size()),
                () -> assertStreamCanOnlyBeConsumedOnce(yearStream)
        );
    }

    @Test
    void sequenceOfStreamCanOnlyBeConsumedOnce() {
        final var yearStream = Stream.of(1, 2, 3, 4, 5, 3, -1, 6, 12)
                .filter(IntX::isEven)
                .map(Year::of);

        final var yearSequence = Sequence.of(yearStream);

        assertAll(
                () -> assertEquals(4, yearSequence.count()),
                () -> assertThrows(IllegalStateException.class, yearSequence::toList)
        );
    }

    private void assertStreamCanOnlyBeConsumedOnce(Stream<Year> yearStream) {
        //noinspection ResultOfMethodCallIgnored
        final var exception = assertThrows(IllegalStateException.class, yearStream::findFirst);
        assertEquals("stream has already been operated upon or closed", exception.getMessage());
    }

}
