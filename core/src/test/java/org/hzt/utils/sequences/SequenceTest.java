package org.hzt.utils.sequences;

import org.hzt.test.ReplaceCamelCaseBySentence;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.collections.CollectionX;
import org.hzt.utils.collections.LinkedSetX;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.iterables.Numerable;
import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.test.Generator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.System.setProperty;
import static org.hzt.utils.It.print;
import static org.hzt.utils.It.printf;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class SequenceTest {

    @Test
    void testSimpleStreamWithMapYieldsIteratorWithNext() {
        final var list = ListX.of("Hallo", "dit", "is", "een", "test");

        final var sequence = Sequence.of(list)
                .map(SequenceTest::lengthMappingNotCalledWhenNotConsumed);

        assertTrue(sequence.iterator()::hasNext);
    }

    private static int lengthMappingNotCalledWhenNotConsumed(final String s) {
        fail("Should only be called when consumed with terminal operation");
        return s.length();
    }

    @Test
    void testMapReduce() {
        final var list = ListX.of("Hallo", "dit", "is", "een", "test");

        final var sum = list
                .map(String::length)
                .mapToDouble(Double::valueOf)
                .sum();

        assertEquals(17, sum);
    }

    @Test
    void testSimpleStreamWithFilterYieldsIteratorWithNext() {
        final var list = ListX.of(1, 2, 3, 4, 5, 6);

        final var sequence = Sequence.of(list)
                .filter(SequenceTest::filterNotCalledWhenNotConsumed)
                .withIndex()
                .windowed(4);

        println(sequence);

        assertNotNull(sequence);
    }

    private static boolean filterNotCalledWhenNotConsumed(final int i) {
        fail("Should only be called when consumed with terminal operation");
        return i < 3;
    }

    @Test
    void testFilterReduce() {
        final var list = ListX.of("Hallo", "dit", "is", "een", "test");
        final int sum = Sequence.of(list)
                .map(String::length)
                .filter(l -> l > 3)
                .reduce(Integer::sum)
                .orElseThrow();

        assertEquals(9, sum);
    }

    @Test
    void testMapFilterReduce() {
        final var list = ListX.of("Hallo", "dit", "is", "een", "test");
        final double sum = Sequence.of(list)
                .map(String::length)
                .map(Double::valueOf)
                .reduce(Double::sum)
                .orElseThrow();

        assertEquals(17, sum);
    }

    @Test
    void testFilterIndexed() {
        final var list = ListX.of("Hallo", "dit", "is", "een", "test");

        final var sum = Sequence.of(list)
                .map(String::length)
                .filterIndexed((index, length) -> length > 2 && IntX.isOdd(index))
                .longSumOf(It::self);

        assertEquals(6, sum);
    }

    @Test
    void testMapNotNull() {
        final var list = ListX.of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final var balances = Sequence.of(list)
                .mapNotNull(BankAccount::getBalance)
                .toMutableList();

        assertFalse(balances.contains(null));
    }

    @Test
    void testMapIfPresent() {
        final var list = ListX.of(TestSampleGenerator.createMuseumList());

        final var dates = Sequence.of(list)
                .mapIfPresent(Museum::dateOfOpening)
                .toList();

        final var expected = ListX.of("1992-04-02", "1940-01-23", "1965-08-04").toListOf(LocalDate::parse);

        assertAll(
                () -> assertEquals(expected, dates),
                () -> assertEquals(4, list.size())
        );
    }

    @Test
    void testMapFilterReduceToIntArray() {
        final var list = ListX.of("Hallo", "dit", "is", "een", "test");

        final var result = list
                .mapToInt(String::length)
                .filter(l -> l > 3)
                .toArray();

        assertArrayEquals(new int[]{5, 4}, result);
    }

    @Test
    void testMapFilterReduceToList() {
        final var list = ListX.of("Hallo", "dit", "is", "een", "test");
        final var result = list.asSequence()
                .map(String::length)
                .filter(l -> l > 3)
                .toListX();

        assertEquals(ListX.of(5, 4), result);
    }

    @Test
    void testMapFilterReduceToSet() {
        final var list = ListX.of("Hallo", "dit", "is", "een", "test");

        final var result = list.asSequence()
                .map(String::length)
                .toSetX();

        assertEquals(SetX.of(2, 3, 4, 5), result);
    }

    @Test
    void testFlatMapToList() {
        final var list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        final var result = list.asSequence()
                .flatMap(It::self)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toListX();

        println("result = " + result);

        assertIterableEquals(ListX.of("Hallo", "test"), result);
    }

    @Test
    void testMapMultiToList() {
        final var list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        final var result = list.asSequence()
                .<String>mapMulti(Iterable::forEach)
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toListX();

        println("result = " + result);

        assertEquals(ListX.of("Hallo", "test"), result);
    }

    @Test
    void testMapMultiToIntList() {
        final ListX<CollectionX<String>> list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                SetX.of("test"));

        final var result = list.asSequence()
                .map(strings -> strings.asSequence().mapToInt(String::length))
                .mapMultiToInt(IntSequence::forEachInt)
                .filter(length -> length > 3)
                .toList();

        println("result = " + result);

        assertEquals(IntList.of(5, 4), result);
    }

    @Test
    void testCollectGroupingBy() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .flatMap(m -> m.getPaintings().stream())
                .collect(Collectors.groupingBy(Painting::painter));

        final var actual = Sequence.of(museumList)
                .flatMap(Museum::getPaintings)
                .collect(Collectors.groupingBy(Painting::painter));

        final var actual2 = Sequence.of(museumList)
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
                .flatMapToInt(s -> s::iterator)
                .toList();

        assertEquals(IntList.of(104, 97, 108, 108, 111, 116, 101, 115, 116), charInts);
    }

    @Test
    void testTransform() {
        final var map = Sequence.of("hallo", "test")
                .flatMapToInt(s -> s.chars()::iterator)
                .transform(this::toFilteredMapX);

        assertEquals(LinkedSetX.of(115, 116, 101, 104, 108, 111), map.keySet());
    }

    private MapX<Integer, String> toFilteredMapX(final IntSequence sequence) {
        return sequence.boxed()
                .associateWith(String::valueOf)
                .filterValues(s -> s.startsWith("1"))
                .toMapX();
    }

    @Test
    void testGenerateSequence() {
        final var strings = Sequence.iterate(0, i -> ++i)
                .map(Generator::fib)
                .filter(LongX::isOdd)
                .take(12)
                .map(Long::intValue)
                .onEach(It::println)
                .toListX();

        assertEquals(12, strings.size());
    }

    @Test
    void testGenerateWithSeedGenerator() {
        final var atomicInteger = new AtomicInteger();

        final var integers = Sequence.generate(atomicInteger::getAndIncrement, i -> i + 1)
                .take(10)
                .toList();

        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), integers);
    }

    @Test
    void testLargeSequence() {
        final var bigDecimals = IntRange.of(0, 100_000)
                .filter(IntX::isEven)
                .boxed()
                .map(BigDecimal::valueOf)
                .sortedDescending()
                .toListX();

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testTakeWhile() {
        final var strings = Sequence.iterate(0, i -> ++i)
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
        final var strings = Sequence.iterate(1, i -> i + 2)
                .map(String::valueOf)
                .takeWhileInclusive(s -> !s.contains("0"))
                .onEach(It::println)
                .map(String::trim)
                .toListX();

        assertEquals(51, strings.size());
    }

    @Test
    void testSkipWhile() {
        final var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final var integers = list.asSequence()
                .skipWhile(i -> i != 5)
                .toList();

        println("integers = " + integers);

        assertEquals(List.of(5, 10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testSkipWhileInclusive() {
        final var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final var integers = list.asSequence()
                .skipWhileInclusive(i -> i != 5)
                .toList();

        println("integers = " + integers);

        assertEquals(List.of(10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testIterateRandomWithinBound() {
        final var integers = IntList.of(1, 2, 3, 4, 5);

        final var group = IntSequence
                .generate(integers::random)
                .take(10_000_000)
                .group();

        group.values().forEach(IntList::size, It::println);

        assertAll(
                () -> assertEquals(integers.size(), group.size()),
                () -> group.values().forEach(IntList::isNotEmpty, Assertions::assertTrue)
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

        println("strings = " + strings);

        assertEquals(List.of("ollah", "eoh", "si"), strings);
    }

    @Nested
    class WindowedSequenceTests {

        @Test
        void testGenerateWindowedThenMapMultiToList() {
            final MutableListX<ListX<Integer>> windows = MutableListX.empty();

            final var result = Sequence.iterate(0, i -> ++i)
                    .windowed(8, 3)
                    .onEach(windows::add)
                    .takeWhile(s -> s.intSumOf(It::asInt) < 1_000_000)
                    .<Integer>mapMulti(Iterable::forEach)
                    .toListX();

            windows.filterIndexed((i, v) -> IntX.multipleOf(10_000).test(i)).forEach(It::println);

            println("windows.last() = " + windows.last());

            assertEquals(333328, result.size());
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

            println("windows = " + windows);

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

            println("windows = " + windows);

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

            println("windows = " + windows);

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

            println("windows = " + windows);

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

            println("windows = " + windows);

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

            println("windows = " + windows);

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

            println("sizes = " + sizes);

            println("windows.first() = " + sizes.findFirst());
            println("windows.last() = " + sizes.findLast());

            assertEquals(22, sizes.size());
        }
    }

    @Test
    void testZipSequenceWithIterable() {
        final var values = Sequence.of(0, 1, 2, 3, 4, 5, 6, 7, 4);
        final var others = List.of(6, 5, 4, 3, 2, 1, 0);

        final var integers = values.zip(others, Integer::compareTo).toList();

        assertEquals(List.of(-1, -1, -1, 0, 1, 1, 1), integers);
    }

    @Test
    void testZipWithNext() {
        final var sums = IntRange.of(0, 1_000)
                .filter(IntX.multipleOf(10))
                .onEach(i -> print(i + ", "))
                .boxed()
                .zipWithNext(Integer::sum)
                .toListX()
                .shuffled();

        println("\nsums = " + sums);

        println("sums.first() = " + sums.findFirst());
        println("sums.last() = " + sums.findLast());

        assertEquals(99, sums.size());
    }

    @Test
    void testSequenceOfMap() {
        final var map = Map.of(1, "a", 2, "b", 3, "c", 4, "d");

        final var mapX = Sequence.ofMap(map)
                .mapByValues(s -> StringX.of(s).first())
                .filterValues(Character::isLetter)
                .filterKeys(IntX::isEven)
                .toMapX();

        assertEquals(2, mapX.size());
    }

    @Test
    void testSequenceOfEntryIterable() {
        final var map = MapX.of(1, "a", 2, "b", 3, "c", 4, "d");

        final var mapX = Sequence.of(map)
                .mapByValues(s -> StringX.of(s).first())
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
                .mapByValues(s -> StringX.of(s).first())
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

        final var list = Sequence.of(stream::iterator)
                .filter(IntX::isEven)
                .sorted()
                .windowed(3, true)
                .flatMap(It::self)
                .withIndex()
                .toList();

        println("list = " + list);

        assertEquals(147, list.size());
    }

    @Test
    void testGeneratedSequenceCanBeConsumedMultipleTimes() {
        final var leapYears = IntRange.from(1900).upTo(2000).step(2)
                .mapToObj(Year::of)
                .filter(Year::isLeap);

        final var first = leapYears.first();
        final var last = leapYears.last();

        println("first = " + first);
        println("last = " + last);
        final var stats = leapYears.intStatsOf(Year::getValue);

        assertAll(
                () -> assertEquals(Year.of(1904), first),
                () -> assertEquals(Year.of(2000), last),
                () -> assertEquals(48_800, stats.getSum())
        );
    }

    @Test
    void testSequenceCanBeConsumedMultipleTimes() {
        final var names = Sequence.of(List.of(1, 2, 3, 4, 5, 3, -1, 6, 12))
                .onEach(It::println)
                .mapNotNull(Year::of)
                .sorted();

        final var first = names.first();
        println("first = " + first);
        println("first = " + names.first());
        final var nameList = names.toList();
        final var last = names.last();


        println("last = " + last);

        assertAll(
                () -> assertEquals(Year.of(-1), first),
                () -> assertEquals(Year.of(12), last),
                () -> assertEquals(9, nameList.size()),
                () -> assertEquals(3, names.windowed(2, 3).count())
        );
    }

    @Nested
    class ConstrainOnceTest {

        @Test
        void testSequenceWithConstrainOnceMethodCanOnlyBeUsedOnce() {
            final var windowedSequence = Sequence.of(List.of(1, 2, 3, 4, 5, 3, -1, 6, 12))
                    .onEach(It::println)
                    .filter(i -> i % 2 == 0)
                    .windowed(2)
                    .constrainOnce();

            final var windows = windowedSequence.toList();

            System.out.println("nameList = " + windows);

            final var expected = List.of(ListX.of(2, 4), ListX.of(4, 6), ListX.of(6, 12));

            assertAll(
                    () -> assertEquals(expected, windows),
                    () -> assertThrows(IllegalStateException.class, windowedSequence::first)
            );
        }

        @Test
        void testSequenceWithConstrainOnceMethodCanOnlyBeUsedOncAfterShortCircuitingOperations() {
            final var integers = Sequence.of(List.of(1, 2, 3, 4, 5, 3, -1, 6, 12))
                    .filter(i -> i % 2 == 0)
                    .constrainOnce();

            final int first = integers.first();

            System.out.println("first = " + first);

            assertAll(
                    () -> assertEquals(2, first),
                    () -> assertThrows(IllegalStateException.class, integers::first)
            );
        }

        @Test
        void testSequenceWithConstrainOnceMethodCanOnlyBeUsedOncAfterShortCircuitingOperationsAndThen() {
            final var integers = Sequence.of(List.of(1, 2, 3, 4, 5, 3, -1, 6, 12))
                    .constrainOnce()
                    .filter(i -> i % 2 == 0);

            final var first = integers.firstOf(String::valueOf);

            System.out.println("first = " + first);

            assertAll(
                    () -> assertEquals("2", first),
                    () -> assertThrows(IllegalStateException.class, integers::toList)
            );
        }
    }

    @Test
    void testSequenceAsIntRange() {
        final var year = 2020;

        final var daysOfYear = Sequence
                .iterate(LocalDate.of(year, Month.JANUARY, 1), date -> date.plusDays(1))
                .takeWhile(date -> date.getYear() == year)
                .mapToInt(LocalDate::getDayOfMonth)
                .toList();


        setProperty("org.openjdk.java.util.stream.tripwire", "false");
        println(daysOfYear.joinToString());
        setProperty("org.openjdk.java.util.stream.tripwire", "true");

        println("daysOfYear.min() = " + daysOfYear.min());

        println("daysOfYear.max() = " + daysOfYear.max());

        assertAll(
                () -> assertTrue(Year.of(year).isLeap()),
                () -> assertEquals(7, daysOfYear.filter(i -> i == 31).count()),
                () -> assertEquals(12, daysOfYear.filter(i -> i == 29).count()),
                () -> assertEquals(366, daysOfYear.count())
        );
    }

    @Test
    void testExecutionOrderSameForSequenceAndStreamWithSortedMethod() {
        final List<String> orderCalledStream = new ArrayList<>();
        final List<String> orderCalledSequence = new ArrayList<>();

        final var ints1 = IntStream.of(6, 1, 456, 2)
                .peek(s -> orderCalledStream.add("first"))
                .peek(s -> orderCalledStream.add("pre-sort"))
                .sorted()
                .peek(s -> orderCalledStream.add("post-sort"))
                .toArray();

        final var ints2 = IntSequence.of(6, 1, 456, 2)
                .onEach(s -> orderCalledSequence.add("first"))
                .onEach(s -> orderCalledSequence.add("pre-sort"))
                .sorted()
                .onEach(s -> orderCalledSequence.add("post-sort"))
                .toArray();

        orderCalledSequence.forEach(System.out::println);

        assertAll(
                () -> assertEquals(orderCalledSequence, orderCalledStream),
                () -> assertEquals(1, ints1[0]),
                () -> assertEquals(ints2[0], ints1[0])
        );
    }

    @Test
    void testToSortedLocalDateTime() {
        final var initDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0);

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
    void testIntersperseByPrevious() {
        final var integers = Sequence.iterate(0, i -> --i)
                .take(10)
                .intersperse(i -> ++i)
                .toList();

        println("integers = " + integers);

        assertEquals(List.of(0, 1, -1, 0, -2, -1, -3, -2, -4, -3, -5, -4, -6, -5, -7, -6, -8, -7, -9), integers);
    }

    @Test
    void testIntersperseConstantValue() {
        final var integers = Sequence.iterate(0, i -> --i)
                .take(10)
                .intersperse(5)
                .toList();

        println("integers = " + integers);

        assertEquals(List.of(0, 5, -1, 5, -2, 5, -3, 5, -4, 5, -5, 5, -6, 5, -7, 5, -8, 5, -9), integers);
    }

    @Test
    void testIntersperseVariable() {
        final var integers = Sequence.iterate(0, i -> --i)
                .take(10)
                .intersperse(0, i -> i + 2)
                .toList();

        println("integers = " + integers);

        assertEquals(List.of(0, 0, -1, 2, -2, 4, -3, 6, -4, 8, -5, 10, -6, 12, -7, 14, -8, 16, -9), integers);
    }

    @Test
    void testIntersperseBySupplier() {
        @SuppressWarnings("squid:S5977") final var random = new Random(0);

        final var integers = Sequence.iterate(0, i -> --i)
                .take(10)
                .intersperse(() -> random.nextInt(20))
                .toList();

        println("integers = " + integers);

        assertEquals(List.of(0, 0, -1, 8, -2, 9, -3, 7, -4, 15, -5, 13, -6, 11, -7, 1, -8, 19, -9), integers);
    }

    @Test
    void testSequenceOfZoneIds() {
        final var now = Instant.parse("2023-02-02T23:43:24Z");
        final var current = now.atZone(ZoneId.of("Europe/Amsterdam"));
        printf("Current time is %s%n%n", current);

        final var noneWholeHourZoneOffsetSummaries = getTimeZoneSummaries(now, id -> nonWholeHourOffsets(now, id));

        noneWholeHourZoneOffsetSummaries.forEach(It::println);

        assertEquals(23, noneWholeHourZoneOffsetSummaries.count());
    }

    private boolean nonWholeHourOffsets(final Instant instant, final ZoneId id) {
        return instant.atZone(id).getOffset().getTotalSeconds() % 3600 != 0;
    }

    @Test
    void testTimeZonesAntarctica() {
        final var now = Instant.parse("2022-10-23T13:34:43Z");
        final var current = now.atZone(ZoneId.of("Europe/Amsterdam"));
        printf("Current time is %s%n%n", current);

        final var timeZonesAntarctica = getTimeZoneSummaries(now, id -> id.getId().contains("Antarctica"));

        timeZonesAntarctica.forEach(It::println);

        assertEquals(12, timeZonesAntarctica.count());
    }

    private Sequence<String> getTimeZoneSummaries(final Instant now, @NotNull final Predicate<ZoneId> predicate) {
        return Sequence.of(ZoneId.getAvailableZoneIds())
                .map(ZoneId::of)
                .filter(predicate)
                .map(now::atZone)
                .sorted()
                .map(this::toZoneSummary);
    }

    private String toZoneSummary(final ZonedDateTime zonedDateTime) {
        return String.format("%10s %-25s %10s", zonedDateTime.getOffset(), zonedDateTime.getZone(),
                zonedDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
    }

    @Test
    void testStepSequence() {
        final var intSequence = Sequence.iterate(0, i -> ++i)
                .skip(4)
                .take(1_000)
                .step(200)
                .onEach(It::println)
                .mapToInt(It::asInt);

        final var integers = intSequence.toMutableList();
        final var sum = intSequence.sum();

        final var pair = intSequence.boxed()
                .foldTwo(IntMutableList.empty(), IntMutableList::plus, 0, Integer::sum);

        assertAll(
                () -> assertEquals(IntMutableList.of(4, 204, 404, 604, 804), integers),
                () -> assertEquals(pair.first(), integers),
                () -> assertEquals(2020, sum),
                () -> assertEquals(pair.second().longValue(), sum)
        );
    }

    @Test
    void testTakeWhileInclusiveZonedDateTime() {
        final var zonedDateTimes = Sequence
                .iterate(ZonedDateTime.parse("2019-03-27T20:45:30+05:30[Asia/Calcutta]"), zdt -> zdt.plusHours(1))
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
        final var goldenRatio = (BigDecimal.ONE.add(sqrtOf5)).divide(BigDecimal.valueOf(2), scale, RoundingMode.HALF_UP);

        println("goldenRatio by sqrt = " + goldenRatio);

        final var MAX_ITERATIONS = 10_000;

        final var approximations = IntSequence.iterate(900, i -> ++i)
                .mapToObj(Generator::fibSumBd)
                .zipWithNext((cur, next) -> next.divide(cur, scale, RoundingMode.HALF_UP))
                .zipWithNext()
                .takeWhileInclusive(It::notEquals)
                .map(Map.Entry::getValue)
                .take(MAX_ITERATIONS)
                .toListX();

        final var actual = approximations.last();
        println("golden ratio by seq = " + actual);

        approximations.forEach(It::println);

        assertAll(
                () -> assertEquals(56, approximations.size()),
                () -> assertEquals(goldenRatio.setScale(30, RoundingMode.HALF_UP), actual.setScale(30, RoundingMode.HALF_UP))
        );
    }

    @Test
    void testFlatmapIterator() {
        setProperty("org.openjdk.java.util.stream.tripwire", "false");

        final var integers = IntRange.of(0, 1_000)
                .windowed(10)
                .map(IntList::iterator)
                .flatMapToInt(i -> () -> i)
                .toList();

        println("integers = " + integers);

        setProperty("org.openjdk.java.util.stream.tripwire", "true");

        assertEquals(9910, integers.size());
    }

    @Test
    void testSequenceMinus() {
        final var pair = IntRange.of(0, 10)
                .boxed()
                .minus(2)
                .toTwo(Sequence::toList, Numerable::count);

        assertAll(
                () -> assertEquals(List.of(0, 1, 3, 4, 5, 6, 7, 8, 9), pair.first()),
                () -> assertEquals(9, pair.second())
        );
    }

    @Test
    void testSequenceMinusOtherIterable() {
        final var intsToRemove = List.of(1, 34, 3, 5);

        final var pair = IntRange.of(0, 10)
                .boxed()
                .minus(intsToRemove)
                .toTwo(Sequence::toList, Numerable::count);

        assertAll(
                () -> assertEquals(List.of(0, 2, 4, 6, 7, 8, 9), pair.first()),
                () -> assertEquals(7, pair.second())
        );
    }

    @Nested
    class SequenceFromNonIterableTests {

        @Test
        void testSequenceFromNonIterable() {
            final var nodes = new Nodes("hello", "this", "is", "a", "test");

            final var index = new AtomicInteger();
            final AtomicIterator<String> atomicIterator = action -> {
                final var hasNext = index.get() < nodes.size();
                if (hasNext) {
                    action.accept(nodes.get(index.getAndIncrement()));
                }
                return hasNext;
            };

            final var strings = Sequence.of(atomicIterator::asIterator)
                    .filter(s -> !s.contains("e"))
                    .toTypedArray(String[]::new);

            assertAll(
                    () -> assertEquals(3, strings.length),
                    () -> assertArrayEquals(new String[]{"this", "is", "a"}, strings)
            );
        }

        private static class Nodes {

            private final List<String> strings = new ArrayList<>();

            public Nodes(final String... strings) {
                this.strings.addAll(List.of(strings));
            }

            private int size() {
                return strings.size();
            }

            private String get(final int index) {
                return strings.get(index);
            }
        }
    }

    @Test
    void testSequenceFromIndexedDataStructure() {
        final var nodes = new Nodes<>("This", "is", "a", "test");

        final var strings = IntRange.of(0, nodes.size())
                .mapToObj(nodes::get)
                .toList();

        assertEquals(List.of("This", "is", "a", "test"), strings);
    }

    @Nested
    class ReverseSequenceTests {

        @Test
        void testReverseSequenceFromArray() {
            final var strings = new String[]{"This", "is", "a", "test"};

            final var sequence = Sequence.reverseOf(strings);

            final var ints = sequence
                    .skip(2)
                    .mapToInt(String::length)
                    .toArray();

            final var first = sequence.first();

            assertAll(
                    () -> assertArrayEquals(new int[]{2, 4}, ints),
                    () -> assertEquals("test", first)
            );
        }

        @Test
        void testReverseSequenceFromList() {
            final var strings = List.of("This", "is", "a", "test");

            final var sequence = Sequence.reverseOf(strings);

            final var ints = sequence
                    .skip(2)
                    .mapToInt(String::length)
                    .toArray();

            final var first = sequence.first();

            System.out.println("first = " + first);

            assertAll(
                    () -> assertArrayEquals(new int[]{2, 4}, ints),
                    () -> assertEquals("test", first)
            );
        }

        @Test
        void testReverseSequenceFromLinkedHashSet() {
            final var strings = new LinkedHashSet<>(List.of("This", "is", "a", "test"));

            final var sequence = Sequence.reverseOf(strings);

            final var ints = sequence
                    .skip(2)
                    .mapToInt(String::length)
                    .toArray();

            final var first = sequence.first();

            System.out.println("first = " + first);

            assertAll(
                    () -> assertArrayEquals(new int[]{2, 4}, ints),
                    () -> assertEquals("test", first)
            );
        }

        @Test
        void testReverseSequenceFromListX() {
            final var strings = ListX.of("This", "is", "a", "test");

            final var sequence = Sequence.reverseOf(strings);

            final var ints = sequence
                    .skip(2)
                    .mapToInt(String::length)
                    .toArray();

            final var first = sequence.first();

            System.out.println("first = " + first);

            assertAll(
                    () -> assertArrayEquals(new int[]{2, 4}, ints),
                    () -> assertEquals("test", first)
            );
        }
    }

    private static final class Nodes<T> {

        private final List<T> nodes = new ArrayList<>();

        @SafeVarargs
        public Nodes(final T... values) {
            Collections.addAll(nodes, values);
        }

        public int size() {
            return nodes.size();
        }

        public T get(final int index) {
            return nodes.get(index);
        }
    }

    @Nested
    class ScanTests {

        @Test
        void testScanOnEmptySequence() {
            final var scan = Sequence.<String>empty().scan(10, (lengths, s) -> lengths + s.length());

            assertIterableEquals(Sequence.of(10), scan);
        }

        @Test
        void testScan() {
            final var integers = Sequence.iterate(1, i -> i * 2)
                    .scan(10, Integer::sum)
                    .take(20)
                    .toList();

            final var expected = List.of(10, 11, 13, 17, 25, 41, 73, 137, 265, 521, 1033, 2057, 4105, 8201, 16393, 32777, 65545, 131081, 262153, 524297);
            assertEquals(expected, integers);
        }

        /**
         * @see <a href="https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/">1614. Maximum Nesting Depth of the Parentheses</a>
         * @param s The nesting string
         * @param expected the expected depth
         */
        @ParameterizedTest
        @CsvSource({
                "(1+(2*3)+((8)/4))+1, 3",
                "(1)+((2))+(((3))), 3"})
        void testMaximumNestingDepthUSingScan(final String s, final int expected) {
            final var actual = StringX.of(s)
                    .filter(StringX.of("()")::contains)
                    .mapToInt(c -> c == '(' ? 1 : -1)
                    .scan(0, Integer::sum)
                    .max();
            assertEquals(expected, actual);
        }
    }
}
