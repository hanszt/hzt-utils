package hzt.collections;

import hzt.ranges.IntRange;
import hzt.sequences.EntrySequence;
import hzt.strings.StringX;
import hzt.tuples.IndexedValue;
import hzt.tuples.Pair;
import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapXTest {

    @Test
    void testInvertMap() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final Map<Museum, String> expected = museumMap.entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getValue(), e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        final MapX<Museum, String> actual = MapX.of(museumMap).inverted();

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testToIterXThanSum() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final int expected = museumMap.values().stream()
                .map(Museum::getDateOfOpening)
                .mapToInt(LocalDate::getDayOfMonth)
                .sum();

        final long actual = MapX.of(museumMap)
                .mapValuesTo(MutableListX::of, Museum::getDateOfOpening)
                .sumOfInts(LocalDate::getDayOfMonth);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testForEachIndexed() {
        List<IndexedValue<Map.Entry<String, Museum>>> list = new ArrayList<>();
        BiConsumer<Integer, Map.Entry<String, Museum>> biConsumer = (index, value) -> list.add(new IndexedValue<>(index, value));

        final List<Museum> museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        ListX.of(museumListContainingNulls).associateBy(Museum::getName).forEachIndexed(biConsumer);

        list.forEach(It::println);

        assertEquals(3, list.size());
    }

    @Test
    void flatMapToListOf() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final List<Painting> expected = museumMap.entrySet().stream()
                .flatMap(e -> e.getValue().getPaintings().stream())
                .collect(Collectors.toList());

        final List<Painting> actual = MapX.of(museumMap).flatMapValuesTo(MutableListX::of, Museum::getPaintings);

        It.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void entrySequenceOfMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);

        final MapX<String, Year> result = EntrySequence.of(map).mapValues(Year::of).toMapX();

        assertEquals(MapX.of("1", Year.of(1), "2", Year.of(2), "3", Year.of(3)), result);
    }

    @Test
    void testMapOfMapXSameAsInputMap() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final MapX<String, Museum> mapX = MapX.of(museumMap);
        MapX.of(mapX).entrySet().forEach(It::println);

        It.println("mapX = " + mapX);

        assertEquals(museumMap, mapX);
    }

    @Test
    void testComputeIfAbsent() {
        final MapX<String, Museum> museumMap = ListX.of(TestSampleGenerator.createMuseumList())
                .associateBy(Museum::getName);

        Museum expected = museumMap.get("Van Gogh Museum");

        final MutableMapX<String, Museum> mapX = MutableMapX.of(museumMap);

        final Museum van_gogh = mapX.computeIfAbsent("Van Gogh Museum", key -> {
            throw new IllegalStateException();
        });

        It.println("van_gogh = " + van_gogh);

        assertEquals(expected, van_gogh);
    }

    @Test
    void testMapXToList() {
        final MapX<String, Museum> museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final ListX<Pair<String, Museum>> pairs = museumMapX.map(Pair::ofEntry);

        assertEquals(new HashSetX<>(Arrays.asList("Picasso Museum", "Van Gogh Museum", "Vermeer Museum")), pairs.toSetXOf(Pair::first));
    }

    @Test
    void testFlatMapToList() {
        final MapX<String, Museum> museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final ListX<Painting> pairs = museumMapX.flatMap(e -> e.getValue().getPaintings());

        final String expected = "Meisje met de rode hoed";

        final String actual = ListX.of(pairs).maxOf(Painting::name);

        assertEquals(expected, actual);
    }

    @Test
    void testMapOfPairs() {
        final StringX hallo = StringX.of("Hallo");
        final MapX<StringX, String> map = MapX.ofPairs(hallo.to("s"), StringX.of("asd").to("asdd"));
        assertEquals(2, map.size());
        assertTrue(map.containsValue("asdd"));
    }

    @Test
    void testBuildMap() {
        final MapX<Integer, LocalDate> mapX = MapX.build(map ->
                IntRange.of(1990, 2022).forEach(year -> map.put(year, LocalDate.of(year, 1, 1))));

        mapX.forEach(It::println);

        It.println("mapX = " + mapX);

        assertEquals(32, mapX.size());
    }
}
