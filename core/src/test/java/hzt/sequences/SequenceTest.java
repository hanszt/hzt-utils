package hzt.sequences;

import hzt.collections.LinkedSetX;
import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.SetX;
import hzt.numbers.IntX;
import hzt.numbers.LongX;
import hzt.ranges.IntRange;
import hzt.sequences.primitives.IntSequence;
import hzt.statistics.IntStatistics;
import hzt.strings.StringX;
import hzt.test.Generator;
import hzt.tuples.IndexedValue;
import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
import java.util.NoSuchElementException;
import java.util.function.Predicate;
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

        final long sum = Sequence.of(list)
                .map(String::length)
                .filterIndexed((index, length) -> length > 2 && IntX.isOdd(index))
                .sumOfLongs(It::self);

        assertEquals(6, sum);
    }

    @Test
    void testMapNotNull() {
       ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final ListX<BigDecimal> sum = Sequence.of(list)
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
       ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final SetX<Integer> result = list.asSequence()
                .map(String::length)
                .toSetX();

        assertEquals(SetX.of(2, 3, 4, 5), result);
    }

    @Test
    void testFlatMapToList() {
        final ListX<Iterable<String>> list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        final ListX<String> result = list.asSequence()
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

        final ListX<String> result = list.asSequence()
                .<String>mapMulti(Iterable::forEach)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toListX();

        It.println("result = " + result);

        assertEquals(ListX.of("Hallo", "test"), result);
    }

    @Test
    void testGenerateWindowedThenMapMultiToList() {
        MutableListX<ListX<Integer>> windows = MutableListX.empty();

        final ListX<Integer> result = Sequence.generate(0, i -> ++i)
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
        final List<Integer> charInts = Sequence.of("hallo", "test")
                .map(String::chars)
                .flatMapStream(IntStream::boxed)
                .toList();

        assertEquals(Arrays.asList(104, 97, 108, 108, 111, 116, 101, 115, 116), charInts);
    }

    @Test
    void testTransform() {
        final MapX<Integer, String> map = Sequence.of("hallo", "test")
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
        final ListX<Integer> strings = Sequence.generate(0, i -> ++i)
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

        final List<Integer> integers = list.asSequence()
                .skipWhile(i -> i != 5)
                .toList();

        It.println("integers = " + integers);

        assertEquals(Arrays.asList(5, 10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testSkipWhileInclusive() {
        ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final List<Integer> integers = list.asSequence()
                .skipWhileInclusive(i -> i != 5)
                .toList();

        It.println("integers = " + integers);

        assertEquals(Arrays.asList(10, 6, 5, 3, 5, 6), integers);
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
        final Sequence<String> integers = Sequence.of("hallo", "hoe", "is", "het");

        final List<String> strings = integers
                .distinctBy(String::length)
                .map(StringX::of)
                .map(StringX::reversed)
                .toListOf(StringX::toString);

        It.println("strings = " + strings);

        assertEquals(Arrays.asList("ollah", "eoh", "si"), strings);
    }

    @Test
    void testWindowedThrowsExceptionWhenStepSizeNegative() {
        final IntRange range = IntRange.of(0, 9);
        assertThrows(IllegalArgumentException.class, () -> range.windowed(-4));
    }

    @Test
    void testWindowedStepGreaterThanWindowSizeWithPartialWindow() {
        final ListX<ListX<Integer>> windows = IntRange.of(0, 98)
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
        final ListX<ListX<Integer>> windows = IntRange.of(0, 98)
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
        final ListX<ListX<Integer>> windows = IntRange.closed(0, 10)
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
        final ListX<ListX<Integer>> windows = IntRange.of(0, 9)
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
        final ListX<ListX<Integer>> windows = IntRange.of(0, 40)
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
        final ListX<ListX<Integer>> windows = IntSequence.of(0, 8)
                .boxed()
                .windowed(10)
                .toListX();

        It.println("windows = " + windows);

        assertTrue(windows.isEmpty());
    }

    @Test
    void testWindowedLargeSequence() {
        final ListX<ListX<Integer>> windows = IntRange.of(0, 1_000_000)
                .boxed()
                .windowed(2_001, 23, true)
                .toListX();

        final ListX<Integer> lastWindow = windows.last();

        final ListX<ListX<Integer>> tail = windows.tailFrom(windows.size() - 2);

        It.println("tail = " + tail);

        assertAll(
                () -> assertEquals(43479, windows.size()),
                () -> assertEquals(999_994, lastWindow.first()),
                () -> assertEquals(999_999, lastWindow.last())
        );
    }

    @Test
    void testSequenceWindowedTransformed() {
        final ListX<Integer> sizes = IntRange.of(0, 1_000)
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
        final ListX<Integer> sums = IntRange.of(0, 1_000)
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
        final MapX<Integer, String> map = MapX.of(1, "a", 2, "b", 3, "c", 4, "d");

        final MapX<Integer, Character> entries = Sequence.of(map)
                .mapValues(s -> StringX.of(s).first())
                .filterValues(Character::isLetter)
                .filterKeys(IntX::isEven)
                .toMapX();

        assertEquals(2, entries.size());
    }

    @Test
    void testSequenceAssociateWith() {
        final ListX<Integer> list = ListX.of(1, 2, 3, 4);

        final MapX<Integer, Character> map = list.asSequence()
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
        final List<Object> list = Sequence.empty().toList();
        assertTrue(list.isEmpty());
    }

    @Test
    void testSequenceOfNullable() {
        assertTrue(Sequence.ofNullable(null).toList().isEmpty());
    }

    @Test
    void testSequenceFromStream() {
        final Stream<Integer> stream = IntStream.range(0, 100).boxed();

        final List<IndexedValue<Integer>> list = Sequence.of(stream)
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
        Sequence<Year> leapYears = IntRange.from(1900).upTo(2000).step(2)
                .boxed()
                .map(Year::of)
                .filter(Year::isLeap);

        final Year first = leapYears.first();
        final Year last = leapYears.last();

        It.println("first = " + first);
        It.println("last = " + last);
        final IntStatistics stats = leapYears.statsOfInts(Year::getValue);

        assertAll(
                () -> assertEquals(Year.of(1904), first),
                () -> assertEquals(Year.of(2000), last),
                () -> assertEquals(48800, stats.getSum())
        );
    }

    @Test
    void testSequenceCanBeConsumedMultipleTimes() {
        Sequence<Year> names = Sequence.of(Arrays.asList(1, 2, 3, 4, 5, 3, -1, 6, 12))
                .onEach(It::println)
                .mapNotNull(Year::of)
                .sorted();

        final Year first = names.first();
        It.println("first = " + first);
        It.println("first = " + names.first());
        final List<Year> nameList = names.toList();
        final Year last = names.last();


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

        final IntSequence daysOfYear = Sequence
                .generate(LocalDate.of(year, Month.JANUARY, 1), date -> date.plusDays(1))
                .takeWhile(date -> date.getYear() < year + 1)
                .mapToInt(LocalDate::getDayOfMonth);

        It.println(daysOfYear.joinToString());

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

        final ListX<LocalDateTime> sorted = IntRange.of(0, 1_000)
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
        System.out.printf("Current time is %s%n%n", current);

        final Sequence<String> noneWholeHourZoneOffsetSummaries = getTimeZoneSummaries(now, id -> nonWholeHourOffsets(now, id));

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
        System.out.printf("Current time is %s%n%n", current);

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
}
