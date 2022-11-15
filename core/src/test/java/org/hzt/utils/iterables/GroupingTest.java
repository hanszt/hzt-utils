package org.hzt.utils.iterables;

import org.hzt.test.ReplaceCamelCaseBySentence;
import org.hzt.utils.collections.ListX;
import org.hzt.utils.collections.MapX;
import org.hzt.utils.collections.MutableMapX;
import org.hzt.utils.collectors.CollectorsX;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import static java.util.Comparator.comparing;
import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class GroupingTest {

    @Test
    void testGroupingAggregateFromListX() {
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
    void testGroupingByEachCount() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final MapX<Integer, Integer> aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .eachCount();

        final var expected = MutableMapX.of(0, 3, 1, 2, 2, 2);

        assertEquals(expected, aggregated);
    }

    @Test
    void testGroupingByEachCountToTreeMap() {
        final var numbers = ListX.of(3, 4, 5, 6, 7, 8, 9);

        final var aggregated = numbers
                .groupingBy(nr -> nr % 3)
                .eachCountTo(TreeMap::new)
                .descendingMap();

        NavigableMap<Integer, Integer> expected = new TreeMap<>(MutableMapX.of(0, 3, 1, 2, 2, 2));

        System.out.println("expected = " + expected);
        System.out.println("aggregated = " + aggregated);

        assertEquals(expected, aggregated);
    }

    @Test
    void testGroupingByFoldTo() {
        var fruits = ListX.of("cherry", "blueberry", "citrus", "apple", "apricot", "banana", "coconut");

        final Map<Character, Pair<Character, List<String>>> evenFruits = fruits
                .groupingBy(fruit -> fruit.charAt(0))
                .foldTo(HashMap::new,
                        (firstChar, string) -> Pair.of(firstChar, new ArrayList<>()),
                        GroupingTest::addEvenFruits);

        final var sorted = evenFruits.values().stream()
                .sorted(comparing(Pair::first))
                .collect(CollectorsX.toListX());

        println(sorted);

        final ListX<Pair<Character, List<String>>> expected = ListX.of(Pair.of('a', Collections.emptyList()),
                Pair.of('b', Collections.singletonList("banana")),
                Pair.of('c', List.of("cherry", "citrus")));

        assertEquals(expected, sorted);
    }

    @Test
    void testGroupingByFold() {
        var fruits = ListX.of("cherry", "blueberry", "citrus", "apple", "apricot", "banana", "coconut");

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

    private static Pair<Character, List<String>> addEvenFruits(char key, Pair<Character, List<String>> pair, String fruit) {
        if (fruit.length() % 2 == 0) {
            pair.second().add(fruit);
        }
        return pair;
    }

    private static StringBuilder toStringBuilder(int key, StringBuilder stringBuilder, int element, boolean firstElement) {
        if (firstElement) {
            return new StringBuilder().append(key).append(":").append(element);
        }
        return stringBuilder.append("-").append(element);
    }

}
