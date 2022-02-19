package hzt.collections;

import hzt.sequences.Sequence;
import hzt.test.Generator;
import hzt.test.model.PaintingAuction;
import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;

import static hzt.collectors.CollectorsX.toListX;
import static java.util.stream.Collectors.collectingAndThen;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ListXTest {

    @Test
    void testGetElement() {
        final var words = ListX.of("hallo", "asffasf", "string", "test");

        assertAll(
                () -> assertEquals("test", words.get(3)),
                () -> assertEquals(4, words.size())
        );
    }

    @Test
    void testToMutableList() {
        final MutableListX<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        expected.add(LocalDate.MIN);

        final var dates = museums.mapTo(MutableListX::empty, PaintingAuction::getDateOfOpening);

        dates.add(LocalDate.MIN);

        assertEquals(expected, dates);
    }

    @Test
    void testTakeWhile() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final List<Museum> expected = museumList.stream()
                .takeWhile(museum -> museum.getPaintings().size() < 3)
                .collect(Collectors.toList());

        final MutableListX<Museum> actual = Sequence.of(museumList)
                .takeWhile(museum -> museum.getPaintings().size() < 3)
                .toMutableList();

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testBinarySearch() {
        final ListX<Integer> sortedList = ListX.of(-1, 0, 1, 2, 3, 4, 5);

        int valueToSearchFor = 2;

        final int indexInSortedList = sortedList.binarySearch(value -> value.compareTo(valueToSearchFor));

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testBinaryOfStringSearch() {
        final MutableListX<String> sortedList = MutableListX.of("adi", "hans", "huib", "sophie", "ted");

        final int indexInSortedList = sortedList.binarySearch(string -> string.compareTo("sophie"));
        final int invertedInsertionPoint = sortedList.binarySearch(string -> string.compareTo("matthijs"));
        // the inverted insertion point (-insertion point - 1)
        final int insertionIndex = -invertedInsertionPoint - 1;

        assertAll(
                () -> assertEquals(3, indexInSortedList),
                () -> assertEquals(3, insertionIndex)
        );
    }

    @Test
    void testToListYieldsUnModifiableList() {
        PaintingAuction auction = Generator.createVanGoghAuction();
        final Year yearToAdd = Year.of(2000);

        final List<Year> years = auction.toListOf(Painting::getYearOfCreation);

        assertThrows(UnsupportedOperationException.class, () -> years.add(yearToAdd));
    }

    @Test
    void buildList() {
        final ListX<String> strings = ListX.build(this::getStringList);

        assertAll(
                () -> assertEquals(101, strings.size()),
                () -> assertTrue(strings.contains("Hallo"))
        );
    }

    private void getStringList(MutableListX<String> list) {
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }
        list.add(1, "Hallo");
    }

    @Test
    void testListWithAll() {
        final MutableListX<PaintingAuction> auctions = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = auctions.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());
        expected.add(LocalDate.MIN);
        expected.add(LocalDate.MAX);

        final ListX<LocalDate> dates = auctions
                .map(PaintingAuction::getDateOfOpening)
                .plus(ListX.of(LocalDate.MIN, LocalDate.MAX));

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testSkipLast() {
        ListX<Integer> list = ListX.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.skipLast(2);

        assertEquals(ListX.of(1, 2, 3, 4, 5), integers);
    }

    @Test
    void testTakeLast() {
        ListX<Integer> list = ListX.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.takeLast(2);

        assertEquals(ListX.of(6, 5), integers);
    }

    @Test
    void testTakeLastTo() {
        ListX<Integer> list = ListX.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.takeLastTo(size -> new LinkedTransferQueue<>(), 5);

        assertIterableEquals(new LinkedTransferQueue<>(List.of(3, 4, 5, 6, 5)), integers);
    }

    @Test
    void testAlso() {
        final MutableListX<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        final CollectionX<LocalDate> dates = museums
                .mapTo(MutableListX::empty, PaintingAuction::getDateOfOpening)
                .also(It::println);

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testWhen() {
        final MutableListX<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        final ListX<LocalDate> dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .when(ListX::isNotEmpty, It::println)
                .when(list -> list.size() > 3, It::println)
                .takeIf(ListX::isNotEmpty)
                .orElseThrow();

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testShuffledInts() {
        final var integers = ListX.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        final var shuffled = integers.shuffled();

        System.out.println("shuffled = " + shuffled);

        assertAll(
                () -> assertNotEquals(integers, shuffled),
                () -> assertTrue(shuffled.containsAll(integers)),
                () -> assertTrue(integers.containsAll(shuffled))
        );
    }

    @Test
    void testShuffledObjects() {
        final var input = ListX.of(TestSampleGenerator.createPaintingList()).map(Painting::name);

        final var shuffled = input.shuffled();

        System.out.println("shuffled = " + shuffled);

        assertAll(
                () -> assertNotEquals(input, shuffled),
                () -> assertTrue(shuffled.containsAll(input)),
                () -> assertTrue(input.containsAll(shuffled))
        );
    }

    @Test
    void testStreamCollectingAndThenEquivalent() {
        final MutableListX<Integer> integers = MutableListX.of(1, 4, 5, 3, 7, 4, 2);

        final int expected = integers.stream()
                .filter(n -> n > 4)
                .collect(collectingAndThen(toListX(), ListXTest::calculateProduct));

        final int product = integers.asSequence()
                .filter(n -> n > 4)
                .toListX().let(ListXTest::calculateProduct);

        It.println("product = " + product);

        assertEquals(expected, product, () -> "Something went wrong. Did you know, you can also crate dates from ints? " +
                integers.toListOf(day -> LocalDate.of(2020, Month.JANUARY, day)));
    }

    private static int calculateProduct(ListX<Integer> list) {
        return list.reduce((acc, i) -> acc * i).orElse(0);
    }
}
