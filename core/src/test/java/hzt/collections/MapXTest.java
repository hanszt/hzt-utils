package hzt.collections;

import hzt.function.It;
import hzt.sequences.Sequence;
import hzt.strings.StringX;
import hzt.tuples.Pair;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
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

        final MapX<Museum, String> actual = MapX.of(museumMap).toInvertedMap();

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testToIterXThanSum() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final int expected = museumMap.values().stream()
                .map(Museum::getDateOfOpening)
                .mapToInt(LocalDate::getDayOfMonth)
                .sum();

        final int actual = MapX.of(museumMap)
                .valuesToIterX(Museum::getDateOfOpening)
                .sumOfInts(LocalDate::getDayOfMonth);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testForEachIndexed() {
        List<IndexedValue<Map.Entry<String, Museum>>> list = new ArrayList<>();
        BiConsumer<Integer, Map.Entry<String, Museum>> biConsumer = (index, value) -> list.add(new IndexedValue<>(index, value));

        final List<Museum> museumListContainingNulls = TestSampleGenerator.getMuseumListContainingNulls();

        ListX.of(museumListContainingNulls).associateBy(Museum::getName).forEachIndexed(biConsumer);

        list.forEach(System.out::println);

        assertEquals(3, list.size());
    }

    @Test
    void flatMapToListOf() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final List<Painting> expected = museumMap.entrySet().stream()
                .flatMap(e -> e.getValue().getPaintings().stream())
                .collect(Collectors.toList());

        final List<Painting> actual = MapX.of(museumMap).flatMapValuesToListOf(Museum::getPaintings);

        System.out.println("actual = " + actual);

        assertEquals(expected, actual);
    }

    @Test
    void testMapOfMapXSameAsInputMap() {
        final Map<String, Museum> museumMap = TestSampleGenerator.createMuseumMap();

        final MapX<String, Museum> mapX = MapX.of(museumMap);
        MapX.of(mapX).entrySet().forEach(System.out::println);

        System.out.println("mapX = " + mapX);

        assertEquals(museumMap, mapX);
    }

    @Test
    void testComputeIfAbsent() {
        final MutableMapX<String, Museum> museumMap = ListX.of(TestSampleGenerator.createMuseumList())
                .associateBy(Museum::getName);

        Museum expected = museumMap.get("Van Gogh Museum");

        final MutableMapX<String, Museum> mapX = MutableMapX.of(museumMap);

        final Museum van_gogh = mapX.computeIfAbsent("Van Gogh Museum", key -> {
            throw new IllegalStateException();
        });

        System.out.println("van_gogh = " + van_gogh);

        assertEquals(expected, van_gogh);
    }

    @Test
    void testMapXToList() {
        final MapX<String, Museum> museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final ListX<Pair<String, Museum>> pairs = museumMapX.toListXOf(Pair::ofEntry);

        assertEquals(new HashSetX<>(Arrays.asList("Picasso Museum", "Van Gogh Museum", "Vermeer Museum")), pairs.toSetXOf(Pair::first));
    }

    @Test
    void testFlatMapToList() {
        final MapX<String, Museum> museumMapX = MapX.of(TestSampleGenerator.createMuseumMap());

        final ListX<Painting> pairs = museumMapX.flatMapToListXOf(e -> e.getValue().getPaintings());

        final String expected = "Meisje met de rode hoed";

        final String actual = ListX.of(pairs).maxOf(Painting::name);

        assertEquals(expected, actual);
    }

    @Test
    void testMapOfPairs() {
        final var hallo = StringX.of("Hallo");
        final var map = MapX.of(hallo.to("s"), StringX.of("asd").to("asdd"));
        assertEquals(2, map.size());
        assertTrue(map.containsValue("asdd"));
    }

    @Test
    void testBuildMap() {
        final var mapX = MapX.<Integer, LocalDate>build(map ->
                Sequence.range(1990, 2022).forEach(year -> map.put(year, LocalDate.of(year, 1, 1))));

        mapX.forEach(It::println);

        It.println("mapX = " + mapX);

        assertEquals(32, mapX.size());
    }
}
