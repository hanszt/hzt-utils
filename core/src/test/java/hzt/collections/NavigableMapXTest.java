package hzt.collections;

import hzt.iterables.IterableX;
import hzt.utils.It;
import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.junit.jupiter.api.Test;

import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class NavigableMapXTest {

    @Test
    void testGetNavigableMap() {
        final IterableX<Museum> museumListContainingNulls = ListX.of(TestSampleGenerator.getMuseumListContainingNulls())
                .sortedBy(e -> Math.random());

        final NavigableMap<String, Museum> expected = new TreeMap<>(museumListContainingNulls.stream()
                .filter(m -> m != null && m.getName() != null)
                .collect(Collectors.toMap(Museum::getName, It::self)));

        final NavigableMapX<String, Museum> actual = museumListContainingNulls
                .toSortedMapAssociatedBy(Museum::getName);

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

        final NavigableMapX<Museum, String> actual = museumSetContainingNulls
                .filterBy(Museum::getName, Objects::nonNull)
                .toSortedMapAssociatedWith(Museum::getName);

        final Museum firstMuseum = actual.first().getKey();

        It.println("firstMuseum = " + firstMuseum);

        assertAll(
                () -> assertIterableEquals(actual.keySet(), expected.keySet()),
                () -> assertIterableEquals(actual.values(), expected.values())
        );
    }

}
