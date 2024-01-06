package org.hzt.utils.iterables;

import org.hzt.test.ReplaceCamelCaseBySentence;
import org.hzt.utils.It;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableListX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collections.SortedMutableMapX;
import org.hzt.utils.sequences.Sequence;
import org.hzt.utils.strings.StringX;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class GroupableTest {

    @Test
    void testGroupingAggregateFromListX() {
        final ListX<Integer> numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final MapX<Integer, String> aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .aggregate(GroupableTest::toStringBuilder)
                .mapByValues(StringBuilder::toString);

        println(aggregated);

        final MapX<Integer, String> expected = MapX.of(0, "0:3-6-9", 1, "1:4-7", 2, "2:5-8");

        assertEquals(expected, aggregated);
    }

    private static StringBuilder toStringBuilder(final int key, final StringBuilder stringBuilder, final int element, final boolean firstElement) {
        if (firstElement) {
            return new StringBuilder().append(key).append(":").append(element);
        }
        return stringBuilder.append("-").append(element);
    }

    @Test
    void testGroupingByEachCount() {
        final ListX<Integer> numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final MapX<Integer, Long> aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .eachCount();

        final MapX<Integer, Long> expected = MapX.of(0, 3L, 1, 2L, 2, 2L);

        assertEquals(expected, aggregated);
    }

    @Test
    void testKeyOf() {
        final ListX<Integer> numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final Integer i = numbers
                .groupingBy(nr -> nr % 3)
                .keyOf(122);

        assertEquals(2, i);
    }

    @Test
    void testGroupingByEachCountToTreeMap() {
        final ListX<Integer> numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final NavigableMap<Integer, Long> aggregated = numbers
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
        final ListX<String> fruits = ListX.of("cherry", "blueberry", "citrus", "apple", "apricot", "banana", "coconut");

        final Map<Character, List<String>> evenFruits = fruits
                .groupingBy(fruit -> fruit.charAt(0))
                .foldTo(HashMap::new,
                        (firstChar, fruit) -> new ArrayList<>(),
                        GroupableTest::addEvenFruits);

        final NavigableMap<Character, List<String>> sorted = Sequence.ofMap(evenFruits).toSortedMap(It::self);

        println(sorted);

        final NavigableMap<Character, List<String>> expected =
                SortedMutableMapX.of(MapX.of('a', Collections.emptyList(),
                        'b', Collections.singletonList("banana"),
                        'c', Arrays.asList("cherry", "citrus")), It::self);

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
        final ListX<String> fruits = ListX.of("cherry", "blueberry", "citrus", "apple", "apricot", "banana", "coconut");

        final MapX<Character, Integer> fruitNameLengthSum = fruits
                .groupingBy(fruit -> fruit.charAt(0))
                .fold(0, (accLengths, next) -> accLengths + next.length());

        println(fruitNameLengthSum);

        final MapX<Character, Integer> expected = MapX.of('c', 19, 'b', 15, 'a', 12);

        assertEquals(expected, fruitNameLengthSum);
    }

    @Test
    void testGroupingReduce() {
        final ListX<Integer> numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final MapX<Integer, Integer> aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .reduce(Integer::sum);

        final MapX<Integer, Integer> expected = MapX.of(0, 18, 1, 11, 2, 13);

        assertEquals(expected, aggregated);
    }

    @Test
    void testCollect() {
        final ListX<Integer> numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final MapX<Integer, Double> aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .collect(mapping(Integer::doubleValue,
                        reducing(0.0, Double::sum)));

        final MapX<Integer, Double> expected = MapX.of(0, 18.0, 1, 11.0, 2, 13.0);

        assertEquals(expected, aggregated);
    }

    @Test
    void testGrouping() {
        final MapX<Character, Long> charsToCounts = StringX.of("this is a string").grouping().eachCount();

        assertEquals(MapX.of('t', 2L, 'h', 1L, 'i', 3L, 's', 3L, 'a', 1L, 'r', 1L, 'n', 1L, 'g', 1L, ' ', 3L), charsToCounts);
    }

    @Test
    void testFilteringCollect() {
        final ListX<Integer> numbers = ListX.of(3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        final MapX<Integer, List<Integer>> aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .filtering(nr -> nr % 2 == 0)
                .collect(toList());

        final MapX<Integer, MutableListX<Integer>> expected = numbers
                .filter(nr -> nr % 2 == 0)
                .groupBy(nr -> nr % 3);

        assertEquals(expected, aggregated);
    }

}
