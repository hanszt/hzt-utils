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

import static hzt.collectors.CollectorsX.toListView;
import static java.util.stream.Collectors.collectingAndThen;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListViewTest {

    @Test
    void testGetElement() {
        final var words = ListView.of("hallo", "asffasf", "string", "test");

        assertAll(
                () -> assertEquals("test", words.get(3)),
                () -> assertEquals(4, words.size())
        );
    }

    @Test
    void testToMutableList() {
        final MutableList<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        expected.add(LocalDate.MIN);

        final var dates = museums.mapTo(MutableList::empty, PaintingAuction::getDateOfOpening);

        dates.add(LocalDate.MIN);

        assertEquals(expected, dates);
    }

    @Test
    void testTakeWhile() {
        final List<Museum> museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final List<Museum> expected = museumList.stream()
                .takeWhile(museum -> museum.getPaintings().size() < 3)
                .collect(Collectors.toList());

        final MutableList<Museum> actual = Sequence.of(museumList)
                .takeWhile(museum -> museum.getPaintings().size() < 3)
                .toMutableList();

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testBinarySearch() {
        final ListView<Integer> sortedList = ListView.of(-1, 0, 1, 2, 3, 4, 5);

        int valueToSearchFor = 2;

        final int indexInSortedList = sortedList.binarySearch(value -> value.compareTo(valueToSearchFor));

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testBinaryOfStringSearch() {
        final MutableList<String> sortedList = MutableList.of("adi", "hans", "huib", "sophie", "ted");

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
        final ListView<String> strings = ListView.build(this::getStringList);

        assertAll(
                () -> assertEquals(101, strings.size()),
                () -> assertTrue(strings.contains("Hallo"))
        );
    }

    private void getStringList(MutableList<String> list) {
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }
        list.add(1, "Hallo");
    }

    @Test
    void testListWithAll() {
        final MutableList<PaintingAuction> auctions = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = auctions.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());
        expected.add(LocalDate.MIN);
        expected.add(LocalDate.MAX);

        final ListView<LocalDate> dates = auctions
                .map(PaintingAuction::getDateOfOpening)
                .plus(ListView.of(LocalDate.MIN, LocalDate.MAX));

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testSkipLast() {
        ListView<Integer> list = ListView.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.skipLast(2);

        assertEquals(ListView.of(1, 2, 3, 4, 5), integers);
    }

    @Test
    void testTakeLast() {
        ListView<Integer> list = ListView.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.takeLast(2);

        assertEquals(ListView.of(6, 5), integers);
    }

    @Test
    void testTakeLastTo() {
        ListView<Integer> list = ListView.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.takeLastTo(size -> new LinkedTransferQueue<>(), 5);

        assertIterableEquals(new LinkedTransferQueue<>(List.of(3, 4, 5, 6, 5)), integers);
    }

    @Test
    void testAlso() {
        final MutableList<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        final CollectionView<LocalDate> dates = museums
                .mapTo(MutableList::empty, PaintingAuction::getDateOfOpening)
                .also(It::println);

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testWhen() {
        final MutableList<PaintingAuction> museums = Generator.createAuctions().toMutableList();

        final List<LocalDate> expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        final ListView<LocalDate> dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .when(ListView::isNotEmpty, It::println)
                .when(list -> list.size() > 3, It::println)
                .takeIf(ListView::isNotEmpty)
                .orElseThrow();

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testStreamCollectingAndThenEquivalent() {
        final MutableList<Integer> integers = MutableList.of(1, 4, 5, 3, 7, 4, 2);

        final int expected = integers.stream()
                .filter(n -> n > 4)
                .collect(collectingAndThen(toListView(), ListViewTest::calculateProduct));

        final int product = integers.asSequence()
                .filter(n -> n > 4)
                .toListView().let(ListViewTest::calculateProduct);

        It.println("product = " + product);

        assertEquals(expected, product, () -> "Something went wrong. Did you know, you can also crate dates from ints? " +
                integers.toListOf(day -> LocalDate.of(2020, Month.JANUARY, day)));
    }

    private static int calculateProduct(ListView<Integer> list) {
        return list.reduce((acc, i) -> acc * i).orElse(0);
    }
}
