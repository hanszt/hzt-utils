package org.hzt.utils.sequences;

import org.hzt.test.ReplaceCamelCaseBySentence;
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
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collections.primitives.IntList;
import org.hzt.utils.collections.primitives.IntMutableList;
import org.hzt.utils.iterables.IterableExtensions;
import org.hzt.utils.iterables.Numerable;
import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.numbers.LongX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.statistics.IntStatistics;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.test.Generator;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.setProperty;
import static org.hzt.test.Locales.testWithFixedLocale;
import static org.hzt.utils.It.print;
import static org.hzt.utils.It.printf;
import static org.hzt.utils.It.println;
import static org.hzt.utils.iterables.IterableExtensions.runningFold;
import static org.hzt.utils.iterables.IterableExtensions.windowed;
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
        final ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final Sequence<Integer> sequence = Sequence.of(list)
                .map(SequenceTest::lengthMappingNotCalledWhenNotConsumed);

        assertTrue(sequence.iterator()::hasNext);
    }

    private static int lengthMappingNotCalledWhenNotConsumed(final String s) {
        fail("Should only be called when consumed with terminal operation");
        return s.length();
    }

    @Test
    void testMapReduce() {
        final ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final double sum = list
                .map(String::length)
                .mapToDouble(Double::valueOf)
                .sum();

        assertEquals(17, sum);
    }

    @Test
    void testSimpleStreamWithFilterYieldsIteratorWithNext() {
        final ListX<Integer> list = ListX.of(1, 2, 3, 4, 5, 6);

        final Sequence<ListX<IndexedValue<Integer>>> sequence = Sequence.of(list)
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
        final ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final int sum = Sequence.of(list)
                .map(String::length)
                .filter(l -> l > 3)
                .reduce(Integer::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(9, sum);
    }

    @Test
    void testMapFilterReduce() {
        final ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final double sum = Sequence.of(list)
                .map(String::length)
                .map(Double::valueOf)
                .reduce(Double::sum)
                .orElseThrow(NoSuchElementException::new);

        assertEquals(17, sum);
    }

    @Test
    void testFilterIndexed() {
        final ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final long sum = Sequence.of(list)
                .map(String::length)
                .filterIndexed((index, length) -> length > 2 && IntX.isOdd(index))
                .longSumOf(It::self);

        assertEquals(6, sum);
    }

    @Test
    void testMapNotNull() {
        final ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final ListX<BigDecimal> balances = Sequence.of(list)
                .mapNotNull(BankAccount::getBalance)
                .toMutableList();

        assertFalse(balances.contains(null));
    }

    @Test
    void testMapIfPresent() {
        final ListX<Museum> list = ListX.of(TestSampleGenerator.createMuseumList());

        final List<LocalDate> dates = Sequence.of(list)
                .mapIfPresent(Museum::dateOfOpening)
                .toList();

        final List<LocalDate> expected = ListX.of("1992-04-02", "1940-01-23", "1965-08-04").toListOf(LocalDate::parse);

        assertAll(
                () -> assertIterableEquals(expected, dates),
                () -> assertEquals(4, list.size())
        );
    }

    @Test
    void testMapFilterReduceToIntArray() {
        final ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final int[] result = list
                .mapToInt(String::length)
                .filter(l -> l > 3)
                .toArray();

        assertArrayEquals(new int[]{5, 4}, result);
    }

    @Test
    void testMapFilterReduceToList() {
        final ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");
        final ListX<Integer> result = list.asSequence()
                .map(String::length)
                .filter(l -> l > 3)
                .toListX();

        assertEquals(ListX.of(5, 4), result);
    }

    @Test
    void testMapFilterReduceToSet() {
        final ListX<String> list = ListX.of("Hallo", "dit", "is", "een", "test");

        final SetX<Integer> result = list.asSequence()
                .map(String::length)
                .toSetX();

        assertEquals(SetX.of(2, 3, 4, 5), result);
    }

    @Test
    void testFlatMapToList() {
        final ListX<Iterable<String>> list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                new ArrayDeque<>(Collections.singleton("test")));

        //noinspection Convert2MethodRef
        final ListX<String> result = list.asSequence()
                .flatMap(t -> It.self(t))
                .filter(s -> s.length() > 3)
                .filterNot(String::isEmpty)
                .toListX();

        println("result = " + result);

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

        println("result = " + result);

        assertEquals(ListX.of("Hallo", "test"), result);
    }

    @Test
    void testMapMultiToIntList() {
        final ListX<CollectionX<String>> list = ListX.of(ListX.of("Hallo", "dit"), SetX.of("is", "een"),
                SetX.of("test"));

        final IntList result = list.asSequence()
                .map(strings -> strings.asSequence().mapToInt(String::length))
                .mapMultiToInt(IntSequence::forEachInt)
                .filter(length -> length > 3)
                .toList();

        println("result = " + result);

        assertEquals(IntList.of(5, 4), result);
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
        final IntList charInts = Sequence.of("hallo", "test")
                .map(String::chars)
                .flatMapToInt(s -> s::iterator)
                .toList();

        assertEquals(IntList.of(104, 97, 108, 108, 111, 116, 101, 115, 116), charInts);
    }

    @Test
    void testTransform() {
        final MapX<Integer, String> map = Sequence.of("hallo", "test")
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
        final ListX<Integer> strings = Sequence.iterate(0, i -> ++i)
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
        final AtomicInteger atomicInteger = new AtomicInteger();

        final List<Integer> integers = Sequence.generate(atomicInteger::getAndIncrement, i -> i + 1)
                .take(10)
                .toList();

        assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), integers);
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
        final ListX<String> strings = Sequence.iterate(0, i -> ++i)
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
        final ListX<String> strings = Sequence.iterate(1, i -> i + 2)
                .map(String::valueOf)
                .takeWhileInclusive(s -> !s.contains("0"))
                .onEach(It::println)
                .map(String::trim)
                .toListX();

        assertEquals(51, strings.size());
    }

    @Test
    void testSkipWhile() {
        final ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final List<Integer> integers = list.asSequence()
                .skipWhile(i -> i != 5)
                .toList();

        println("integers = " + integers);

        assertEquals(Arrays.asList(5, 10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testSkipWhileInclusive() {
        final ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final List<Integer> integers = list.asSequence()
                .skipWhileInclusive(i -> i != 5)
                .toList();

        println("integers = " + integers);

        assertEquals(Arrays.asList(10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testIterateRandomWithinBound() {
        final Random random = new Random(0);
        final IntList integers = IntList.of(1, 2, 3, 4, 5);

        final MapX<Integer, IntMutableList> group = IntSequence
                .generate(() -> integers.random(random))
                .take(1_000_000)
                .group();

        final IntList actual = group.values().mapToInt(IntList::size);

        assertAll(
                () -> assertEquals(integers.size(), group.size()),
                () -> assertEquals(IntList.of(200084, 200023, 200084, 199562, 200247), actual)
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

        assertEquals(Arrays.asList("ollah", "eoh", "si"), strings);
    }

    @Nested
    class WindowedSequenceTests {

        @Test
        void testGenerateWindowedThenMapMultiToList() {
            final MutableListX<ListX<Integer>> windows = MutableListX.empty();

            final ListX<Integer> result = Sequence.iterate(0, i -> ++i)
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
            final IntRange range = IntRange.of(0, 9);
            assertThrows(IllegalArgumentException.class, () -> range.windowed(-4));
        }

        @Test
        void testWindowedStepGreaterThanWindowSizeWithPartialWindow() {
            final ListX<ListX<Integer>> windows = IntRange.of(0, 98)
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
            final ListX<ListX<Integer>> windows = IntRange.of(0, 98)
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
            final ListX<ListX<Integer>> windows = IntRange.closed(0, 10)
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
            final ListX<ListX<Integer>> windows = IntRange.of(0, 9)
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
            final ListX<ListX<Integer>> windows = IntRange.of(0, 40)
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
            final ListX<ListX<Integer>> windows = IntSequence.of(0, 8)
                    .boxed()
                    .windowed(10)
                    .toListX();

            println("windows = " + windows);

            assertTrue(windows.isEmpty());
        }

        @Test
        void testSequenceWindowedTransformed() {
            final ListX<Integer> sizes = IntRange.of(0, 1_000)
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
        final Sequence<Integer> values = Sequence.of(0, 1, 2, 3, 4, 5, 6, 7, 4);
        final List<Integer> others = Arrays.asList(6, 5, 4, 3, 2, 1, 0);

        final List<Integer> integers = values.zip(others, Integer::compareTo).toList();

        assertEquals(Arrays.asList(-1, -1, -1, 0, 1, 1, 1), integers);
    }

    @Test
    void testZipWithNext() {
        final Random random = new Random(0);
        final ListX<Integer> sums = IntRange.of(0, 1_000)
                .filter(IntX.multipleOf(10))
                .onEach(i -> print(i + ", "))
                .boxed()
                .zipWithNext(Integer::sum)
                .toListX()
                .shuffled(random);

        println("\nsums = " + sums);

        println("sums.first() = " + sums.findFirst());
        println("sums.last() = " + sums.findLast());

        assertEquals(99, sums.size());
    }

    @Test
    void testSequenceOfMap() {
        final Map<Integer, String> map = MutableMapX.of(1, "a", 2, "b", 3, "c", 4, "d");

        final MapX<Integer, Character> entries = Sequence.ofMap(map)
                .mapByValues(s -> StringX.of(s).first())
                .filterValues(Character::isLetter)
                .filterKeys(IntX::isEven)
                .toMapX();

        assertEquals(2, entries.size());
    }

    @Test
    void testSequenceOfEntryIterable() {
        final MapX<Integer, String> map = MutableMapX.of(1, "a", 2, "b", 3, "c", 4, "d");

        final MapX<Integer, Character> entries = Sequence.of(map)
                .mapByValues(s -> StringX.of(s).first())
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
                .mapByValues(s -> StringX.of(s).first())
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

        //noinspection Convert2MethodRef
        final List<IndexedValue<Integer>> list = Sequence.of(stream::iterator)
                .filter(IntX::isEven)
                .sorted()
                .windowed(3, true)
                .flatMap(t -> It.self(t))
                .withIndex()
                .toList();

        println("list = " + list);

        assertEquals(147, list.size());
    }

    @Test
    void testGeneratedSequenceCanBeConsumedMultipleTimes() {
        final Sequence<Year> leapYears = IntRange.from(1900).upTo(2000).step(2)
                .mapToObj(Year::of)
                .filter(Year::isLeap);

        final Year first = leapYears.first();
        final Year last = leapYears.last();

        println("first = " + first);
        println("last = " + last);
        final IntStatistics stats = leapYears.intStatsOf(Year::getValue);

        assertAll(
                () -> assertEquals(Year.of(1904), first),
                () -> assertEquals(Year.of(2000), last),
                () -> assertEquals(48_800, stats.getSum())
        );
    }

    @Test
    void testSequenceCanBeConsumedMultipleTimes() {
        final Sequence<Year> names = Sequence.of(Arrays.asList(1, 2, 3, 4, 5, 3, -1, 6, 12))
                .onEach(It::println)
                .mapNotNull(Year::of)
                .sorted();

        final Year first = names.first();
        println("first = " + first);
        println("first = " + names.first());
        final List<Year> nameList = names.toList();
        final Year last = names.last();


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
            final Sequence<ListX<Integer>> windowedSequence = Sequence.of(Arrays.asList(1, 2, 3, 4, 5, 3, -1, 6, 12))
                    .onEach(It::println)
                    .filter(i -> i % 2 == 0)
                    .windowed(2)
                    .constrainOnce();

            final List<ListX<Integer>> windows = windowedSequence.toList();

            System.out.println("nameList = " + windows);

            final List<ListX<Integer>> expected = Arrays.asList(ListX.of(2, 4), ListX.of(4, 6), ListX.of(6, 12));

            assertAll(
                    () -> assertEquals(expected, windows),
                    () -> assertThrows(IllegalStateException.class, windowedSequence::first)
            );
        }

        @Test
        void testSequenceWithConstrainOnceMethodCanOnlyBeUsedOncAfterShortCircuitingOperations() {
            final Sequence<Integer> integers = Sequence.of(Arrays.asList(1, 2, 3, 4, 5, 3, -1, 6, 12))
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
            final Sequence<Integer> integers = Sequence.of(Arrays.asList(1, 2, 3, 4, 5, 3, -1, 6, 12))
                    .constrainOnce()
                    .filter(i -> i % 2 == 0);

            final String first = integers.firstOf(String::valueOf);

            System.out.println("first = " + first);

            assertAll(
                    () -> assertEquals("2", first),
                    () -> assertThrows(IllegalStateException.class, integers::toList)
            );
        }
    }

    @Test
    void testSequenceAsIntRange() {
        final int year = 2020;

        final IntList daysOfYear = Sequence
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

        final int[] ints1 = IntStream.of(6, 1, 456, 2)
                .peek(s -> orderCalledStream.add("first"))
                .peek(s -> orderCalledStream.add("pre-sort"))
                .sorted()
                .peek(s -> orderCalledStream.add("post-sort"))
                .toArray();

        final int[] ints2 = IntSequence.of(6, 1, 456, 2)
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
    void testIntersperseByPrevious() {
        final List<Integer> integers = Sequence.iterate(0, i -> --i)
                .take(10)
                .intersperse(i -> ++i)
                .toList();

        println("integers = " + integers);

        assertEquals(Arrays.asList(0, 1, -1, 0, -2, -1, -3, -2, -4, -3, -5, -4, -6, -5, -7, -6, -8, -7, -9), integers);
    }

    @Test
    void testIntersperseConstantValue() {
        final List<Integer> integers = Sequence.iterate(0, i -> --i)
                .take(10)
                .intersperse(5)
                .toList();

        println("integers = " + integers);

        assertEquals(Arrays.asList(0, 5, -1, 5, -2, 5, -3, 5, -4, 5, -5, 5, -6, 5, -7, 5, -8, 5, -9), integers);
    }

    @Test
    void testIntersperseVariable() {
        final List<Integer> integers = Sequence.iterate(0, i -> --i)
                .take(10)
                .intersperse(0, i -> i + 2)
                .toList();

        println("integers = " + integers);

        assertEquals(Arrays.asList(0, 0, -1, 2, -2, 4, -3, 6, -4, 8, -5, 10, -6, 12, -7, 14, -8, 16, -9), integers);
    }

    @Test
    void testIntersperseBySupplier() {
        @SuppressWarnings("squid:S5977") final Random random = new Random(0);

        final List<Integer> integers = Sequence.iterate(0, i -> --i)
                .take(10)
                .intersperse(() -> random.nextInt(20))
                .toList();

        println("integers = " + integers);

        assertEquals(Arrays.asList(0, 0, -1, 8, -2, 9, -3, 7, -4, 15, -5, 13, -6, 11, -7, 1, -8, 19, -9), integers);
    }

    @Test
    void testSequenceOfZoneIds() {
        final Instant now = Instant.parse("2023-02-02T23:43:24Z");
        final ZonedDateTime current = now.atZone(ZoneId.of("Europe/Amsterdam"));
        printf("Current time is %s%n%n", current);

        final Sequence<String> noneWholeHourZoneOffsetSummaries = getTimeZoneSummaries(now, id -> nonWholeHourOffsets(now, ZoneId.of(id)));

        noneWholeHourZoneOffsetSummaries.forEach(It::println);

        assertEquals(23, noneWholeHourZoneOffsetSummaries.count());
    }

    private boolean nonWholeHourOffsets(final Instant instant, final ZoneId id) {
        return instant.atZone(id).getOffset().getTotalSeconds() % 3600 != 0;
    }

    @Test
    void testTimeZonesAntarctica() {
        testWithFixedLocale(Locale.US, l -> {
            final Instant now = Instant.parse("2024-01-04T14:32:23Z");

            final ListX<String> timeZonesAntarctica = getTimeZoneSummaries(now, id -> id.contains("Antarctica")).toListX();

            final ListX<String> expected = ListX.of(
                    "    -03:00 Antarctica/Palmer           11:32 AM",
                    "    -03:00 Antarctica/Rothera          11:32 AM",
                    "         Z Antarctica/Troll             2:32 PM",
                    "    +03:00 Antarctica/Syowa             5:32 PM",
                    "    +05:00 Antarctica/Mawson            7:32 PM",
                    "    +06:00 Antarctica/Vostok            8:32 PM",
                    "    +07:00 Antarctica/Davis             9:32 PM",
                    "    +10:00 Antarctica/DumontDUrville   12:32 AM",
                    "    +11:00 Antarctica/Casey             1:32 AM",
                    "    +11:00 Antarctica/Macquarie         1:32 AM",
                    "    +13:00 Antarctica/McMurdo           3:32 AM",
                    "    +13:00 Antarctica/South_Pole        3:32 AM"
            );
            assertEquals(expected, timeZonesAntarctica);
        });
    }

    private Sequence<String> getTimeZoneSummaries(final Instant now, final Predicate<String> predicate) {
        return Sequence.of(ZoneId.getAvailableZoneIds())
                .filter(predicate)
                .map(ZoneId::of)
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
        final IntSequence intSequence = Sequence.iterate(0, i -> ++i)
                .skip(4)
                .take(1_000)
                .step(200)
                .onEach(It::println)
                .mapToInt(It::asInt);

        final IntMutableList integers = intSequence.toMutableList();
        final long sum = intSequence.sum();

        final Pair<IntMutableList, Integer> pair = intSequence.boxed()
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
        final ListX<ZonedDateTime> zonedDateTimes = Sequence
                .iterate(ZonedDateTime.parse("2019-03-27T20:45:30+05:30[Asia/Calcutta]"), zdt -> zdt.plusHours(1))
                .takeWhileInclusive(d -> d.isBefore(ZonedDateTime.parse("2020-03-27T20:45:30+00:00[UTC]")))
                .toListX();

        final LocalDateTime expected = LocalDateTime.of(2020, Month.MARCH, 28, 2, 45, 30);

        assertAll(
                () -> assertEquals(8791, zonedDateTimes.size()),
                () -> assertEquals(expected, zonedDateTimes.last().toLocalDateTime())
        );
    }

    @Test
    void testGoldenRatioConvergenceBigDecimal() {
        final int scale = 200;
        final BigDecimal sqrtOf5 = BigDecimal.valueOf(Math.sqrt(5.0));
        final BigDecimal goldenRatio = (BigDecimal.ONE.add(sqrtOf5)).divide(BigDecimal.valueOf(2), scale, RoundingMode.HALF_UP);

        println("goldenRatio by sqrt = " + goldenRatio);

        final int MAX_ITERATIONS = 10_000;

        final ListX<BigDecimal> approximations = IntSequence.iterate(900, i -> ++i)
                .mapToObj(Generator::fibSumBd)
                .zipWithNext((cur, next) -> next.divide(cur, scale, RoundingMode.HALF_UP))
                .zipWithNext()
                .takeWhileInclusive(It::notEquals)
                .map(Map.Entry::getValue)
                .take(MAX_ITERATIONS)
                .toListX();

        final BigDecimal actual = approximations.last();
        println("golden ratio by seq = " + actual);

        approximations.forEach(It::println);

        assertAll(
                () -> assertEquals(56, approximations.size()),
                () -> assertEquals(goldenRatio.setScale(10, RoundingMode.HALF_UP), actual.setScale(10, RoundingMode.HALF_UP))
        );
    }

    @Test
    void testFlatmapIterator() {
        setProperty("org.openjdk.java.util.stream.tripwire", "false");

        final IntList integers = IntRange.of(0, 1_000)
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
        final Pair<List<Integer>, Long> pair = IntRange.of(0, 10)
                .boxed()
                .minus(2)
                .toTwo(Sequence::toList, Numerable::count);

        assertAll(
                () -> assertEquals(Arrays.asList(0, 1, 3, 4, 5, 6, 7, 8, 9), pair.first()),
                () -> assertEquals(9, pair.second())
        );
    }

    @Test
    void testSequenceMinusOtherIterable() {
        final List<Integer> intsToRemove = Arrays.asList(1, 34, 3, 5);

        final Pair<List<Integer>, Long> pair = IntRange.of(0, 10)
                .boxed()
                .minus(intsToRemove)
                .toTwo(Sequence::toList, Numerable::count);

        assertAll(
                () -> assertEquals(Arrays.asList(0, 2, 4, 6, 7, 8, 9), pair.first()),
                () -> assertEquals(7, pair.second())
        );
    }

    @Nested
    class SequenceFromNonIterableTests {

        @Test
        void testSequenceFromNonIterable() {
            final Nodes nodes = new Nodes("hello", "this", "is", "a", "test");

            final AtomicInteger index = new AtomicInteger();
            final AtomicIterator<String> atomicIterator = action -> {
                final boolean hasNext = index.get() < nodes.size();
                if (hasNext) {
                    action.accept(nodes.get(index.getAndIncrement()));
                }
                return hasNext;
            };

            final String[] strings = Sequence.of(atomicIterator::asIterator)
                    .filter(s -> !s.contains("e"))
                    .toTypedArray(String[]::new);

            assertAll(
                    () -> assertEquals(3, strings.length),
                    () -> assertArrayEquals(new String[]{"this", "is", "a"}, strings)
            );
        }

        private class Nodes {

            private final List<String> strings = new ArrayList<>();

            public Nodes(final String... strings) {
                this.strings.addAll(Arrays.asList(strings));
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
        final Nodes<String> nodes = new Nodes<>("This", "is", "a", "test");

        final List<String> strings = IntRange.of(0, nodes.size())
                .mapToObj(nodes::get)
                .toList();

        assertEquals(Arrays.asList("This", "is", "a", "test"), strings);
    }

    @Nested
    class ReverseSequenceTests {

        @Test
        void testReverseSequenceFromArray() {
            final String[] strings = {"This", "is", "a", "test"};

            final Sequence<String> sequence = Sequence.reverseOf(strings);

            final int[] ints = sequence
                    .skip(2)
                    .mapToInt(String::length)
                    .toArray();

            final String first = sequence.first();

            assertAll(
                    () -> assertArrayEquals(new int[]{2, 4}, ints),
                    () -> assertEquals("test", first)
            );
        }

        @Test
        void testReverseSequenceFromList() {
            final List<String> strings = Arrays.asList("This", "is", "a", "test");

            final Sequence<String> sequence = Sequence.reverseOf(strings);

            final int[] ints = sequence
                    .skip(2)
                    .mapToInt(String::length)
                    .toArray();

            final String first = sequence.first();

            System.out.println("first = " + first);

            assertAll(
                    () -> assertArrayEquals(new int[]{2, 4}, ints),
                    () -> assertEquals("test", first)
            );
        }

        @Test
        void testReverseSequenceFromListX() {
            final ListX<String> strings = ListX.of("This", "is", "a", "test");

            final Sequence<String> sequence = Sequence.reverseOf(strings);

            final int[] ints = sequence
                    .skip(2)
                    .mapToInt(String::length)
                    .toArray();

            final String first = sequence.first();

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
            final Sequence<Integer> scan = Sequence.<String>empty().scan(10, (lengths, s) -> lengths + s.length());

            assertIterableEquals(Sequence.of(10), scan);
        }

        @Test
        void testScan() {
            final List<Integer> integers = Sequence.iterate(1, i -> i * 2)
                    .scan(10, Integer::sum)
                    .take(20)
                    .toList();

            final List<Integer> expected = Arrays.asList(10, 11, 13, 17, 25, 41, 73, 137, 265, 521, 1033, 2057, 4105, 8201, 16393, 32777, 65545, 131081, 262153, 524297);
            assertEquals(expected, integers);
        }

        @Test
        void testScanIndexed() {
            final List<Integer> integers = Sequence.iterate(1, i -> i * 2)
                    .scanIndexed(10, ((index, acc, value) -> acc + index + value))
                    .take(6)
                    .toList();

            assertEquals(Arrays.asList(10, 11, 14, 20, 31, 51), integers);
        }

        /**
         * @param s        The nesting string
         * @param expected the expected depth
         * @see <a href="https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/">1614. Maximum Nesting Depth of the Parentheses</a>
         */
        @ParameterizedTest
        @CsvSource({
                "(1+(2*3)+((8)/4))+1, 3",
                "(1)+((2))+(((3))), 3"})
        void testMaximumNestingDepthUSingScan(final String s, final int expected) {
            final int actual = StringX.of(s)
                    .filter(StringX.of("()")::contains)
                    .mapToInt(c -> c == '(' ? 1 : -1)
                    .scan(0, Integer::sum)
                    .max();
            assertEquals(expected, actual);
        }
    }

    @Nested
    class ExtensionsTests {

        @Test
        void testWindowedExtension() {
            final List<List<Integer>> windows = Sequence.iterate(0, i -> i + 1)
                    .then(windowed(4, 4, true))
                    .take(10)
                    .toList();

            final List<List<Integer>> expected = Sequence.iterate(0, i -> i + 1)
                    .chunked(4)
                    .take(10)
                    .map(ListX::toList)
                    .toList();

            assertIterableEquals(expected, windows);
        }

        @Test
        void extendedExtension() {
            final List<Integer> windows = Sequence.iterate(0, i -> i + 1)
                    .then(IterableExtensions.<Integer>windowed(4, 4, true)
                            .andThen(runningFold(1, (acc, t) -> acc + t.size())))
                    .take(10)
                    .toList();

            final List<Integer> expected = Sequence.iterate(0, i -> i + 1)
                    .chunked(4)
                    .scan(1, (acc, t) -> acc + t.size())
                    .take(10)
                    .toList();

            assertIterableEquals(expected, windows);
        }

        @Test
        void collectFromExtensionChain() {
            final Collector<Integer, ?, Map<Integer, List<Integer>>> extension = IterableExtensions.<Integer>chunked(4)
                    .andThen(runningFold(1, (acc, t) -> acc + t.size()))
                    .collect(Collectors.groupingBy(i -> i % 4));

            final Map<Integer, List<Integer>> windows = Sequence.iterate(0, i -> i + 1)
                    .take(10)
                    .collect(extension);

            final Map<Integer, List<Integer>> expected = Sequence.iterate(0, i -> i + 1)
                    .take(10)
                    .chunked(4)
                    .scan(1, (acc, t) -> acc + t.size())
                    .collect(Collectors.groupingBy(i -> i % 4));

            assertEquals(expected, windows);
        }

        @Test
        void composedExtension() {
            final List<Integer> windows = Sequence.iterate(0, i -> i + 1)
                    .then(IterableExtensions.<List<Integer>, Integer>runningFold(1, (acc, t) -> acc + t.size())
                            .compose(windowed(4, 4, true)))
                    .take(10)
                    .toList();

            final List<Integer> expected = Sequence.iterate(0, i -> i + 1)
                    .chunked(4)
                    .scan(1, (acc, t) -> acc + t.size())
                    .take(10)
                    .toList();

            assertIterableEquals(expected, windows);
        }

    }

    @Nested
    class BuilderTests {

        @Test
        void testBuildSequence() {
            final Sequence.Builder<String> builder = Sequence.builder();

            builder.accept("This");
            final Sequence<String> strings = builder.add("is").add("a").add("test").build();

            assertIterableEquals(Arrays.asList("This", "is", "a", "test"), strings);
        }
    }
}
