package org.hzt.utils.iterables;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Book;
import org.hzt.test.model.Customer;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collectors.BigDecimalCollectors;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.iterators.functional_iterator.AtomicIterator;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.statistics.BigDecimalSummaryStatistics;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.test.Generator;
import org.hzt.utils.test.model.PaintingAuction;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.hzt.utils.collectors.CollectorsX.intersectingBy;
import static org.hzt.utils.collectors.CollectorsX.toListX;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IterableXTest {

    @Test
    void testMappingToSet() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final Set<LocalDate> expected = museumList.stream()
                .map(Museum::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        final SetX<LocalDate> actual = ListX.of(museumList).toSetXOf(Museum::getDateOfOpening);

        assertEquals(expected, actual);
    }

    @Test
    void testFlatMapToSet() {
        final MutableListX<Museum> museumList = MutableListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Set<Painting> expected = museumList.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(toSet());

        final Set<Painting> actual = museumList.flatMap(Museum::getPaintings).toSet();

        assertEquals(expected, actual);
    }

    @Test
    void testToNonNullKeyMap() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Map<String, List<Painting>> expectedMap = museumList.stream()
                .filter(m -> m.getName() != null)
                .collect(toMap(Museum::getName, Museum::getPaintings));

        final Map<String, List<Painting>> actualMap = museumList.toMap(Museum::getName, Museum::getPaintings);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToLongArray() {
        final long[] longs = ListX.of(BigInteger.ONE, BigInteger.valueOf(2), BigInteger.TEN)
                .toLongArray(BigInteger::longValue);

        assertArrayEquals(new long[]{1L, 2L, 10L}, longs);
    }

    @Test
    void testToNonNullKeyMapWithValues() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Map<Museum, Painting> expectedMap = museumList.stream()
                .collect(toMap(It::self, Museum::getMostPopularPainting));

        final Map<Museum, Painting> actualMap = museumList.asSequence()
                .onEach(It::println)
                .associateWith(Museum::getMostPopularPainting)
                .onEach(It::println)
                .toMutableMap();

        It.println("expectedMap.size() = " + expectedMap.size());
        It.println("actualMap.size() = " + actualMap.size());

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testWithIndicesZipWithNext2() {
        final ListX<Museum> museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final ListX<Long> sumsOfThree = museums
                .flatMap(Museum::getPaintings)
                .indices()
                .boxed()
                .windowed(3, IntSequence::of)
                .onEach(It::println)
                .map(IntSequence::sum)
                .toListX();

        It.println("sumsOfThree = " + sumsOfThree);

        assertIterableEquals(Arrays.asList(3L, 6L, 9L, 12L, 15L, 18L, 21L), sumsOfThree);
    }

    @Test
    void testMapIndexed() {
        final ListX<Museum> museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final ListX<Long> sumsOfThree = museums
                .mapIndexed((index, value) -> index)
                .windowed(3, IntSequence::of)
                .map(IntSequence::sum);

        It.println("sumsOfThree = " + sumsOfThree);

        assertIterableEquals(Arrays.asList(3L, 6L), sumsOfThree);
    }

    @Test
    void testSortedIfOfComparableType() {
        final MutableListX<Integer> list = MutableListX.of(1, 3, 5, 4, 2, 7, 214, 5, 8, 3, 4, 123);
        final IterableX<Integer> sorted = list.sorted();
        assertIterableEquals(ListX.of(1, 2, 3, 3, 4, 4, 5, 5, 7, 8, 123, 214), sorted);
    }

    @Test
    void testSortedThrowsExceptionWhenNotOfComparableType() {
        final ListX<BankAccount> bankAccountList = ListX.of(TestSampleGenerator.createSampleBankAccountList());
        assertThrows(IllegalStateException.class, bankAccountList::sorted);
    }

    @Test
    void testMaxOf() {
        final ListX<BankAccount> bankAccountList = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final BigDecimal expected = bankAccountList.stream()
                .max(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow(NoSuchElementException::new);

        final BigDecimal actual = bankAccountList.maxOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testMinOf() {
        final ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final BigDecimal expected = list.stream()
                .min(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow(NoSuchElementException::new);

        final BigDecimal actual = list.minOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAuctionImplementingIterableX() {
        final MutableListX<PaintingAuction> auctions = Generator.createAuctions();

        final PaintingAuction auction = auctions.first();

        final Painting expected = auction.getPaintings().stream()
                .filter(painting -> !painting.isInMuseum())
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        final Painting firstPaintingNotInMuseum = auction.firstNot(Painting::isInMuseum);

        assertEquals(firstPaintingNotInMuseum, expected);
    }

    @Test
    void testMinBy() {
        final Set<BankAccount> bankAccounts = new HashSet<>(TestSampleGenerator.createSampleBankAccountList());

        final Optional<BankAccount> expected = bankAccounts.stream().min(Comparator.comparing(BankAccount::getBalance));

        final Optional<BankAccount> actual = ListX.of(bankAccounts).minBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testMaxBy() {
        final ListX<BankAccount> set = ListX.of(new HashSet<>(TestSampleGenerator.createSampleBankAccountList()));

        final Optional<BankAccount> expected = set.stream().max(Comparator.comparing(BankAccount::getBalance));

        final Optional<BankAccount> actual = set.maxBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testGrouping() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final Map<Painter, List<Painting>> expectedMap = paintings.stream().collect(groupingBy(Painting::painter));

        final MapX<Painter, MutableListX<Painting>> actualMap = paintings.groupBy(Painting::painter);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testGroupMapping() {
        final List<Painting> paintings = TestSampleGenerator.createPaintingList();

        final Map<Painter, List<Year>> expectedMap = paintings.stream()
                .collect(groupingBy(Painting::painter, Collectors.mapping(Painting::getYearOfCreation, toList())));

        final MapX<Painter, MutableListX<Year>> actualMap = ListX.of(paintings)
                .groupMapping(Painting::painter, Painting::getYearOfCreation);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testPartitionMapping() {
        final List<Painting> paintings = TestSampleGenerator.createPaintingList();
        final LocalDate now = LocalDate.of(2024, Month.JANUARY, 4);

        final Map<Boolean, List<Period>> expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum,
                        mapping(painting -> painting.age(now), toList())));

        final Pair<ListX<Period>, ListX<Period>> actualMap = ListX.of(paintings)
                .partitionMapping(Painting::isInMuseum, painting -> painting.age(now));

        assertAll(
                () -> assertEquals(expectedMap.get(true), actualMap.first()),
                () -> assertEquals(expectedMap.get(false), actualMap.second())
        );
    }

    @Test
    void testPartitioning() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final Map<Boolean, List<Painting>> expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum));

        final Pair<ListX<Painting>, ListX<Painting>> actualMap = paintings.partition(Painting::isInMuseum);

        assertAll(
                () -> assertEquals(expectedMap.get(true), actualMap.first()),
                () -> assertEquals(expectedMap.get(false), actualMap.second())
        );
    }

    @Test
    void testToSummaryStatistics() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());
        final int currentYear = 2024;

        final IntSummaryStatistics expected = paintings.stream().mapToInt(painting -> painting.ageInYears(currentYear)).summaryStatistics();

        final IntSummaryStatistics actual = paintings.intStatsOf(painting -> painting.ageInYears(currentYear));

        assertAll(
                () -> assertEquals(expected.getMin(), actual.getMin()),
                () -> assertEquals(expected.getMax(), actual.getMax()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getCount(), actual.getCount())
        );
    }

    @Test
    void testToBigDecimalSummaryStatistics() {
        final ListX<BankAccount> bankAccounts = ListX.of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final BigDecimalSummaryStatistics expected = bankAccounts.stream()
                .filter(Objects::nonNull)
                .map(BankAccount::getBalance)
                .collect(BigDecimalCollectors.summarizingBigDecimal());

        final BigDecimalSummaryStatistics actual = bankAccounts.bigDecimalStatsOf(BankAccount::getBalance);

        assertAll(
                () -> assertEquals(expected.getMin(), actual.getMin()),
                () -> assertEquals(expected.getMax(), actual.getMax()),
                () -> assertEquals(expected.getAverage(), actual.getAverage()),
                () -> assertEquals(expected.getSum(), actual.getSum()),
                () -> assertEquals(expected.getCount(), actual.getCount())
        );
    }

    @Test
    void testFlatMapFiltersNullsFilterAndMapToList() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Painter> expectedPainters = museumList.stream()
                .flatMap(museum -> museum.getPaintings().stream())
                .filter(Objects::nonNull)
                .filter(Painting::isInMuseum)
                .map(Painting::painter)
                .collect(toList());

        final ListX<Painter> actualPainters = museumList
                .flatMap(Museum::getPaintings)
                .filter(Painting::isInMuseum)
                .map(Painting::painter);

        assertIterableEquals(actualPainters, expectedPainters);
    }

    @Test
    void testMapToExistingCollection() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Deque<LocalDate> deque = new ArrayDeque<>();
        deque.add(LocalDate.MAX);

        final Deque<LocalDate> expectedLocalDates = museumList.stream()
                .map(Museum::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(toCollection(() -> deque));

        final Deque<LocalDate> actualLocalDates = museumList.asSequence()
                .mapNotNull(Museum::getDateOfOpening)
                .mapTo(() -> deque, It::self);

        It.println("actualLocalDates = " + actualLocalDates);

        assertIterableEquals(expectedLocalDates, actualLocalDates);
    }

    @Test
    void testUnion() {
        final ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final SetX<Integer> union = list.union(Arrays.asList(2, 3, 4, 5, 7));

        assertEquals(SetX.of(1, 2, 3, 4, 5, 6, 7, 10), union);
    }

    @Test
    void testFromIntArrayToMappedList() {
        final int[] numbers = {1, 4, 3, 6, 7, 4, 3, 234};

        final List<BigDecimal> expected = IntStream.of(numbers).mapToObj(BigDecimal::valueOf).collect(Collectors.toList());

        final List<BigDecimal> actual = IntSequence.of(numbers).boxed().toListOf(BigDecimal::valueOf);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testToArray() {
        final MutableListX<Painting> paintings = MutableListX.of(TestSampleGenerator.createPaintingList());

        final Year[] expected = paintings.stream().map(Painting::getYearOfCreation).toArray(Year[]::new);

        final Year[] actual = paintings.toArrayOf(Painting::getYearOfCreation, Year[]::new);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testIntersectionOf() {
        final ListX<Collection<Integer>> collections = ListX.of(
                Arrays.asList(1, 2, 3, 4, 5, 7),
                Arrays.asList(2, 4, 5),
                Arrays.asList(4, 5, 6)
        );

        final SetX<Integer> intersect = collections.intersectionOf(It::self);

        assertIterableEquals(Arrays.asList(4, 5), intersect);
    }

    @Test
    void testIntersect() {
        final ListX<Integer> integers = ListX.of(1, 2, 3, 4, 5, 7);
        final List<Integer> otherInts = Arrays.asList(1, 4, 5, 6);

        final SetX<Integer> intersect = integers.intersect(otherInts);

        assertEquals(SetX.of(1, 4, 5), intersect);
    }

    @Test
    void testIntersectMuseumPaintings() {
        final ListX<Museum> museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Set<Period> expected = museums.stream()
                .map(Museum::getPaintings)
                .collect(intersectingBy(Painting::getMillenniumOfCreation));

        final SetX<Period> intersection = museums.intersectionOf(Museum::getPaintings, Painting::getMillenniumOfCreation);

        It.println("intersection = " + intersection);

        assertEquals(expected, intersection);
    }

    @Test
    void testSumBigDecimals() {
        final ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final BigDecimal expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = list.bigDecimalSumOf(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testFold() {
        final SetX<Integer> integers = ListX.of(1, 3, 3, 4, 5, 2, 6).toSetX();

        final BigDecimal expectedSum = integers.stream()
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal sum = integers
                .fold(BigDecimal.ZERO, (bigDecimal, integer) -> bigDecimal.add(BigDecimal.valueOf(integer)));

        It.println("sum = " + sum);

        assertEquals(expectedSum, sum);
    }

    @Test
    void zipWithNextCanHandleSameParameterValues() {
        final ListX<Integer> inputList = ListX.of(1, 1, 1, 2, 3, 2, 2, 2, 2);
        final Sequence<Integer> inputSequence = Sequence.of(1, 1, 1, 2, 3, 2, 2, 2, 2);

        final ListX<Pair<Integer, Integer>> zippedList = inputList.zipWithNext().toListXOf(Pair::of);
        final ListX<Pair<Integer, Integer>> zippedSequence = inputSequence.zipWithNext().toListXOf(Pair::of);

        zippedSequence.forEach(It::println);

        assertAll(
                () -> assertEquals(inputList.size() - 1, zippedList.size()),
                () -> assertEquals(zippedList, zippedSequence)
        );

    }

    @Test
    void testFoldRight() {
        final ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        list.forEach(It::println);

        final BigDecimal bigDecimal = list.foldRight(BigDecimal.ZERO, (b, a) -> a.add(b.getBalance()));

        assertEquals(BigDecimal.valueOf(232511.34), bigDecimal);
    }

    @Test
    void testDropLastWhile() {
        final ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final ListX<Integer> integers = list.skipLastWhile(i -> i != 10);

        It.println("integers = " + integers);

        assertIterableEquals(Arrays.asList(1, 2, 10, 4, 5, 10), integers);
    }

    @Test
    void testSkipWhile() {
        final ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> integers = list.skipWhile(i -> i != 5);

        It.println("integers = " + integers);

        assertIterableEquals(Arrays.asList(5, 10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testSkipWhileInclusive() {
        final ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> integers = list.skipWhileInclusive(i -> i != 5);

        It.println("integers = " + integers);

        assertIterableEquals(Arrays.asList(10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testListIteratorGetPreviousOnlyWorksBeforeWhenIsAtEnd() {
        final List<Integer> list = Arrays.asList(22, 44, 88, 11, 33);
        final ListIterator<Integer> listIterator = list.listIterator();

        assertFalse(listIterator.hasPrevious());

        It.println("In actual order :");
        while (listIterator.hasNext()) {
            It.println(listIterator.next());
        }

        assertTrue(listIterator.hasPrevious());

        It.println("In reverse order :");
        while (listIterator.hasPrevious()) {
            It.println(listIterator.previous());
        }
    }

    @Test
    void testCountBy() {
        final ListX<BankAccount> bankAccounts = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final long expected = bankAccounts.stream().filter(BankAccount::isDutchAccount).count();

        final long actual = bankAccounts.filter(Objects::nonNull).count(BankAccount::isDutchAccount);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOf() {
        final ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final BigDecimal expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = list.bigDecimalSumOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOfInt() {
        final ListX<Painting> list = ListX.of(TestSampleGenerator.createPaintingList());
        final int currentYear = 2024;

        final int expected = list.stream().mapToInt(painting -> painting.ageInYears(currentYear)).sum();

        final long actual = list.intSumOf(painting -> painting.ageInYears(currentYear));

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAverageOf() {
        final ListX<Painting> list = ListX.of(TestSampleGenerator.createPaintingList());
        final int currentYear = 2024;

        final double expected = list.stream()
                .mapToInt(painting -> painting.ageInYears(currentYear))
                .average()
                .orElseThrow(NoSuchElementException::new);

        final double actual = list.averageOf(painting -> painting.ageInYears(currentYear));

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testIterXOfIterable() {
        final ListX<BankAccount> listX = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final BigDecimal expected = listX.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = ListX.of(listX).bigDecimalSumOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testTransformForEach() {
        final int[] ints = IntStream.range(0, 100_000).toArray();

        IntSequence.of(ints)
                .filter(IntX::isEven)
                .onEach(this::printEvery10_000stElement)
                .forEachInt(i -> assertTrue(IntX.isEven(i)));
    }

    @Test
    void testFirst() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final Painting expected = paintings.first();

        final Painting actual = paintings.stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        assertEquals(expected, actual);
    }

    @Test
    void testLast() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final Painting expected = paintings.last();

        final Painting actual = paintings.get(paintings.size() - 1);

        assertEquals(expected, actual);
    }

    @Test
    void testFindLast() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final Optional<Painting> actual = paintings.findLast(painting -> !painting.isInMuseum());

        It.println("actual = " + actual);

        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", actual
                .map(Painting::name)
                .orElseThrow(NoSuchElementException::new));
    }

    @Test
    void testAny() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final boolean expected = paintings.stream().anyMatch(Painting::isInMuseum);

        final boolean actual = paintings.any(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testJoinToString() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());
        final LocalDate now = LocalDate.of(2024, Month.JANUARY, 2);

        final String expected = paintings.stream()
                .map(painting -> painting.age(now))
                .map(Period::toString)
                .collect(Collectors.joining(", "));

        final String actual = paintings.joinToStringBy(painting -> painting.age(now), ", ");

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testToString() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final String expected = "[" +
                "Painting[name=Lentetuin, de pastorietuin te Nuenen in het voorjaar, " +
                "painter=Painter{firstName='Vincent', lastname='van Gogh', dateOfBirth=1853-03-20}, " +
                "yearOfCreation=1884, isInMuseum=false]]";

        final ListX<Painting> actual = paintings.filterNot(Painting::isInMuseum);

        assertEquals(expected, actual.toString());
    }

    @Test
    void testSkippingToSet() {
        final ListX<Book> bookList = ListX.of(TestSampleGenerator.createBookList());

        final SetX<Book> expected = bookList.stream()
                .filter(book -> !book.isAboutProgramming())
                .collect(CollectorsX.toSetX());

        final SetX<Book> actual = bookList.filterNot(Book::isAboutProgramming).toSetX();

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testCountingByNullableValue() {
        final ListX<Book> bookList = ListX.of(TestSampleGenerator.createBookList());

        final long expected = bookList.stream()
                .map(Book::getCategory)
                .filter(Objects::nonNull)
                .count();

        final long actual = bookList.filterBy(Book::getCategory, Objects::nonNull).count();

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAll() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final boolean expected = paintings.stream().allMatch(Painting::isInMuseum);

        final boolean actual = paintings.all(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testNone() {
        final ListX<Painting> paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final boolean expected = paintings.stream().noneMatch(Painting::isInMuseum);

        final boolean actual = paintings.none(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testMapNotNullMaxOf() {
        final ListX<Painting> paintingList = ListX.of(TestSampleGenerator.createPaintingList());

        final LocalDate expected = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .max(Comparator.naturalOrder())
                .orElseThrow(NoSuchElementException::new);

        final LocalDate max = paintingList.stream()
                .map(Painting::painter)
                .collect(toListX())
                .maxOf(Painter::getDateOfBirth);

        final LocalDate actual = paintingList
                .mapNotNull(Painting::painter)
                .maxOf(Painter::getDateOfBirth);

        assertAll(
                () -> assertEquals(expected, max),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testCollectingToMap() {
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final Map<LocalDate, String> expected = paintingList.stream()
                .map(Painting::painter)
                .collect(Collectors.toMap(Painter::getDateOfBirth, Painter::getLastname, (a, b) -> a));

        final MapX<LocalDate, String> actual =ListX.of(paintingList)
                .map(Painting::painter)
                .toMapX(Painter::getDateOfBirth, Painter::getLastname);

        assertEquals(expected, actual);
    }

    @Test
    void testMapNotNull() {
        final MutableListX<BankAccount> bankAccounts = MutableListX
                .of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final String expected = bankAccounts.stream()
                .filter(Objects::nonNull)
                .map(BankAccount::getCustomer)
                .filter(Objects::nonNull)
                .map(Customer::getCustomerId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElseThrow(NoSuchElementException::new);

        final String actual = bankAccounts
                .mapNotNull(BankAccount::getCustomer)
                .maxOf(Customer::getCustomerId);

        assertEquals(expected, actual);
    }

    @Test
    void testMapIfPresent() {
        final ListX<Museum> list = ListX.of(TestSampleGenerator.createMuseumList());

        final ListX<LocalDate> dates = list.mapIfPresent(Museum::dateOfOpening);

        final ListX<LocalDate> expected = ListX.of("1992-04-02", "1940-01-23", "1965-08-04").map(LocalDate::parse);

        assertAll(
                () -> assertEquals(expected, dates),
                () -> assertEquals(4, list.size())

        );
    }

    @Test
    void testZipWithNext() {
        final ListX<Painting> museumList = ListX.of(TestSampleGenerator.createPaintingList());

        final ListX<Integer> integers = museumList
                .mapNotNull(Painting::name)
                .zipWithNext(String::compareTo);

        assertIterableEquals(Arrays.asList(-5, 83, -1, 5, -5, 1, 8), integers);
    }

    @Test
    void testZipTwoCollections() {
        final ListX<Integer> values = ListX.of(0, 1, 2, 3, 4, 5, 6, 7);
        final List<Integer> others = Arrays.asList(6, 5, 4, 3, 2, 1, 0);

        final ListX<Integer> integers = values.zip(others, Integer::compareTo);

        assertIterableEquals(Arrays.asList(-1, -1, -1, 0, 1, 1, 1), integers);
    }

    @Test
    void testToUnion() {
        final ListX<Integer> values = ListX.of(0, 1, 2, 1, 3, 4, 5, 6, 7);
        final List<Integer> other = Arrays.asList(6, 5, 4, 3, 2, 1, 0);

        final SetX<String> union = values.union(other, String::valueOf);

        assertIterableEquals(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7"), union);
    }

    @Test
    void mapToStringX() {
        final ListX<Book> bookList = ListX.of(TestSampleGenerator.createBookList());

        final List<Character> expected = bookList.stream()
                .map(Book::getCategory)
                .flatMapToInt(IterableXTest::chars)
                .mapToObj(c -> (char) c)
                .collect(toList());


        final ListX<Character> actual = bookList
                .map(Book::getCategory)
                .map(StringX::of)
                .flatMap(StringX::toListX);

        It.println("stringXES = " + actual);

        assertIterableEquals(expected, actual);
    }

    private static IntStream chars(final String s) {
        return s.chars();
    }

    private void printEvery10_000stElement(final int i) {
        if (IntX.of(i).isMultipleOf(10_000)) {
            It.println(i);
        }
    }

    @Test
    void testLargeStream() {
        final List<BigDecimal> bigDecimals = IntStream.range(0, 100_000)
                .filter(IntX::isEven)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList());

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testDistinctBy() {
        final ListX<BigDecimal> bigDecimals = ListX.of(IntStream.range(0, 100_000)
                .filter(IntX::isEven)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList()));

        final List<BigDecimal> expected = IntStream.rangeClosed(0, 254)
                .filter(IntX::isEven)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList());

        final ListX<BigDecimal> list = bigDecimals.distinctBy(BigDecimal::byteValue);

        It.println("list = " + list);

        assertIterableEquals(expected, list);
    }

    @Test
    void testDistinct() {
        final ListX<Integer> integers = ListX.of(1, 1, 2, 3, 2, 4, 5, 3, 5, 6);

        final ListX<Integer> distinct = integers.distinct();

        assertEquals(ListX.of(1, 2, 3, 4, 5, 6), distinct);
    }

    @Test
    void castIfInstanceOf() {
        final ListX<Comparable<?>> list = ListX.of(3.0, 2, 4, 3, BigDecimal.valueOf(10), 5L, 'a', "String");

        final ListX<Integer> integers = list.castIfInstanceOf(Integer.class);

        It.println("integers = " + integers);

        assertEquals(ListX.of(2, 4, 3), integers);
    }

    @Test
    void testCreateAnEmptyIterableX() {
        final ArrayDeque<String> strings = ListX.<String>empty().to(ArrayDeque::new);
        assertTrue(strings.isEmpty());
    }

    @Test
    void testDifferenceBetweenIterableXAndSequence() {
        final ListX<Integer> range = IntRange.of(0, 20).boxed().toListX();

        final ListX<String> strings = range.asSequence()
                .filter(IntX::isEven)
                .map(Generator::toStringIn50Millis)
                .onEach(String::length, It::println)
                .takeWhileInclusive(s -> s.length() < 6)
                .toListX();

        assertEquals(6, strings.size());
    }

    @Test
    void testIteratorX() {
        final List<String> expected = new ArrayList<>();
        final List<String> actual = new ArrayList<>();
        final ListX<String> strings = ListX.of("hello", "this", "is", "a", "test");

        strings.iterator().forEachRemaining(expected::add);
        final AtomicIterator<String> stringIteratorX = strings.atomicIterator();
        //noinspection StatementWithEmptyBody
        while(stringIteratorX.tryAdvance(actual::add)) {
        }

        assertEquals(expected, actual);
    }

    @Test
    void testToBooleanArray() {
        final Sequence<String> sequence = Sequence.of("This", "is", "a", "test");

        final boolean[] booleans = sequence.toBooleanArray(s -> s.contains("i"));

        assertArrayEquals(new boolean[]{true, true, false, false}, booleans);
    }

}
