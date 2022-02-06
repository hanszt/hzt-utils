package hzt.iterables;

import hzt.collections.ListX;
import hzt.collections.MapX;
import hzt.collections.MutableListX;
import hzt.collections.MutableMapX;
import hzt.collections.SetX;
import hzt.collectors.BigDecimalCollectors;
import hzt.ranges.IntRange;
import hzt.sequences.Sequence;
import hzt.statistics.IntStatistics;
import hzt.utils.It;
import hzt.statistics.BigDecimalSummaryStatistics;
import hzt.strings.StringX;
import hzt.test.Generator;
import hzt.test.model.PaintingAuction;
import hzt.tuples.Pair;
import hzt.tuples.Triple;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.hzt.test.model.Book;
import org.hzt.test.model.Customer;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static hzt.collectors.CollectorsX.branching;
import static hzt.collectors.CollectorsX.intersectingBy;
import static hzt.collectors.CollectorsX.toListX;
import static hzt.collectors.CollectorsX.toMapX;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

public class IterableXTest {

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
        MutableListX<Museum> museumList = MutableListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Set<Painting> expected = museumList.stream()
                .map(Museum::getPaintings)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(toSet());

        final SetX<Painting> actual = museumList.flatMapToSetXOf(Museum::getPaintings);

        assertEquals(expected, actual);
    }

    @Test
    void testToNonNullKeyMap() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Map<String, List<Painting>> expectedMap = museumList.stream()
                .filter(m -> m.getName() != null)
                .collect(toMap(Museum::getName, Museum::getPaintings));

        final MapX<String, List<Painting>> actualMap = museumList.toMapX(Museum::getName, Museum::getPaintings);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToNonNullKeyMapWithValues() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Map<Museum, Painting> expectedMap = museumList.stream()
                .collect(toMap(It::self, Museum::getMostPopularPainting));

        final MutableMapX<Museum, Painting> actualMap = museumList.associateWith(Museum::getMostPopularPainting);

        assertEquals(expectedMap, actualMap);
    }

    @Test
    void testWithIndicesZipWithNext2() {
        final ListX<Museum> museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final ListX<Integer> sumsOfThree = museums
                .flatMap(Museum::getPaintings)
                .indices()
                .zipWithNext2(IntStream::of)
                .toListXOf(IntStream::sum);

        It.println("sumsOfThree = " + sumsOfThree);

        assertEquals(Arrays.asList(3, 6, 9, 12, 15, 18, 21), sumsOfThree);
    }

    @Test
    void testMapIndexed() {
        final ListX<Museum> museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final ListX<Integer> sumsOfThree = museums
                .mapIndexed((index, value) -> index)
                .zipWithNext2(IntStream::of)
                .toListXOf(IntStream::sum);

        It.println("sumsOfThree = " + sumsOfThree);

        assertEquals(Arrays.asList(3, 6), sumsOfThree);
    }

    @Test
    void testSortedIfOfComparableType() {
        MutableListX<Integer> list = MutableListX.of(1, 3, 5, 4, 2, 7, 214, 5, 8, 3, 4, 123);
        final IterableX<Integer> sorted = list.sorted();
        assertIterableEquals(ListX.of(1, 2, 3, 3, 4, 4, 5, 5, 7, 8, 123, 214), sorted);
    }

    @Test
    void testSortedThrowsExceptionWhenNotOfComparableType() {
        ListX<BankAccount> bankAccountList = ListX.of(TestSampleGenerator.createSampleBankAccountList());
        assertThrows(IllegalStateException.class, bankAccountList::sorted);
    }

    @Test
    void testMaxOf() {
        ListX<BankAccount> bankAccountList = ListX.of(TestSampleGenerator.createSampleBankAccountList());

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
        ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

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
        MutableListX<PaintingAuction> auctions = Generator.createAuctions();

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
        Set<BankAccount> bankAccounts = new HashSet<>(TestSampleGenerator.createSampleBankAccountList());

        final Optional<BankAccount> expected = bankAccounts.stream().min(Comparator.comparing(BankAccount::getBalance));

        final Optional<BankAccount> actual = ListX.of(bankAccounts).minBy(BankAccount::getBalance);

        assertEquals(expected, actual);
    }

    @Test
    void testMaxBy() {
        ListX<BankAccount> set = ListX.of(new HashSet<>(TestSampleGenerator.createSampleBankAccountList()));

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

        final Map<Boolean, List<Period>> expectedMap = paintings.stream()
                .collect(partitioningBy(Painting::isInMuseum,
                        mapping(Painting::age, toList())));

        final Pair<ListX<Period>, ListX<Period>> actualMap = ListX.of(paintings)
                .partitionMapping(Painting::isInMuseum, Painting::age);

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

        final IntSummaryStatistics expected = paintings.stream().mapToInt(Painting::ageInYears).summaryStatistics();

        final IntSummaryStatistics actual = paintings.statsOfInts(Painting::ageInYears);

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

        final BigDecimalSummaryStatistics actual = bankAccounts.statsOfBigDecimals(BankAccount::getBalance);

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
                .toListXOf(Painting::painter);

        assertIterableEquals(actualPainters, expectedPainters);
    }

    @Test
    void testFilterAndMapToCollection() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final Deque<LocalDate> expectedLocalDates = museumList.stream()
                .filter(museum -> museum.getPaintings().size() > 3)
                .map(Museum::getDateOfOpening)
                .collect(toCollection(ArrayDeque::new));

        final Deque<LocalDate> actualLocalDates = museumList
                .filterBy(Museum::getPaintings, paintings -> paintings.size() > 3)
                .mapTo(ArrayDeque::new, Museum::getDateOfOpening);

        assertIterableEquals(expectedLocalDates, actualLocalDates);
    }

    @Test
    void testMapToExistingCollection() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        Deque<LocalDate> deque = new ArrayDeque<>();
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
    void testTakeWhile() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Museum> expected = new ArrayList<>();
        for (Museum m : museumList) {
            if (!(m.getPaintings().size() < 3)) {
                break;
            }
            expected.add(m);
        }

        final ListX<Museum> actual = museumList.takeWhile(museum -> museum.getPaintings().size() < 3);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testTakeWhileInclusive() {
        ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> takeWhileInclusive = list.takeWhileInclusive(i -> i != 5);
        final ListX<Integer> takeWhile = list.takeWhile(i -> i != 5);

        It.println("integers = " + takeWhileInclusive);

        assertAll(
                () -> assertIterableEquals(Arrays.asList(1, 2, 10, 4, 5), takeWhileInclusive),
                () -> assertIterableEquals(Arrays.asList(1, 2, 10, 4), takeWhile)
        );
    }

    @Test
    void testSkip() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Museum> expected = museumList.stream().skip(3).collect(Collectors.toList());

        final IterableX<Museum> actual = museumList.skip(3);

        It.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testTake() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final List<Museum> expected = museumList.stream().limit(3).collect(Collectors.toList());

        final IterableX<Museum> actual = museumList.take(3);

        It.println("actual = " + actual);

        assertIterableEquals(expected, actual);
    }

    @Test
    void testTakeLastWhile() {
        final ListX<Museum> museumList = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        final String[] strings = "De sterrennacht, Het melkmeisje, Les Demoiselles d'Avignon, Meisje met de parel, Le Rêve, Meisje met de rode hoed, Guernica ".split(",");

        final SetX<String> expected = ListX.of(strings).toSetXOf(String::trim);

        final SetX<String> actual = museumList
                .flatMap(Museum::getPaintings)
                .takeLastWhile(Painting::isInMuseum)
                .toSetXOf(Painting::name);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testFromIntArrayToMappedList() {
        final int[] numbers = {1, 4, 3, 6, 7, 4, 3, 234};

        final List<BigDecimal> expected = IntStream.of(numbers).mapToObj(BigDecimal::valueOf).collect(Collectors.toList());

        final List<BigDecimal> actual = ListX.ofInts(numbers).toListOf(BigDecimal::valueOf);

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
    void testIntersect() {
        final ListX<Collection<Integer>> collections = ListX.of(
                Arrays.asList(1, 2, 3, 4, 5, 7),
                Arrays.asList(2, 4, 5),
                Arrays.asList(4, 5, 6)
        );

        final SetX<Integer> intersect = collections.intersectionOf(It::self);

        assertIterableEquals(Arrays.asList(4, 5), intersect);
    }

    @Test
    void testIntersectMuseumPaintings() {
        final ListX<Museum> museums = ListX.of(TestSampleGenerator.getMuseumListContainingNulls());

        Set<Period> expected = museums.stream()
                .map(Museum::getPaintings)
                .collect(intersectingBy(Painting::getMilleniumOfCreation));

        final SetX<Period> intersection = museums.intersectionOf(Museum::getPaintings, Painting::getMilleniumOfCreation);

        It.println("intersection = " + intersection);

        assertEquals(expected, intersection);
    }

    @Test
    void testSumBigDecimals() {
        ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final BigDecimal expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = list.sumOf(BankAccount::getBalance);

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
    void testFoldRight() {
        ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        list.forEach(It::println);

        final BigDecimal bigDecimal = list.foldRight(BigDecimal.ZERO, (b, a) -> a.add(b.getBalance()));

        assertEquals(BigDecimal.valueOf(232511.34), bigDecimal);
    }

    @Test
    void testDropLastWhile() {
        ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final MutableListX<Integer> integers = list.skipLastToMutableListWhile(i -> i != 10);

        It.println("integers = " + integers);

        assertEquals(Arrays.asList(1, 2, 10, 4, 5, 10), integers);
    }

    @Test
    void testSkipWhile() {
        ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> integers = list.skipWhile(i -> i != 5);

        It.println("integers = " + integers);

        assertIterableEquals(Arrays.asList(5, 10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testSkipWhileInclusive() {
        ListX<Integer> list = ListX.of(1, 2, 10, 4, 5, 10, 6, 5, 3, 5, 6);

        list.forEach(It::println);

        final IterableX<Integer> integers = list.skipWhileInclusive(i -> i != 5);

        It.println("integers = " + integers);

        assertIterableEquals(Arrays.asList(10, 6, 5, 3, 5, 6), integers);
    }

    @Test
    void testListIteratorGetPreviousOnlyWorksBeforeWhenIsAtEnd() {
        List<Integer> list = Arrays.asList(22, 44, 88, 11, 33);
        ListIterator<Integer> listIterator = list.listIterator();

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
        ListX<BankAccount> bankAccounts = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final long expected = bankAccounts.stream().filter(BankAccount::isDutchAccount).count();

        final int actual = bankAccounts.countNotNullBy(BankAccount::isDutchAccount);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOf() {
        ListX<BankAccount> list = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final BigDecimal expected = list.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = list.sumOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testSumOfInt() {
        ListX<Painting> list = ListX.of(TestSampleGenerator.createPaintingList());

        final int expected = list.stream().mapToInt(Painting::ageInYears).sum();

        final long actual = list.sumOfInts(Painting::ageInYears);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testAverageOf() {
        ListX<Painting> list = ListX.of(TestSampleGenerator.createPaintingList());

        final double expected = list.stream().mapToInt(Painting::ageInYears).average().orElseThrow(NoSuchElementException::new);

        final double actual = list.averageOfInts(Painting::ageInYears);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testIterXOfIterable() {
        ListX<BankAccount> listX = ListX.of(TestSampleGenerator.createSampleBankAccountList());

        final BigDecimal expected = listX.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal actual = ListX.of(listX).sumOf(BankAccount::getBalance);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testTransformForEach() {
        final int[] ints = IntStream.range(0, 100_000).toArray();

        ListX.ofInts(ints)
                .filter(integer -> integer % 2 == 0)
                .onEach(this::printEvery10_000stElement)
                .forEach(i -> assertEquals(0, i % 2));
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

        final String expected = paintings.stream()
                .map(Painting::age)
                .map(Period::toString)
                .collect(Collectors.joining(", "));

        final String actual = paintings.joinToStringBy(Painting::age, ", ");

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

        final Set<Book> expected = bookList.stream()
                .filter(book -> !book.isAboutProgramming())
                .collect(Collectors.toSet());

        final SetX<Book> actual = bookList.toSetXSkipping(Book::isAboutProgramming);

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

        final int actual = bookList.countNotNullOf(Book::getCategory);

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
        ListX<Painting> paintingList = ListX.of(TestSampleGenerator.createPaintingList());

        final LocalDate expected = paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .max(Comparator.naturalOrder())
                .orElseThrow(NoSuchElementException::new);

        final ChronoLocalDate max = paintingList.stream()
                .map(Painting::painter)
                .collect(toListX())
                .maxOf(Painter::getDateOfBirth);

        final ChronoLocalDate actual = paintingList
                .mapNotNull(Painting::painter)
                .maxOf(Painter::getDateOfBirth);

        assertAll(
                () -> assertEquals(expected, max),
                () -> assertEquals(expected, actual)
        );
    }

    @Test
    void testCollectingToMapX() {
        List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final Map<LocalDate, String> expected = paintingList.stream()
                .map(Painting::painter)
                .collect(Collectors.toMap(Painter::getDateOfBirth, Painter::getLastname, (a, b) -> a));

//        final Set<LocalDate> expectedLocalDates = expected.keySet().stream()
//                .flatMap(date -> date.datesUntil(LocalDate.of(2000, Month.JANUARY, 1)))
//                .collect(toSet());

        final MapX<LocalDate, String> actual = ListX.of(paintingList)
                .map(Painting::painter)
                .collect(toMapX(Painter::getDateOfBirth, Painter::getLastname, (a, b) -> a));

        assertEquals(expected, actual);
    }

    @Test
    void testMapNotNull() {
        MutableListX<BankAccount> bankAccounts = MutableListX
                .of(TestSampleGenerator.createSampleBankAccountListContainingNulls());

        final String expected = bankAccounts.stream()
                .filter(Objects::nonNull)
                .map(BankAccount::getCustomer)
                .filter(Objects::nonNull)
                .map(Customer::getId)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElseThrow(NoSuchElementException::new);

        final String actual = bankAccounts
                .mapNotNull(BankAccount::getCustomer)
                .maxOf(Customer::getId);

        assertEquals(expected, actual);
    }

    @Test
    void testZipWithNext() {
        ListX<Painting> museumList = ListX.of(TestSampleGenerator.createPaintingList());

        final List<Integer> integers = museumList
                .mapNotNull(Painting::name)
                .zipWithNextToListOf(String::compareTo);

        assertEquals(Arrays.asList(-5, 83, -1, 5, -5, 1, 8), integers);
    }

    @Test
    void testZipTwoCollections() {
        ListX<Integer> values = ListX.of(0, 1, 2, 3, 4, 5, 6, 7);
        List<Integer> others = Arrays.asList(6, 5, 4, 3, 2, 1, 0);

        final List<Integer> integers = values.zipToListWith(others, Integer::compareTo);

        assertIterableEquals(Arrays.asList(-1, -1, -1, 0, 1, 1, 1), integers);
    }

    @Test
    void testToUnion() {
        ListX<Integer> values = ListX.of(0, 1, 2, 1, 3, 4, 5, 6, 7);
        List<Integer> other = Arrays.asList(6, 5, 4, 3, 2, 1, 0);

        final SetX<String> union = values.union(other, String::valueOf);

        assertIterableEquals(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7"), union);
    }

    @Test
    void mapToStringX() {
        final ListX<Book> bookList = ListX.of(TestSampleGenerator.createBookList());

        final List<Character> expected = bookList.stream()
                .map(Book::getCategory)
                .flatMapToInt(String::chars)
                .mapToObj(c -> (char) c)
                .collect(toList());


        final MutableListX<Character> actual = bookList
                .mapToStringX(Book::getCategory)
                .flatMapToMutableListOf(StringX::toMutableList);

        It.println("stringXES = " + actual);

        assertEquals(expected, actual);
    }

    private void printEvery10_000stElement(int i) {
        if (i % 10_000 == 0) {
            It.println(i);
        }
    }

    @Test
    void testLargeStream() {
        final List<BigDecimal> bigDecimals = IntStream.range(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList());

        assertEquals(50_000, bigDecimals.size());
    }

    @Test
    void testDistinctBy() {
        final ListX<BigDecimal> bigDecimals = ListX.of(IntStream.range(0, 100_000)
                .filter(integer -> integer % 2 == 0)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList()));

        final List<BigDecimal> expected = IntStream.rangeClosed(0, 254)
                .filter(integer -> integer % 2 == 0)
                .mapToObj(BigDecimal::valueOf)
                .collect(Collectors.toList());

        final ListX<BigDecimal> list = bigDecimals.distinctBy(BigDecimal::byteValue);

        It.println("list = " + list);

        assertEquals(expected, list);
    }

    @Test
    void castIfInstanceOf() {
        final ListX<Comparable<?>> list = ListX.of(3.0, 2, 4, 3, BigDecimal.valueOf(10), 5L, 'a', "String");

        final ListX<Integer> integers = list.castIfInstanceOf(Integer.class);

        It.println("integers = " + integers);

        assertEquals(ListX.ofInts(2, 4, 3), integers);
    }

    @Test
    void testCreateAnEmptyIterableX() {
        final ArrayDeque<String> strings = ListX.<String>empty()
                .toCollectionNotNullOf(ArrayDeque::new, It::self);

        assertTrue(strings.isEmpty());
    }

    @Test
    void testStreamCollectingAndThenEquivalent() {
        final MutableListX<Integer> integers = MutableListX.of(1, 4, 5, 3, 7, 4, 2);

        final int expected = integers.stream()
                .filter(n -> n > 4)
                .collect(collectingAndThen(toListX(), IterableXTest::calculateProduct));

        final int product = integers
                .filter(n -> n > 4)
                .let(IterableXTest::calculateProduct);

        It.println("product = " + product);

        assertEquals(expected, product, () -> "Something went wrong. Did you know, you can also crate dates from ints? " +
                integers.toListOf(day -> LocalDate.of(2020, Month.JANUARY, day)));
    }

    private static int calculateProduct(ListX<Integer> list) {
        return list.reduce((result, integer) -> result * integer).orElse(0);
    }

    @Test
    void testJava11AndBeforeCompilerTripperWorkFineInJava17() {
        assertDoesNotThrow(() -> empty());
    }

    // causes compiler error on java 11 and older:
    // java: Compilation failed: internal java compiler error
    public static <T> IterableX<T> empty() {
        return ListX.of(() -> new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                throw new NoSuchElementException();
            }
        });
    }

    @Test
    void testBranchingPaintingDataToThreeValues() {
        //arrange
        final ListX<Painting> paintingList = ListX.of(TestSampleGenerator.createPaintingList());

        final Triple<Map<Boolean, List<Painter>>, IntSummaryStatistics, Long> expected = paintingList.stream()
                .collect(branching(
                        partitioningBy(Painting::isInMuseum, mapping(Painting::painter, toList())),
                        summarizingInt(Painting::ageInYears),
                        counting()));

        final Triple<Pair<ListX<Painter>, ListX<Painter>>, IntStatistics, Long> actual = paintingList.asSequence()
                .toThree(s -> s.partitionMapping(Painting::isInMuseum, Painting::painter),
                        s -> s.statsOfInts(Painting::ageInYears),
                        Sequence::count);

        final IntSummaryStatistics expectedStats = expected.second();
        final IntSummaryStatistics actualStats = actual.second();

        assertAll(
                () -> assertEquals(expected.first().get(true), actual.first().first()),
                () -> assertEquals(expectedStats.getAverage(), actualStats.getAverage()),
                () -> assertEquals(expectedStats.getMax(), actualStats.getMax()),
                () -> assertEquals(expected.third().intValue(), actual.third())
        );
    }

    @Test
    void testDifferenceBetweenIterableXAndSequence() {
        final ListX<Integer> range = IntRange.of(0, 20).toListX();

        final ListX<String> strings = range/*.asSequence()*/
                .filter(i -> i % 2 == 0)
                .map(Generator::toStringIn100Millis)
                .onEach(String::length, It::println)
                .takeToMutableListWhileInclusive(s -> s.length() < 6);

        assertEquals(6, strings.size());
    }
}
