package org.hzt.utils.sequences;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.collections.LinkedSetX;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collections.primitives.IntListX;
import org.hzt.utils.collections.primitives.IntMutableListX;
import org.hzt.utils.iterables.Reducable;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.test.Generator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SequenceTest {

    @BeforeAll
    static void setup() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");
    }

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
                .orElseThrow();

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
                .orElseThrow();

        assertEquals(9, sum);
    }

    @Test
    void testMapFilterReduce() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final double sum = Sequence.of(list)
                .map(String::length)
                .map(Double::valueOf)
                .reduce(Double::sum)
                .orElseThrow();

        assertEquals(17, sum);
    }

    @Test
    void testFilterIndexed() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final var sum = Sequence.of(list)
                .map(String::length)
                .filterIndexed((index, length) -> length > 2 && IntX.isOdd(index))
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
    void testMapFilterReduceToIntArray() {
        ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final int[] result = list
                .mapToInt(String::length)
                .filter(l -> l > 3)
                .toArray();

        assertArrayEquals(new int[]{5, 4}, result);
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
    void testMapMultiToList() {
        final ListX<Iterable<String>> list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        final var result = list.asSequence()
                .<String>mapMulti(Iterable::forEach)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toListX();

        It.println("result = " + result);

        assertEquals(ListX.of("Hallo", "test"), result);
    }

    @Test
    void testMapMultiToIntList() {
        final ListX<CollectionX<String>> list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                SetX.of("test"));

        final var result = list.asSequence()
                .mapMultiToInt((strings, accept) -> strings
                        .mapToInt(String::length)
                        .forEachInt(accept))
                .filter(length -> length > 3)
                .toListX();

        It.println("result = " + result);

        assertEquals(IntListX.of(5, 4), result);
    }

    @Test
    void testGenerateWindowedThenMapMultiToList() {
        MutableListX<ListX<Integer>> windows = MutableListX.empty();

        final var result = Sequence.generate(0, i -> ++i)
                .windowed(8, 3)
                .onEach(windows::add)
                .takeWhile(s -> s.sumOfInts(It::asInt) < 1_000_000)
                .<Integer>mapMulti(Iterable::forEach)
                .toListX();

        windows.filterIndexed((i, v) -> IntX.multipleOf(10_000).test(i)).forEach(It::println);

        It.println("windows.last() = " + windows.last());

        assertEquals(333328, result.size());
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
    void testFlatMapStream() {
        final var charInts = Sequence.of("hallo", "test")
                .map(String::chars)
                .flatMapStream(IntStream::boxed)
                .toList();

        assertEquals(List.of(104, 97, 108, 108, 111, 116, 101, 115, 116), charInts);
    }

    @Test
    void testTransform() {
        final var map = Sequence.of("hallo", "test")
                .map(String::chars)
                .flatMapStream(IntStream::boxed)
                .transform(this::toFilteredMapX);

        assertEquals(LinkedSetX.of(115, 116, 101, 104, 108, 111), map.keySet());
    }

    private MapX<Integer, String> toFilteredMapX(Sequence<Integer> sequence) {
        return sequence.associateWith(String::valueOf)
                .filterValues(s -> s.startsWith("1"))
                .toMapX();
    }

    @Test
    void testGenerateSequence() {
        final var strings = Sequence.generate(0, i -> ++i)
                .map(Generator::fib)
                .filter(LongX::isOdd)
                .take(12)
                .map(Long::intValue)
                .onEach(It::println)
                .toListX();

        assertEquals(12, strings.size());
    }

    @Test
    void testLargeSequence() {
        final ListX<BigDecimal> bigDecimals = IntRange.of(0, 100_000)
                .filter(IntX::isEven)
                .boxed()
                .map(BigDecimal::valueOf)
                .sortedDescending()
                .toListX();

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testTakeWhile() {
        final ListX<String> strings = Sequence.generate(0, i -> ++i)
                .takeWhile(i -> i < 10)
                .filter(LongX::isEven)
                .onEach(It::println)
                .map(String::valueOf)
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
    void testIterateRandomWithinBound() {
        final IntListX integers = IntListX.of(1, 2, 3, 4, 5);

        final MapX<Integer, IntMutableListX> group = IntSequence
                .generate(integers::random)
                .take(10_000_000)
                .group();

        group.values().forEach(IntListX::size, It::println);

        assertAll(
                () -> assertEquals(integers.size(), group.size()),
                () -> group.values().forEach(IntListX::isNotEmpty, Assertions::assertTrue)
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
        final var integers = IntSequence.of(0, 9).boxed();
        assertThrows(IllegalArgumentException.class, () -> integers.windowed(-4));
    }

    @Test
    void testWindowedStepGreaterThanWindowSizeWithPartialWindow() {
        final var windows = IntRange.of(0, 98)
                .boxed()
                .windowed(5, 6, true)
                .toListX();

        It.println("windows = " + windows);

        new HashMap<>(0);

        assertAll(
                () -> assertEquals(ListX.of(0, 1, 2, 3, 4), windows.first()),
                () -> assertEquals(ListX.of(96, 97), windows.last())
        );
    }

    @Test
    void testWindowedStepGreaterThanWindowSizeNoPartialWindow() {
        final var windows = IntRange.of(0, 98)
                .boxed()
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
                .boxed()
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
                .boxed()
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
    void testWindowedVariableSize() {
        final var windows = IntRange.of(0, 40)
                .boxed()
                .windowed(1, n -> ++n, 4, true, It::self)
                .toListX();

        It.println("windows = " + windows);

        assertAll(
                () -> assertEquals(10, windows.size()),
                () -> assertEquals(ListX.of(0), windows.first()),
                () -> assertEquals(ListX.of(36, 37, 38, 39), windows.last())
        );
    }

    @Test
    void testWindowedSizeGreaterThanSequenceSizeNoPartialWindowGivesEmptyList() {
        final var windows = IntSequence.of(0, 8)
                .boxed()
                .windowed(10)
                .toListX();

        It.println("windows = " + windows);

        assertTrue(windows.isEmpty());
    }

    @Test
    void testSequenceWindowedTransformed() {
        final var sizes = IntRange.of(0, 1_000)
                .filter(i -> IntX.multipleOf(5).test(i))
                .boxed()
                .windowed(51, 7)
                .map(ListX::size)
                .toListX();

        It.println("sizes = " + sizes);

        It.println("windows.first() = " + sizes.findFirst());
        It.println("windows.last() = " + sizes.findLast());

        assertEquals(22, sizes.size());
    }

    @Test
    void testZipSequenceWithIterable() {
        Sequence<Integer> values = Sequence.of(0, 1, 2, 3, 4, 5, 6, 7, 4);
        List<Integer> others = Arrays.asList(6, 5, 4, 3, 2, 1, 0);

        final List<Integer> integers = values.zip(others, Integer::compareTo).toList();

        assertEquals(Arrays.asList(-1, -1, -1, 0, 1, 1, 1), integers);
    }

    @Test
    void testZipWithNext() {
        final var sums = IntRange.of(0, 1_000)
                .filter(IntX.multipleOf(10))
                .onEach(i -> It.print(i + ", "))
                .boxed()
                .zipWithNext(Integer::sum)
                .toListX()
                .shuffled();

        It.println("\nsums = " + sums);

        It.println("sums.first() = " + sums.findFirst());
        It.println("sums.last() = " + sums.findLast());

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
        final var list = ListX.of(1, 2, 3, 4);

        final var map = list.asSequence()
                .associateWith(String::valueOf)
                .onEach(It::println)
                .mapValues(s -> StringX.of(s).first())
                .filterKeys(IntX::isEven)
                .onEachKey(It::println)
                .toMapX();

        assertEquals(2, map.size());
    }

    @Test
    void testEmpty() {
        final var list = Sequence.empty().toList();
        assertTrue(list.isEmpty());
    }

    @Test
    void testSequenceOfNullable() {
        assertTrue(Sequence.ofNullable(null).toList().isEmpty());
    }

    @Test
    void testSequenceFromStream() {
        final var stream = IntStream.range(0, 100).boxed();

        final var list = Sequence.of(stream)
                .filter(IntX::isEven)
                .sorted()
                .windowed(3, true)
                .flatMap(It::self)
                .withIndex()
                .toList();

        It.println("list = " + list);

        assertEquals(147, list.size());
    }

    @Test
    void testGeneratedSequenceCanBeConsumedMultipleTimes() {
        var leapYears = IntRange.from(1900).upTo(2000).step(2)
                .boxed()
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
                .takeWhile(date -> date.getYear() == year)
                .mapToInt(LocalDate::getDayOfMonth)
                .toListX();


        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");
        It.println(daysOfYear.joinToString());
        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        It.println("daysOfYear.min() = " + daysOfYear.min());

        It.println("daysOfYear.max() = " + daysOfYear.max());

        assertAll(
                () -> assertTrue(Year.of(year).isLeap()),
                () -> assertEquals(7, daysOfYear.filter(i -> i == 31).count()),
                () -> assertEquals(12, daysOfYear.filter(i -> i == 29).count()),
                () -> assertEquals(366, daysOfYear.count())
        );
    }

    @Test
    void testToSortedLocalDateTime() {
        final LocalDateTime initDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0);

        final var sorted = IntRange.of(0, 1_000)
                .mapToObj(initDateTime::plusDays)
                .sorted()
                .toListX();

        assertAll(
                () -> assertEquals(initDateTime, sorted.first()),
                () -> assertEquals(1_000, sorted.size())
        );
    }

    @Test
    void testSequenceOfZoneIds() {
        Instant now = Instant.now();
        ZonedDateTime current = now.atZone(ZoneId.systemDefault());
        It.printf("Current time is %s%n%n", current);

        final var noneWholeHourZoneOffsetSummaries = getTimeZoneSummaries(now, id -> nonWholeHourOffsets(now, id));

        noneWholeHourZoneOffsetSummaries.forEach(It::println);

        assertEquals(23, noneWholeHourZoneOffsetSummaries.count());
    }

    private boolean nonWholeHourOffsets(Instant instant, ZoneId id) {
        return instant.atZone(id).getOffset().getTotalSeconds() % 3600 != 0;
    }

    @Test
    void testTimeZonesAntarctica() {
        Instant now = Instant.now();
        ZonedDateTime current = now.atZone(ZoneId.systemDefault());
        It.printf("Current time is %s%n%n", current);

        final Sequence<String> timeZonesAntarctica = getTimeZoneSummaries(now, id -> id.getId().contains("Antarctica"));

        timeZonesAntarctica.forEach(It::println);

        assertEquals(12, timeZonesAntarctica.count());
    }

    private Sequence<String> getTimeZoneSummaries(Instant now, @NotNull Predicate<ZoneId> predicate) {
        return Sequence.of(ZoneId.getAvailableZoneIds())
                .map(ZoneId::of)
                .filter(predicate)
                .map(now::atZone)
                .sorted()
                .map(this::toZoneSummary);
    }

    private String toZoneSummary(ZonedDateTime zonedDateTime) {
        return String.format("%10s %-25s %10s", zonedDateTime.getOffset(), zonedDateTime.getZone(),
                zonedDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
    }

    @Test
    void testStepSequence() {
        final var intSequence = Sequence.generate(0, i -> ++i)
                .skip(4)
                .take(1_000)
                .step(200)
                .onEach(It::println)
                .mapToInt(It::asInt);

        final var integers = intSequence.toListX();
        final var sum = intSequence.sum();

        final var pair = intSequence.boxed()
                .foldTwo(IntMutableListX.empty(), IntMutableListX::plus, 0, Integer::sum);

        assertAll(
                () -> assertEquals(IntListX.of(4, 204, 404, 604, 804), integers),
                () -> assertEquals(pair.first(), integers),
                () -> assertEquals(2020, sum),
                () -> assertEquals(pair.second().longValue(), sum)
        );
    }

    @Test
    void testTakeWhileInclusiveZonedDateTime() {
        final var zonedDateTimes = Sequence
                .generate(ZonedDateTime.parse("2019-03-27T20:45:30+05:30[Asia/Calcutta]"), zdt -> zdt.plusHours(1))
                .takeWhileInclusive(d -> d.isBefore(ZonedDateTime.parse("2020-03-27T20:45:30+00:00[UTC]")))
                .toListX();

        final var expected = LocalDateTime.of(2020, Month.MARCH, 28, 2, 45, 30);

        assertAll(
                () -> assertEquals(8791, zonedDateTimes.size()),
                () -> assertEquals(expected, zonedDateTimes.last().toLocalDateTime())
        );
    }

    @Test
    void testGoldenRatioConvergenceBigDecimal() {
        final var scale = 200;
        final var sqrtOf5 = BigDecimal.valueOf(5).sqrt(MathContext.DECIMAL128);
        var goldenRatio = (BigDecimal.ONE.add(sqrtOf5)).divide(BigDecimal.valueOf(2), scale, RoundingMode.HALF_UP);

        It.println("goldenRatio by sqrt = " + goldenRatio);

        final var MAX_ITERATIONS = 10_000;

        final var approximations = IntSequence.generate(900, i -> ++i)
                .mapToObj(Generator::fibSumBd)
                .windowed(2)
                .map(w -> w.last().divide(w.first(), scale, RoundingMode.HALF_UP))
                .windowed(2)
                .takeWhileInclusive(approximation -> !approximation.first().equals(approximation.last()))
                .filter(ListX::isNotEmpty)
                .map(Reducable::last)
                .take(MAX_ITERATIONS)
                .toListX();

        final var actual = approximations.last();
        It.println("golden ratio by seq = " + actual);

        approximations.forEach(It::println);

        assertAll(
                () -> assertEquals(56, approximations.size()),
                () -> assertEquals(goldenRatio.setScale(30, RoundingMode.HALF_UP), actual.setScale(30, RoundingMode.HALF_UP))
        );
    }

    @Test
    void testFlatmapIterator() {
        System.setProperty("org.openjdk.java.util.stream.tripwire", "false");

        final var integers = IntRange.of(0, 1_000)
                .windowed(10)
                .map(IntListX::iterator)
                .flatMap(i -> () -> i)
                .toListX();

        It.println("integers = " + integers);

        System.setProperty("org.openjdk.java.util.stream.tripwire", "true");

        assertEquals(9910, integers.size());
    }
}
