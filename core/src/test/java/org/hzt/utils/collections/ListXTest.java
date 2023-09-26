package org.hzt.utils.collections;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.iterables.Collectable;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.test.Generator;
import org.hzt.utils.test.model.PaintingAuction;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static org.hzt.utils.collectors.CollectorsX.toListX;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testReverseOf() {
        final var strings = ListX.of("This", "is", "a", "test");
        final var reversed = strings.reversed();
        assertEquals(ListX.of("test", "a", "is", "This"), reversed);
    }

    @Test
    void testToMutableList() {
        final var museums = Generator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .collect(Collectors.toList());

        expected.add(LocalDate.MIN);

        final var dates = museums.mapTo(MutableListX::empty, PaintingAuction::getDateOfOpening);

        dates.add(LocalDate.MIN);

        assertEquals(expected, dates);
    }

    @Test
    void testTakeWhile() {
        final var museumList = TestSampleGenerator.getMuseumListContainingNulls();

        final var expected = museumList.stream()
                .takeWhile(museum -> museum.getPaintings().size() < 3)
                .toList();

        final var actual = Sequence.of(museumList)
                .takeWhile(museum -> museum.getPaintings().size() < 3)
                .toMutableList();

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testBinarySearch() {
        final var sortedList = ListX.of(-1, 0, 1, 2, 3, 4, 5);

        final var valueToSearchFor = 2;

        final var indexInSortedList = sortedList.binarySearch(value -> value.compareTo(valueToSearchFor));

        assertEquals(3, indexInSortedList);
    }

    @Test
    void testBinaryOfStringSearch() {
        final var sortedList = MutableListX.of("adi", "hans", "huib", "sophie", "ted");

        final var indexInSortedList = sortedList.binarySearchFor("sophie");
        final var invertedInsertionPoint = sortedList.binarySearchFor("matthijs");
        // the inverted insertion point (-insertion point - 1)
        final var insertionIndex = -invertedInsertionPoint - 1;

        assertAll(
                () -> assertEquals(3, indexInSortedList),
                () -> assertEquals(3, insertionIndex)
        );
    }

    @Test
    void testBinarySearchTo() {
        final var sortedList = MutableListX.of("adi", "hans", "huib", "sophie", "ted");

        final var indexInSortedList = sortedList.binarySearchTo(4, name -> name.compareTo("sophie"));
        final var invertedInsertionPoint = sortedList.binarySearchTo(4, name -> name.compareTo("ted"));
        // the inverted insertion point (-insertion point - 1)
        final var insertionIndex = -invertedInsertionPoint - 1;

        assertAll(
                () -> assertEquals(3, indexInSortedList),
                () -> assertEquals(4, insertionIndex)
        );
    }

    @Test
    void testBinarySearchFrom() {
        final var sortedList = MutableListX.of("adi", "hans", "huib", "sophie", "ted");

        final var indexInSortedList = sortedList.binarySearchFrom(2, value ->  value.compareTo("sophie"));
        final var invertedInsertionPoint = sortedList.binarySearchFrom(2, value -> value.compareTo("adi"));
        // the inverted insertion point (-insertion point - 1)
        final var insertionIndex = -invertedInsertionPoint - 1;

        assertAll(
                () -> assertEquals(3, indexInSortedList),
                () -> assertEquals(2, insertionIndex)
        );
    }

    @Test
    void testBinarySearchInListWithNonComparableObjectsThrowsIllegalStateException() {
        final var list = ListX.of(Locale.US, Locale.FRANCE, Locale.CANADA, Locale.GERMANY);
        final var exception = assertThrows(IllegalStateException.class, () -> list.binarySearchFor(Locale.US));
        assertEquals("Can not perform binary search by non comparable search value type: Locale", exception.getMessage());
    }

    @Test
    void testToListYieldsUnModifiableList() {
        final var auction = Generator.createVanGoghAuction();
        final var yearToAdd = Year.of(2000);

        final var years = auction.toListOf(Painting::getYearOfCreation);

        assertThrows(UnsupportedOperationException.class, () -> years.add(yearToAdd));
    }

    @Test
    void buildList() {
        final var strings = ListX.build(this::getStringList);

        assertAll(
                () -> assertEquals(101, strings.size()),
                () -> assertTrue(strings.contains("Hallo"))
        );
    }

    private void getStringList(final MutableListX<String> list) {
        for (var i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }
        list.add(1, "Hallo");
    }

    @Test
    void testListPlusOtherIterable() {
        final var auctions = Generator.createAuctions().toMutableList();

        final var datesFromStream = auctions.stream()
                .map(PaintingAuction::getDateOfOpening)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        datesFromStream.add(LocalDate.MIN);
        datesFromStream.add(LocalDate.MAX);
        final var expected = ListX.of(datesFromStream);

        final var dates = auctions
                .mapNotNull(PaintingAuction::getDateOfOpening)
                .plus(ListX.of(LocalDate.MIN, LocalDate.MAX));

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testSkipLast() {
        final var list = ListX.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.skipLast(2);

        assertEquals(ListX.of(1, 2, 3, 4, 5), integers);
    }

    @Test
    void testSkipLastTo() {
        final var list = ListX.of(1, 2, 2, 3, 4, 5, 6, 5);

        final var integers = list.skipLastTo(HashSet::new, 2);

        assertEquals(Set.of(1, 2, 3, 4, 5), integers);
    }

    @Test
    void testTakeLast() {
        final var list = ListX.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.takeLast(2);

        assertEquals(ListX.of(6, 5), integers);
    }

    @Test
    void testTakeLastTo() {
        final var list = ListX.of(1, 2, 3, 4, 5, 6, 5);

        final var integers = list.takeLastTo(size -> new LinkedTransferQueue<>(), 5);

        assertIterableEquals(new LinkedTransferQueue<>(List.of(3, 4, 5, 6, 5)), integers);
    }

    @Test
    void testAlso() {
        final var museums = Generator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .toList();

        final CollectionX<LocalDate> dates = museums
                .mapTo(MutableListX::empty, PaintingAuction::getDateOfOpening)
                .also(It::println);

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testWhen() {
        final var museums = Generator.createAuctions().toMutableList();

        final var expected = museums.stream()
                .map(PaintingAuction::getDateOfOpening)
                .filter(Objects::nonNull)
                .toList();

        final List<LocalDate> dates = museums
                .map(PaintingAuction::getDateOfOpening)
                .when(ListX::isNotEmpty, It::println)
                .when(list -> list.size() > 3, It::println)
                .takeIf(ListX::isNotEmpty)
                .map(Collectable::toMutableList)
                .orElseThrow();

        It.println("dates = " + dates);

        assertEquals(expected, dates);
    }

    @Test
    void testShuffledInts() {
        final var integers = ListX.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        final var shuffled = integers.shuffled();

        It.println("shuffled = " + shuffled);

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

        It.println("shuffled = " + shuffled);

        assertAll(
                () -> assertNotEquals(input, shuffled),
                () -> assertTrue(shuffled.containsAll(input)),
                () -> assertTrue(input.containsAll(shuffled))
        );
    }

    @Test
    void testListXCanNotBeCastToMutableListXToModifyItsInternalContent() {
        final var integers = ListX.of(1, 2, 3, 4, 5, 6);
        final var initSize = integers.size();

        final Executable executable = () -> {
            final var mutableList = (MutableListX<Integer>) integers;
            System.out.println("mutableList = " + mutableList);
        };

        assertAll(
                () -> assertThrows(ClassCastException.class, executable),
                () -> assertEquals(initSize, integers.size())
        );
    }

    @Test
    void testStreamCollectingAndThenEquivalent() {
        final var integers = MutableListX.of(1, 4, 5, 3, 7, 4, 2);

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

    @Nested
    class EqualsTests {

        @Test
        void testListXEquals() {
            final var list1 = ListX.of("This", "is", "a", "test");
            final var list2 = ListX.of("This", "is", "a", "test");

            assertAll(
                    () -> assertEquals(list2, list1),
                    () -> assertEquals(list1, list2)
            );
        }

        @Test
        void testListXAndListDoNotEqual() {
            final var listX =ListX.of("This", "is", "a", "test");
            final var list = List.of("This", "is", "a", "test");

            assertAll(
                    () -> assertNotEquals(list, listX),
                    () -> assertNotEquals(listX, list)
            );
        }
    }

    private static int calculateProduct(final ListX<Integer> list) {
        return list.reduce((acc, i) -> acc * i).orElse(0);
    }
}
