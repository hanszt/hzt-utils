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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapXTest {

    @Test
    void testInvertMap() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final Map<Museum, String> expected = museumMap.entrySet().stream()
                .map(e -> MapX.entry(e.getValue(), e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final MapX<Museum, String> actual = MapX.of(museumMap).inverted();

        It.println("actual = " + actual);

        assertEquals(expected.entrySet(), actual.entrySet());
    }

    @Test
    void testToIterXThanSum() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final int expected = museumMap.values().stream()
                .map(Museum::getDateOfOpening)
                .mapToInt(LocalDate::getDayOfMonth)
                .sum();

        final long actual = MapX.of(museumMap)
                .mapValuesTo(MutableListX::empty, Museum::getDateOfOpening)
                .intSumOf(LocalDate::getDayOfMonth);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testForEachIndexed() {
        final List<IndexedValue<Map.Entry<String, Museum>>> list = new ArrayList<>();
        final IndexedConsumer<Map.Entry<String, Museum>> biConsumer = (index, value) -> list.add(new IndexedValue<>(index, value));

        final List<Museum> museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        ListX.of(museumListContainingNulls).associateBy(Museum::getName).forEachIndexed(biConsumer);

        list.forEach(It::println);

        assertEquals(3, list.size());
    }

    @Test
    void flatMapToListOf() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final Set<Painting> expected = museumMap.entrySet().stream()
                .flatMap(e -> e.getValue().getPaintings().stream())
                .collect(Collectors.toSet());

        final Set<Painting> actual = MapX.of(museumMap).flatMapValuesTo(MutableSetX::empty, Museum::getPaintings);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void entrySequenceOfMap() {
        final Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        final MapX<String, Year> result = EntrySequence.of(map).mapByValues(Year::of).toMapX();

        final MapX<String, Year> expected = MapX.of("1", Year.of(1), "2", Year.of(2), "3", Year.of(3));
        assertEquals(expected.entrySet(), result.entrySet());
    }

    @Test
    void testMapXOfMapSameAsInputMap() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final MapX<String, Museum> mapX = MapX.of(museumMap);
        MapX.of(mapX).entrySet().forEach(It::println);

        It.println("map = " + mapX);

        assertEquals(museumMap.entrySet(), mapX.entrySet());
    }

    @Test
    void testComputeIfAbsent() {
        final MapX<String, Museum> museumMap = ListX.of(TestSampleGenerator.createMuseumList())
                .associateBy(Museum::getName);

        final Museum expected = museumMap.get("Van Gogh Museum");

        final MutableMapX<String, Museum> map = MutableMapX.of(museumMap);

        final Museum van_gogh = map.computeIfAbsent("Van Gogh Museum", key -> {
            throw new IllegalStateException();
        });

        It.println("van_gogh = " + van_gogh);

        assertEquals(expected, van_gogh);
    }

    @Test
    void testMapToList() {
        final MapX<String, Museum> museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final ListX<Pair<String, Museum>> pairs = museumMapX.map(Pair::ofEntry);

        assertEquals(new HashSetX<>(Arrays.asList("Picasso Museum", "Van Gogh Museum", "Vermeer Museum")), pairs.toSetXOf(Pair::first));
    }

    @Test
    void testFlatMapToList() {
        final MapX<String, Museum> museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final ListX<Painting> paintings = museumMapX.flatMap(e -> e.getValue().getPaintings());

        final String expected = "Meisje met de rode hoed";

        final String actual = ListX.of(paintings).maxOf(Painting::name);

        assertEquals(expected, actual);
    }

    @Test
    void testMapOfPairs() {
        final StringX hallo = StringX.of("Hallo");
        final MapX<StringX, String> map = MapX.ofPairs(hallo.to("s"), StringX.of("asd").to("asdd"));

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
            final MapX<String, String> map1 = MapX.of("This", "is", "a", "test");
            final MapX<String, String> map2 = MapX.of("This", "is", "a", "test");

            assertAll(
                    () -> assertEquals(map2, map1),
                    () -> assertEquals(map1, map2)
            );
        }

        @Test
        void testMapXAndJavaMapDoNotEqual() {
            final MapX<String, String> mapX = MapX.of("This", "is", "a", "test");
            final Map<String, String> map = MapX.of("This", "is", "a", "test").toMap();

            assertAll(
                    () -> assertNotEquals(map, mapX),
                    () -> assertNotEquals(mapX, map)
            );
        }
    }
}
