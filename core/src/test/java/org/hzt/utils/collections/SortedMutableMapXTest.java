package org.hzt.utils.collections;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.utils.It;
import org.hzt.utils.iterables.IterableX;
import org.junit.jupiter.api.Test;

import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class SortedMutableMapXTest {

    @Test
    void testNavigableMapComparingByKey() {
        final SortedMutableMapX<String, Integer> map = SortedMutableMapX.comparingByKey(String::length);
        map.put("hallo", 1);
        map.put("Hi", 2);
        map.put("greetings", 3);

        final MapX<String, Integer> mapView = MapX.of("hallo", 1, "greetings", 3, "Hi", 2);
        final SortedMutableMapX<String, Integer> expected = SortedMutableMapX.of(mapView, String::length);

        map.forEach(It::println);

        assertEquals(expected, map);
    }

    @Test
    void testGetNavigableMap() {
        final IterableX<Museum> museumListContainingNulls = ListX.of(TestSampleGenerator.getMuseumListContainingNulls())
                .sortedBy(e -> Math.random());

        final NavigableMap<String, Museum> expected = new TreeMap<>(museumListContainingNulls.stream()
                .filter(m -> m != null && m.getName() != null)
                .collect(Collectors.toMap(Museum::getName, It::self)));

        final SortedMutableMapX<String, Museum> actual = museumListContainingNulls
                .associateBy(Museum::getName)
                .toSortedMap(It::self);

        actual.keySet().forEach(It::println);

        assertAll(
                () -> assertIterableEquals(actual.keySet(), expected.keySet()),
                () -> assertIterableEquals(actual.values(), expected.values())
        );
    }

    @Test
    void testGetNavigableMapAssociatedWith() {
        final MutableSetX<Museum> museumSetContainingNulls = MutableSetX
                .of(TestSampleGenerator.getMuseumListContainingNulls());

        final NavigableMap<Museum, String> expected = new TreeMap<>(museumSetContainingNulls.stream()
                .filter(m -> m != null && m.getName() != null)
                .collect(Collectors.toMap(It::self, Museum::getName)));

        final SortedMutableMapX<Museum, String> actual = museumSetContainingNulls
                .filterBy(Museum::getName, Objects::nonNull)
                .associateWith(Museum::getName)
                .toSortedMap(Museum::getName);

        final Museum firstMuseum = actual.first().getKey();

        It.println("firstMuseum = " + firstMuseum);

        assertAll(
                () -> assertIterableEquals(actual.keySet(), expected.keySet()),
                () -> assertIterableEquals(actual.values(), expected.values())
        );
    }

}
