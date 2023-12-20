package org.hzt.utils.iterables;

import org.hzt.test.ReplaceCamelCaseBySentence;
import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.SortedMutableMapX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class GroupingTest {

    @Test
    void testGroupingByAggregateFromListX() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final var aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .aggregate(GroupingTest::toStringBuilder)
                .mapByValues(StringBuilder::toString);

        println(aggregated);

        final var expected = MutableMapX.of(0, "0:3-6-9", 1, "1:4-7", 2, "2:5-8");

        assertEquals(expected, aggregated);
    }

    @Test
    void testGroupingAggregateFromListX() {
        final var eachCount = StringX.of("hallo daar")
                .grouping()
                .eachCount();

        final var expected = MapX.of('h', 1L, 'a', 3L, 'l', 2L, 'o', 1L, ' ', 1L, 'd', 1L, 'r', 1L);

        assertEquals(expected, eachCount);
    }

    private static StringBuilder toStringBuilder(final int key, final StringBuilder stringBuilder, final int element, final boolean firstElement) {
        if (firstElement) {
            return new StringBuilder().append(key).append(":").append(element);
        }
        return stringBuilder.append("-").append(element);
    }

    @Test
    void testGroupingByEachCount() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final MapX<Integer, Long> aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .eachCount();

        final var expected = MapX.of(0, 3L, 1, 2L, 2, 2L);

        assertTrue(expected.containsAll(aggregated));
    }

    @Test
    void testKeyOf() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final var i = numbers
                .groupingBy(nr -> nr % 3)
                .keyOf(122);

        assertEquals(2, i);
    }

    @Test
    void testGroupingByEachCountToTreeMap() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final var aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .eachCountTo(TreeMap::new)
                .descendingMap();

        final NavigableMap<Integer, Long> expected = new TreeMap<>(MutableMapX.of(0, 3L, 1, 2L, 2, 2L));

        println("expected = " + expected);
        println("aggregated = " + aggregated);

        assertEquals(expected, aggregated);
    }

    @Test
    void testGroupingByFoldTo() {
        final var fruits = ListX.of("cherry", "blueberry", "citrus", "apple", "apricot", "banana", "coconut");

        final Map<Character, List<String>> evenFruits = fruits
                .groupingBy(fruit -> fruit.charAt(0))
                .foldTo(HashMap::new,
                        (firstChar, fruit) -> new ArrayList<>(),
                        GroupingTest::addEvenFruits);

        final NavigableMap<Character, List<String>> sorted = Sequence.ofMap(evenFruits).toSortedMap(It::self);

        println(sorted);

        final NavigableMap<Character, List<String>> expected =
                SortedMutableMapX.of(MapX.of('a', Collections.emptyList(),
                        'b', List.of("banana"),
                'c', List.of("cherry", "citrus")), It::self);

        assertEquals(expected, sorted);
    }

    private static List<String> addEvenFruits(final char key, final List<String> fruits, final String fruit) {
        if (fruit.length() % 2 == 0) {
            fruits.add(fruit);
        }
        return fruits;
    }

    @Test
    void testGroupingByFold() {
        final var fruits = ListX.of("cherry", "blueberry", "citrus", "apple", "apricot", "banana", "coconut");

        final var fruitNameLengthSum = fruits
                .groupingBy(fruit -> fruit.charAt(0))
                .fold(0, (accLengths, next) -> accLengths + next.length());

        println(fruitNameLengthSum);

        final var expected = MutableMapX.of('c', 19, 'b', 15, 'a', 12);

        assertEquals(expected, fruitNameLengthSum);
    }

    @Test
    void testGroupingReduce() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final var aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .reduce(Integer::sum);

        final var expected = MutableMapX.of(0, 18, 1, 11, 2, 13);

        assertEquals(expected, aggregated);
    }

    @Test
    void testCollect() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final var aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .collect(mapping(Integer::doubleValue,
                                reducing(0.0, Double::sum)));

        final var expected = MapX.of(0, 18.0, 1, 11.0, 2, 13.0);

        assertTrue(expected.containsAll(aggregated));
    }

    @Test
    void testFilteringCollect() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        final var aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .filtering(nr -> nr % 2 == 0)
                .collect(toList());

        final var expected = numbers
                .filter(nr -> nr % 2 == 0)
                .groupBy(nr -> nr % 3);

        println("aggregated = " + aggregated);
        println("expected = " + expected);

        assertEquals(expected.entrySet(), aggregated.entrySet());
    }

}
