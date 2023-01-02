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
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.SetX;
import org.hzt.utils.collectors.BigDecimalCollectors;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.numbers.IntX;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.sequences.primitives.IntSequence;
import org.hzt.utils.statistics.BigDecimalSummaryStatistics;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.test.Generator;
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
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;
import static org.hzt.utils.collectors.CollectorsX.intersectingBy;
import static org.hzt.utils.collectors.CollectorsX.toListX;
import static org.junit.jupiter.api.Assertions.*;

class IterableXTest {

    @Test
    void testMappingToSet() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .map(Museum::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        final var actual = ListX.of(museumList).toSetXOf(Museum::getDateOfOpening);

        assertEquals(expected, actual);
    }

    @Test
    void testFlatMapToSet() {
        var museumList = MutableListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expected = museumList.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(toSet());

        final var actual = museumList.flatMap(Museum::getPaintings).toSet();

        assertEquals(expected, actual);
    }

    @Test
    void testToNonNullKeyMap() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expectedMap = museumList.stream()
                .filter(m -> m.getName() != null)
                .collect(toMap(Museum::getName, Museum::getPaintings));

        final var actualMap = museumList.toMap(Museum::getName, Museum::getPaintings);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToLongArray() {
        final var longs = ListX.of(BigInteger.ONE, BigInteger.TWO, BigInteger.TEN)
                .toLongArray(BigInteger::longValue);

        assertArrayEquals(new long[]{1L, 2L, 10L}, longs);
    }

    @Test
    void testToNonNullKeyMapWithValues() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expectedMap = museumList.stream()
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
        final var museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var sumsOfThree = museums
                .flatMap(Museum::getPaintings)
                .indices()
                .boxed()
                .windowed(3, IntSequence::of)
                .onEach(It::println)
                .map(IntSequence::sum)
                .toListX();

        It.println("sumsOfThree = " + sumsOfThree);

        assertIterableEquals(List.of(3L, 6L, 9L, 12L, 15L, 18L, 21L), sumsOfThree);
    }

    @Test
    void testMapIndexed() {
        final var museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var sumsOfThree = museums
                .mapIndexed((index, value) -> index)
                .windowed(3, IntSequence::of)
                .map(IntSequence::sum);

        It.println("sumsOfThree = " + sumsOfThree);

        assertIterableEquals(List.of(3L, 6L), sumsOfThree);
    }

    @Test
    void testSortedIfOfComparableType() {
        var list = MutableListX.of(1, 3, 5, 4, 2, 7, 214, 5, 8, 3, 4, 123);
        final var sorted = list.sorted();
        assertIterableEquals(ListX.of(1, 2, 3, 3, 4, 4, 5, 5, 7, 8, 123, 214), sorted);
    }

    @Test
    void testSortedThrowsExceptionWhenNotOfComparableType() {
        var bankAccountList = ListX.of(TestSampleGenerator.createSampleBankAccountList());
        assertThrows(IllegalStateException.class, bankAccountList::sorted);
    }

    @Test
    void testMaxOf() {
        var bankAccountList = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = bankAccountList.stream()
                .max(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow();

        final var actual = bankAccountList.maxOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testMinOf() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = list.stream()
                .min(Comparator.comparing(BankAccount::getBalance))
                .map(BankAccount::getBalance)
                .orElseThrow();

        final var actual = list.minOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAuctionImplementingIterableX() {
        var auctions = Generator.createAuctions();

        final var auction = auctions.first();

        final var expected = auction.getPaintings().stream()
                .filter(painting -> !painting.isInMuseum())
                .findFirst()
                .orElseThrow();

        final var firstPaintingNotInMuseum = auction.firstNot(Painting::isInMuseum);

        assertEquals(firstPaintingNotInMuseum, expected);
    }

    @Test
    void testMinBy() {
        Set<BankAccount> bankAccounts = new HashSet<>(TestSampleGenerator.createSampleBankAccountList());

        final var expected = bankAccounts.stream().min(Comparator.comparing(BankAccount::getBalance));

        final var actual = ListX.of(bankAccounts).minBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testMaxBy() {
        var set = ListX.of(new HashSet<>(TestSampleGenerator.createSampleBankAccountList()));

        final var expected = set.stream().max(Comparator.comparing(BankAccount::getBalance));

        final var actual = set.maxBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testGrouping() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expectedMap = paintings.stream().collect(groupingBy(Painting::painter));

        final var actualMap = paintings.groupBy(Painting::painter);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testGroupMapping() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expectedMap = paintings.stream()
                .collect(groupingBy(Painting::painter, Collectors.mapping(Painting::getYearOfCreation, toList())));

        final var actualMap = ListX.of(paintings)
                .groupMapping(Painting::painter, Painting::getYearOfCreation);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testPartitionMapping() {
        final var paintings = TestSampleGenerator.createPaintingList();

        final var expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum,
                        mapping(Painting::age, toList())));

        final var actualMap = ListX.of(paintings)
                .partitionMapping(Painting::isInMuseum, Painting::age);

        assertAll(
                () -> assertEquals(expectedMap.get(true), actualMap.first()),
                () -> assertEquals(expectedMap.get(false), actualMap.second())
        );
    }

    @Test
    void testPartitioning() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum));

        final var actualMap = paintings.partition(Painting::isInMuseum);

        assertAll(
                () -> assertEquals(expectedMap.get(true), actualMap.first()),
                () -> assertEquals(expectedMap.get(false), actualMap.second())
        );
    }

    @Test
    void testToSummaryStatistics() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().mapToInt(Painting::ageInYears).summaryStatistics();

        final IntSummaryStatistics actual = paintings.intStatsOf(Painting::ageInYears);

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
        final var bankAccounts = ListX.of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final var expected = bankAccounts.stream()
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
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final var expectedPainters = museumList.stream()
                .flatMap(museum -> museum.getPaintings().stream())
                .filter(Objects::nonNull)
                .filter(Painting::isInMuseum)
                .map(Painting::painter)
                .collect(toList());

        final var actualPainters = museumList
                .flatMap(Museum::getPaintings)
                .filter(Painting::isInMuseum)
                .map(Painting::painter);

        assertIterableEquals(actualPainters, expectedPainters);
    }

    @Test
    void testMapToExistingCollection() {
        final var museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        Deque<LocalDate> deque = new ArrayDeque<>();
        deque.add(LocalDate.MAX);

        final var expectedLocalDates = museumList.stream()
                .map(Museum::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(toCollection(() -> deque));

        final var actualLocalDates = museumList.asSequence()
                .mapNotNull(Museum::getDateOfOpening)
                .mapTo(() -> deque, It::self);

        It.println("actualLocalDates = " + actualLocalDates);

        assertIterableEquals(expectedLocalDates, actualLocalDates);
    }

    @Test
    void testUnion() {
        var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        final var union = list.union(List.of(2, 3, 4, 5, 7));

        assertEquals(SetX.of(1, 2, 3, 4, 5, 6, 7, 10), union);
    }

    @Test
    void testFromIntArrayToMappedList() {
        final var numbers = new int[]{1, 4, 3, 6, 7, 4, 3, 234};

        final var expected = IntStream.of(numbers).mapToObj(BigDecimal::valueOf).collect(Collectors.toList());

        final var actual = IntSequence.of(numbers).boxed().toListOf(BigDecimal::valueOf);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testToArray() {
        final var paintings = MutableListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().map(Painting::getYearOfCreation).toArray(Year[]::new);

        final var actual = paintings.toArrayOf(Painting::getYearOfCreation, Year[]::new);

        assertArrayEquals(expected, actual);
    }

    @Test
    void testIntersectionOf() {
        final ListX<Collection<Integer>> collections = ListX.of(
                List.of(1, 2, 3, 4, 5, 7),
                List.of(2, 4, 5),
                List.of(4, 5, 6)
        );

        final var intersect = collections.intersectionOf(It::self);

        assertIterableEquals(List.of(4, 5), intersect);
    }

    @Test
    void testIntersect() {
        final var integers = ListX.of(1, 2, 3, 4, 5, 7);
        final var otherInts = List.of(1, 4, 5, 6);

        final var intersect = integers.intersect(otherInts);

        assertEquals(SetX.of(1, 4, 5), intersect);
    }

    @Test
    void testIntersectMuseumPaintings() {
        final var museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        var expected = museums.stream()
                .map(Museum::getPaintings)
                .collect(intersectingBy(Painting::getMillenniumOfCreation));

        final var intersection = museums.intersectionOf(Museum::getPaintings, Painting::getMillenniumOfCreation);

        It.println("intersection = " + intersection);

        assertEquals(expected, intersection);
    }

    @Test
    void testSumBigDecimals() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = list.bigDecimalSumOf(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testFold() {
        final var integers = ListX.of(1, 3, 3, 4, 5, 2, 6).toSetX();

        final var expectedSum = integers.stream()
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var sum = integers
                .fold(BigDecimal.ZERO, (bigDecimal, integer) -> bigDecimal.add(BigDecimal.valueOf(integer)));

        It.println("sum = " + sum);

        assertEquals(expectedSum, sum);
    }

    @Test
    void zipWithNextCanHandleSameParameterValues() {
        final var inputList = ListX.of(1, 1, 1, 2, 3, 2, 2, 2, 2);
        final var inputSequence = Sequence.of(1, 1, 1, 2, 3, 2, 2, 2, 2);

        final var zippedList = inputList.zipWithNext().toListXOf(Pair::of);
        final var zippedSequence = inputSequence.zipWithNext().toListXOf(Pair::of);

        zippedSequence.forEach(It::println);

        assertAll(
                () -> assertEquals(inputList.size() - 1, zippedList.size()),
                () -> assertEquals(zippedList, zippedSequence)
        );

    }

    @Test
    void testFoldRight() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        list.forEach(It::println);

        final var bigDecimal = list.foldRight(BigDecimal.ZERO, (b, a) -> a.add(b.getBalance()));

        assertEquals(BigDecimal.valueOf(232511.34), bigDecimal);
    }

    @Test
    void testDropLastWhile() {
        var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final var integers = list.skipLastWhile(i -> i != 10);

        It.println("integers = " + integers);

        assertIterableEquals(List.of(1, 2, 10, 4, 5, 10), integers);
    }

    @Test
    void testSkipWhile() {
        var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> integers = list.skipWhile(i -> i != 5);

        It.println("integers = " + integers);

        assertIterableEquals(List.of(5, 10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testSkipWhileInclusive() {
        var list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> integers = list.skipWhileInclusive(i -> i != 5);

        It.println("integers = " + integers);

        assertIterableEquals(List.of(10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testListIteratorGetPreviousOnlyWorksBeforeWhenIsAtEnd() {
        var list = List.of(22, 44, 88, 11, 33);
        var listIterator = list.listIterator();

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
        var bankAccounts = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = bankAccounts.stream().filter(BankAccount::isDutchAccount).count();

        final var actual = bankAccounts.filter(Objects::nonNull).count(BankAccount::isDutchAccount);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOf() {
        var list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = list.bigDecimalSumOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOfInt() {
        var list = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = list.stream().mapToInt(Painting::ageInYears).sum();

        final var actual = list.intSumOf(Painting::ageInYears);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAverageOf() {
        var list = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = list.stream().mapToInt(Painting::ageInYears).average().orElseThrow();

        final var actual = list.averageOf(Painting::ageInYears);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testIterXOfIterable() {
        var listX = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final var expected = listX.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = ListX.of(listX).bigDecimalSumOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testTransformForEach() {
        final var ints = IntStream.range(0, 100_000).toArray();

        IntSequence.of(ints)
                .filter(IntX::isEven)
                .onEach(this::printEvery10_000stElement)
                .forEachInt(i -> assertTrue(IntX.isEven(i)));
    }

    @Test
    void testFirst() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.first();

        final var actual = paintings.stream()
                .findFirst()
                .orElseThrow();

        assertEquals(expected, actual);
    }

    @Test
    void testLast() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.last();

        final var actual = paintings.get(paintings.size() - 1);

        assertEquals(expected, actual);
    }

    @Test
    void testFindLast() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var actual = paintings.findLast(painting -> !painting.isInMuseum());

        It.println("actual = " + actual);

        assertEquals("Lentetuin, de pastorietuin te Nuenen in het voorjaar", actual
                .map(Painting::name)
                .orElseThrow());
    }

    @Test
    void testAny() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().anyMatch(Painting::isInMuseum);

        final var actual = paintings.any(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testJoinToString() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream()
                .map(Painting::age)
                .map(Period::toString)
                .collect(Collectors.joining(", "));

        final var actual = paintings.joinToStringBy(Painting::age, ", ");

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testToString() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = "[" +
                "Painting[name=Lentetuin, de pastorietuin te Nuenen in het voorjaar, " +
                "painter=Painter{firstName='Vincent', lastname='van Gogh', dateOfBirth=1853-03-20}, " +
                "yearOfCreation=1884, isInMuseum=false]]";

        final var actual = paintings.filterNot(Painting::isInMuseum);

        assertEquals(expected, actual.toString());
    }

    @Test
    void testSkippingToSet() {
        final var bookList = ListX.of(TestSampleGenerator.createBookList());

        final var expected = bookList.stream()
                .filter(book -> !book.isAboutProgramming())
                .collect(CollectorsX.toSetX());

        final var actual = bookList.filterNot(Book::isAboutProgramming).toSetX();

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testCountingByNullableValue() {
        final var bookList = ListX.of(TestSampleGenerator.createBookList());

        final var expected = bookList.stream()
                .map(Book::getCategory)
                .filter(Objects::nonNull)
                .count();

        final var actual = bookList.filterBy(Book::getCategory, Objects::nonNull).count();

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAll() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().allMatch(Painting::isInMuseum);

        final var actual = paintings.all(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testNone() {
        final var paintings = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintings.stream().noneMatch(Painting::isInMuseum);

        final var actual = paintings.none(Painting::isInMuseum);

        assertEquals(expected, actual);
    }

    @Test
    void testMapNotNullMaxOf() {
        var paintingList = ListX.of(TestSampleGenerator.createPaintingList());

        final var expected = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        final var max = paintingList.stream()
                .map(Painting::painter)
                .collect(toListX())
                .maxOf(Painter::getDateOfBirth);

        final var actual = paintingList
                .mapNotNull(Painting::painter)
                .maxOf(Painter::getDateOfBirth);

        assertAll(
                () -> assertEquals(expected, max),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testCollectingToMap() {
        var paintingList = TestSampleGenerator.createPaintingList();

        final var expected = paintingList.stream()
                .map(Painting::painter)
                .collect(Collectors.toMap(Painter::getDateOfBirth, Painter::getLastname, (a, b) -> a));

        final var expectedLocalDates = expected.keySet().stream()
                .flatMap(date -> date.datesUntil(LocalDate.of(2000, Month.JANUARY, 1)))
                .collect(toSet());

        final var actual = Sequence.of(paintingList)
                .map(Painting::painter)
                .toMapX(Painter::getDateOfBirth, Painter::getLastname);

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertTrue(expectedLocalDates.containsAll(actual.keySet()))
        );
    }

    @Test
    void testMapNotNull() {
        var bankAccounts = MutableListX
                .of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final var expected = bankAccounts.stream()
                .filter(Objects::nonNull)
                .map(BankAccount::getCustomer)
                .filter(Objects::nonNull)
                .map(Customer::getCustomerId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElseThrow();

        final var actual = bankAccounts
                .mapNotNull(BankAccount::getCustomer)
                .maxOf(Customer::getCustomerId);

        assertEquals(expected, actual);
    }

    @Test
    void testZipWithNext() {
        var museumList = ListX.of(TestSampleGenerator.createPaintingList());

        final var integers = museumList
                .mapNotNull(Painting::name)
                .zipWithNext(String::compareTo);

        assertIterableEquals(List.of(-5, 83, -1, 5, -5, 1, 8), integers);
    }

    @Test
    void testZipTwoCollections() {
        var values = ListX.of(0, 1, 2, 3, 4, 5, 6, 7);
        var others = List.of(6, 5, 4, 3, 2, 1, 0);

        final var integers = values.zip(others, Integer::compareTo);

        assertIterableEquals(List.of(-1, -1, -1, 0, 1, 1, 1), integers);
    }

    @Test
    void testToUnion() {
        var values = ListX.of(0, 1, 2, 1, 3, 4, 5, 6, 7);
        var other = List.of(6, 5, 4, 3, 2, 1, 0);

        final var union = values.union(other, String::valueOf);

        assertIterableEquals(List.of("0", "1", "2", "3", "4", "5", "6", "7"), union);
    }

    @Test
    void mapToStringX() {
        final var bookList = ListX.of(TestSampleGenerator.createBookList());

        final var expected = bookList.stream()
                .map(Book::getCategory)
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .collect(toList());


        final var actual = bookList
                .map(Book::getCategory)
                .map(StringX::of)
                .flatMap(StringX::toListX);

        It.println("stringXES = " + actual);

        assertIterableEquals(expected, actual);
    }

    private void printEvery10_000stElement(int i) {
        if (IntX.of(i).isMultipleOf(10_000)) {
            It.println(i);
        }
    }

    @Test
    void testLargeStream() {
        final var bigDecimals = IntStream.range(0, 100_000)
                .filter(IntX::isEven)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList());

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testDistinctBy() {
        final var bigDecimals = ListX.of(IntStream.range(0, 100_000)
                .filter(IntX::isEven)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList()));

        final var expected = IntStream.rangeClosed(0, 254)
                .filter(IntX::isEven)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList());

        final var list = bigDecimals.distinctBy(BigDecimal::byteValue);

        It.println("list = " + list);

        assertIterableEquals(expected, list);
    }

    @Test
    void testDistinct() {
        final var integers = ListX.of(1, 1, 2, 3, 2, 4, 5, 3, 5, 6);

        final var distinct = integers.distinct();

        assertEquals(ListX.of(1, 2, 3, 4, 5, 6), distinct);
    }

    @Test
    void castIfInstanceOf() {
        final ListX<Comparable<?>> list = ListX.of(3.0, 2, 4, 3, BigDecimal.valueOf(10), 5L, 'a', "String");

        final var integers = list.castIfInstanceOf(Integer.class);

        It.println("integers = " + integers);

        assertEquals(ListX.of(2, 4, 3), integers);
    }

    @Test
    void testCreateAnEmptyIterableX() {
        final var strings = ListX.<String>empty().to(ArrayDeque::new);
        assertTrue(strings.isEmpty());
    }

    @Test
    void testDifferenceBetweenIterableXAndSequence() {
        final var range = IntRange.of(0, 20).boxed().toListX();

        final var strings = range.asSequence()
                .filter(IntX::isEven)
                .map(Generator::printAndReturnAsString)
                .onEach(String::length, It::println)
                .takeWhileInclusive(s -> s.length() < 6)
                .toListX();

        assertEquals(6, strings.size());
    }

    @Test
    void testIteratorX() {
        final var expected = new ArrayList<String>();
        final var actual = new ArrayList<String>();
        final var strings = ListX.of("hello", "this", "is", "a", "test");

        strings.iterator().forEachRemaining(expected::add);
        final var stringIteratorX = strings.atomicIterator();
        //noinspection StatementWithEmptyBody
        while (stringIteratorX.tryAdvance(actual::add)) ;

        assertEquals(expected, actual);
    }

    @Test
    void testToBooleanArray() {
        final var sequence = Sequence.of("This", "is", "a", "test");

        final var booleans = sequence.toBooleanArray(s -> s.contains("i"));

        assertArrayEquals(new boolean[]{true, true, false, false}, booleans);
    }

    @Test
    void testScan() {
        final var list = ListX.of("This", "is", "a", "test");
        final var scan = list.scan(10, (lengths, s) -> lengths + s.length());
        final var sequenceScan = list.asSequence()
                .scan(10, (lengths, s) -> lengths + s.length())
                .toListX();

        assertAll(
                () -> assertEquals(ListX.of(10, 14, 16, 17, 21), scan),
                () -> assertEquals(scan, sequenceScan)
        );
    }

}
