package org.hzt.utils.collections;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.hzt.utils.It;
import org.hzt.utils.function.IndexedConsumer;
import org.hzt.utils.ranges.IntRange;
import org.hzt.utils.sequences.EntrySequence;
import org.hzt.utils.strings.StringX;
import org.hzt.utils.tuples.IndexedValue;
import org.hzt.utils.tuples.Pair;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapXTest {

    @Test
    void testInvertMap() {
        final var museumMap = TestSampleGenerator.createMuseumMap();

        final var expected = museumMap.entrySet().stream()
                .map(e -> Map.entry(e.getValue(), e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final var actual = MapX.of(museumMap).inverted();

        It.println("actual = " + actual);

        assertEquals(expected.entrySet(), actual.entrySet());
    }

    @Test
    void testToIterXThanSum() {
        final var museumMap = TestSampleGenerator.createMuseumMap();

        final var expected = museumMap.values().stream()
                .map(Museum::getDateOfOpening)
                .mapToInt(LocalDate::getDayOfMonth)
                .sum();

        final var actual = MapX.of(museumMap)
                .mapValuesTo(MutableListX::empty, Museum::getDateOfOpening)
                .intSumOf(LocalDate::getDayOfMonth);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testForEachIndexed() {
        final List<IndexedValue<Map.Entry<String, Museum>>> list = new ArrayList<>();
        final IndexedConsumer<Map.Entry<String, Museum>> biConsumer = (index, value) -> list.add(new IndexedValue<>(index, value));

        final var museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        ListX.of(museumListContainingNulls).associateBy(Museum::getName).forEachIndexed(biConsumer);

        list.forEach(It::println);

        assertEquals(3, list.size());
    }

    @Test
    void flatMapToListOf() {
        final var museumMap = TestSampleGenerator.createMuseumMap();

        final var expected = museumMap.entrySet().stream()
                .flatMap(e -> e.getValue().getPaintings().stream())
                .toList();

        final List<Painting> actual = MapX.of(museumMap).flatMapValuesTo(MutableListX::empty, Museum::getPaintings);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void entrySequenceOfMap() {
        final Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        final var result = EntrySequence.of(map).mapByValues(Year::of).toMapX();

        assertEquals(MapX.of("1", Year.of(1), "2", Year.of(2), "3", Year.of(3)), result);
    }

    @Test
    void testMapXOfMapSameAsInputMap() {
        final var museumMap = TestSampleGenerator.createMuseumMap();

        final var mapX = MapX.of(museumMap);
        MapX.of(mapX).entrySet().forEach(It::println);

        It.println("map = " + mapX);

        assertEquals(museumMap.entrySet(), mapX.entrySet());
    }

    @Test
    void testComputeIfAbsent() {
        final var museumMap = ListX.of(TestSampleGenerator.createMuseumList())
                .associateBy(Museum::getName);

        final var expected = museumMap.get("Van Gogh Museum");

        final var map = MutableMapX.of(museumMap);

        final var van_gogh = map.computeIfAbsent("Van Gogh Museum", key -> {
            throw new IllegalStateException();
        });

        It.println("van_gogh = " + van_gogh);

        assertEquals(expected, van_gogh);
    }

    @Test
    void testMapToList() {
        final var museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final var pairs = museumMapX.map(Pair::ofEntry);

        assertEquals(new HashSetX<>(List.of("Picasso Museum", "Van Gogh Museum", "Vermeer Museum")), pairs.toSetXOf(Pair::first));
    }

    @Test
    void testFlatMapToList() {
        final var museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final var paintings = museumMapX.flatMap(e -> e.getValue().getPaintings());

        final var expected = "Meisje met de rode hoed";

        final var actual = ListX.of(paintings).maxOf(Painting::name);

        assertEquals(expected, actual);
    }

    @Test
    void testMapOfPairs() {
        final var hallo = StringX.of("Hallo");
        final var map = MapX.ofPairs(hallo.to("s"), StringX.of("asd").to("asdd"));

        assertAll(
                () -> assertEquals(2, map.size()),
                () -> assertTrue(map.containsValue("asdd"))
        );
    }

    @Test
    void testBuildMap() {
        final MapX<Integer, LocalDate> map = MapX.build(m ->
                IntRange.of(1990, 2022).forEachInt(year -> m.put(year, LocalDate.of(year, 1, 1))));

        map.forEach(It::println);

        It.println("map = " + map);

        assertEquals(32, map.size());
    }

    @Nested
    class EqualsTests {

        @Test
        void testMapXEquals() {
            final var map1 = MapX.of("This", "is", "a", "test");
            final var map2 = MapX.of("This", "is", "a", "test");

            assertAll(
                    () -> assertEquals(map2, map1),
                    () -> assertEquals(map1, map2)
            );
        }

        @Test
        void testMapXAndJavaMapDoNotEqual() {
            final var mapX = MapX.of("This", "is", "a", "test");
            final var map = Map.of("This", "is", "a", "test");

            assertAll(
                    () -> assertNotEquals(map, mapX),
                    () -> assertNotEquals(mapX, map)
            );
        }
    }
}
