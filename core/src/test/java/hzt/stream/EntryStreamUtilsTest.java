package hzt.stream;

import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Book;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static hzt.stream.EntryStreamUtils.*;
import static hzt.collectors.CollectorsX.toMap;
import static hzt.function.predicates.StringPredicates.startsWithAnyOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntryStreamUtilsTest {

    @Test
    void testMappingEntriesWithEntryFunction() {
        final Map<String, List<Book>> groupedByCategoryMap = TestSampleGenerator.createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        final List<String> groupedByBookNameListAsString = groupedByCategoryMap.entrySet().stream()
                .map(value(HashSet::new))
                .map(toSingle(EntryStreamUtilsTest::asString))
                .collect(Collectors.toList());

        groupedByBookNameListAsString.forEach(It::println);

        assertTrue(groupedByBookNameListAsString.stream().noneMatch(String::isEmpty));
    }

    @Test
    void testMapEntryThenFilterByValueThenFilterByKeyThenCollectToMap() {
        final Map<String, List<Book>> groupedByCategoryMap = TestSampleGenerator.createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        It.println("groupedByCategoryMap:");
        groupedByCategoryMap.entrySet().forEach(It::println);

        final Map<String, Set<Book>> expectedMap = groupedByCategoryMap.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<String, HashSet<Book>>(e.getKey(), new HashSet<>(e.getValue())) {})
                .filter(e -> e.getKey() != null && (e.getKey().startsWith("E") || e.getKey().startsWith("F")))
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        final Map<String, HashSet<Book>> actualMap = groupedByCategoryMap.entrySet().stream()
                .map(value(HashSet::new))
                .filter(byKey(startsWithAnyOf("E", "F")))
                .filter(byValue(books -> !books.isEmpty()))
                .collect(toMap());

        It.println("Result:");
        actualMap.entrySet().forEach(It::println);

        assertEquals(expectedMap, actualMap);
    }

    private static String asString(String s, Set<Book> books) {
        return s + " -> " + books.stream()
                .map(Book::toString)
                .collect(Collectors.joining(", "));
    }

    @Test
    void testStreamingOverMapEntriesThenFilterByEntryThenMappingToEntryThenCollectingToMap() {
        final Map<Painter, List<Painting>> groupedByPainterMap = TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(Painting::painter));

        groupedByPainterMap.entrySet().stream()
                .map(value(List::size))
                .forEach(It::println);

        final Map<String, Integer> result = groupedByPainterMap.entrySet().stream()
                .filter(byEntry(EntryStreamUtilsTest::painterBornIn19thCenturyAndListGreaterThan1))
                .map(toEntry(Painter::getLastname, List::size))
                .collect(toMap());

        It.println("result = " + result);
        assertTrue(result.keySet().containsAll(Arrays.asList("van Gogh", "Picasso")));
    }

    private static boolean painterBornIn19thCenturyAndListGreaterThan1(Painter painter, List<Painting> paintingList) {
        final int year = painter.getDateOfBirth().getYear();
        return 1800 <= year && year < 1900 && paintingList.size() >= 2;
    }

    @Test
    void testStreamingOverMapEntriesThenFilterByKeyThenMappingToInvertedEntryThrowsDuplicatedKeyException() {
        final Map<Painter, List<Painting>> groupedByPainterMap = TestSampleGenerator.createPaintingList().stream()
                .collect(Collectors.groupingBy(Painting::painter));

        groupedByPainterMap.entrySet().stream()
                .map(value(List::size))
                .forEach(It::println);

        Throwable throwable = assertThrows(IllegalStateException.class, () -> invertKeysAndValues(groupedByPainterMap));
        throwable.printStackTrace();
    }

    private void invertKeysAndValues(Map<Painter, List<Painting>> groupedByPainterMap) {
        groupedByPainterMap.entrySet().stream()
                .filter(byValue(list -> list.size() > 1))
                .map(toInvertedEntry(Painter::getLastname, List::size))
                .collect(toMap())
                .entrySet().forEach(It::println);
    }

    @Test
    void testConsumingEntryStreamWithBiFunction() {
        final Map<String, List<Book>> groupedByCategoryMap = TestSampleGenerator.createBookList().stream()
                .collect(Collectors.groupingBy(Book::getCategory));

        It.println("groupedByCategoryMap:");
        groupedByCategoryMap.entrySet().forEach(It::println);

        groupedByCategoryMap.entrySet().stream()
                .map(value(HashSet::new))
                .filter(byKey(startsWithAnyOf("E", "F")))
                .filter(byValue(books -> !books.isEmpty()))
                .forEach(entry(EntryStreamUtilsTest::assertResult)
                        .andThen(entry((bookCategory, bookSet) ->
                                It.println("key: " +  bookCategory + ", value: " + bookSet))));
    }

    private static void assertResult(String category, HashSet<Book> books) {
        assertTrue(category.startsWith("E") || category.startsWith("F"));
        assertFalse(books.isEmpty());
    }

}
